package gameengine;

import android.os.Handler;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    
   private GameThread _thread;
   private Context ctx;

   public GameView(Context context, AttributeSet attrs) {
       super(context, attrs);
       ctx = context;
   	//So we can listen for events...
       SurfaceHolder holder = getHolder();
       holder.addCallback(this);
       //setFocusable(true); 
       requestFocus();
       setFocusableInTouchMode(true);
       //and instantiate the thread
       _thread = new GameThread(holder, new Handler(), getScreensize());
   }  

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent msg) {
       return _thread.getGameState().keyPressed(keyCode, msg);
   }

   //Implemented as part of the SurfaceHolder.Callback interface
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		//Mandatory, just swallowing it for this example

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		_thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (_thread != null) {
			try {
			_thread.interrupt();
			_thread.join();
			_thread = null;
			} catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
	}
	
	@SuppressLint("NewApi")
	private Point getScreensize() {
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		Point size = new Point(1,1);

		if (android.os.Build.VERSION.SDK_INT >= 13) {
			display.getSize(size);
		}
		else { 
			size.set(display.getWidth(), display.getHeight()); 
		}
		return size;
	}
}