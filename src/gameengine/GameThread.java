package gameengine;

import java.util.List;

import constants.Values;
import exceptions.GameOverException;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	/**
	 * Handle to the surface manager object we interact with
	 */
	private SurfaceHolder _surfaceHolder;
	private GameState _state;
	private Context _context;

	public GameThread(Context ctx, SurfaceHolder surfaceHolder, Handler handler, Point screenSize, String gameMode)
	{
		_surfaceHolder = surfaceHolder;
		_state = new GameState(screenSize, gameMode);
		_context = ctx;
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
		if(this._state.getGameMode().equals(Values.GAME_MODE_NORMAL) ||
				this._state.getGameMode().equals(Values.GAME_MODE_EXPERT)) {
//			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//					_context);
//	 
//				// set title
//				alertDialogBuilder.setTitle("Game over!");
//	 
//				// set dialog message
//				alertDialogBuilder
//					.setMessage("Click \"Post\" to save your result or \"Cancel\""
//							+ "to go back to the main menu")
//					.setCancelable(false)
//					.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog,int id) {
//							dialog.cancel();
//						}
//					});
	 
			
			if(this._state.getGameMode().equals(Values.GAME_MODE_NORMAL)) {
//				alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int id) {
//						// if this button is clicked, close
//						// current activity
//					}
//				  });
			}
			else if(this._state.getGameMode().equals(Values.GAME_MODE_EXPERT)) {
//				alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int id) {
//						// if this button is clicked, close
//						// current activity
//					}
//				  });
			}
			// create alert dialog			
//			AlertDialog alertDialog = alertDialogBuilder.create();
 
			// show it
//			alertDialog.show();
			
		}
	}

	public GameState getGameState()
	{
		return _state;
	}
}