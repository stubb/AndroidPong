package foo.bar.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	/** Handle to the surface manager object we interact with */
	private SurfaceHolder _surfaceHolder;
	private Paint _paint;
	private GameState _state;

	public GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler)
	{
		_surfaceHolder = surfaceHolder;
		_paint = new Paint();
		_state = new GameState(context);
	}

	@Override
	public void run() {
		while(true)
		{
			Canvas canvas = _surfaceHolder.lockCanvas();
			_state.update();
			_state.draw(canvas,_paint);
			_surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	public GameState getGameState()
	{
		return _state;
	}
}