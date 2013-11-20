package gameengine;

import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
  
	/**
	 * The tag is used to identify the class while logging
	 */
	private final String LOGTAG = getClass().getName();
  
	/**
	 * Handle to the surface manager object we interact with
	 */
	private SurfaceHolder _surfaceHolder;
	private GameState _state;

	public GameThread(SurfaceHolder surfaceHolder, Handler handler, Point screenSize)
	{
		_surfaceHolder = surfaceHolder;
		_state = new GameState(screenSize);
	}

	@Override
	public void run() {
		boolean gameRunning = true;
		while(gameRunning)
		{
			try {
				gameRunning = _state.update();
				Canvas canvas = _surfaceHolder.lockCanvas();	
				_state.draw(canvas);
				_surfaceHolder.unlockCanvasAndPost(canvas);
			} catch (NullPointerException e) {
				gameRunning = false;
			}
		}
	}

	public GameState getGameState()
	{
		return _state;
	}
}