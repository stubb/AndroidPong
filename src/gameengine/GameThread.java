package gameengine;

import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	/**
	 * Handle to the surface manager object we interact with
	 */
	private SurfaceHolder _surfaceHolder;
	private GameState _state;
	public static Handler msgHandler;

	public GameThread(SurfaceHolder surfaceHolder, Handler handler, Point screenSize, String gameMode)
	{
		_surfaceHolder = surfaceHolder;
		_state = new GameState(screenSize, gameMode);
	}

	@Override
	public void run() {

	  
	  Looper.prepare();
	  

	 msgHandler = new Handler() {
	    public void handleMessage(Message msg) {
	      // process incoming messages here
	  		System.out.println("Here be lions");
	  		
	  	  int FRAMES_PER_SECOND = 60;
	  	  int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
	  	  long next_game_tick = SystemClock.uptimeMillis();
	  	  long sleep_time = 0;
	  	  boolean gameRunning = true;
	  	  
	  		while(gameRunning)
	  		{
	  			try {
	  				Integer data = 0;
	  				if(msg.what == 999) {
	  					data = (Integer) msg.obj;
	  				}
	  				gameRunning = _state.update(data);
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
	  				Looper.myLooper().quit();
	  				
	  			} catch (InterruptedException e) {
	  	      // TODO Auto-generated catch block
	  	      e.printStackTrace();
	        }
	  		}
	  }
	  };

	  System.out.println("Muh");
    Looper.loop();
	  System.out.println("Muh2");


	}

	public GameState getGameState()
	{
		return _state;
	}
}