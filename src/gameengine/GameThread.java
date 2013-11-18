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
  
	/** Handle to the surface manager object we interact with */
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
		int i = 1;
		while(gameRunning)
		{
			Canvas canvas = _surfaceHolder.lockCanvas();
			gameRunning = _state.update();
			_state.draw(canvas);
			_surfaceHolder.unlockCanvasAndPost(canvas);
			Log.e(LOGTAG, Integer.toString(i));
			i++;
		}
	}

	public GameState getGameState()
	{
		return _state;
	}
}