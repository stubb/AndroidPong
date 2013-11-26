package gameengine;

import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
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

	public GameThread(SurfaceHolder surfaceHolder, Handler handler, Point screenSize, String gameMode)
	{
		_surfaceHolder = surfaceHolder;
		_state = new GameState(screenSize, gameMode);
	}

	@Override
	public void run() {
	  int FRAMES_PER_SECOND = 60;
	  int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
	  long next_game_tick = SystemClock.uptimeMillis();
	  long sleep_time = 0;
	  boolean gameRunning = true;
	  
		while(gameRunning)
		{
			try {
				gameRunning = _state.update();
				Canvas canvas = _surfaceHolder.lockCanvas();	
				_state.draw(canvas);
				_surfaceHolder.unlockCanvasAndPost(canvas);

        next_game_tick += SKIP_TICKS;
        sleep_time = next_game_tick - SystemClock.uptimeMillis();
        if(sleep_time >= 0) {
            Thread.sleep(sleep_time);
        }
        else {
            // Shit, we are running behind!
        }
			} catch (NullPointerException e) {
				gameRunning = false;
			} catch (InterruptedException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
      }
		}
	}

	public GameState getGameState()
	{
		return _state;
	}
}