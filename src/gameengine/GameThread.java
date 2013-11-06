package gameengine;

import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

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
		while(true)
		{
			Canvas canvas = _surfaceHolder.lockCanvas();
			_state.update();
			_state.draw(canvas);
			_surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	public GameState getGameState()
	{
		return _state;
	}
}