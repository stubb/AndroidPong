package foo.bar.pong;

import android.os.Handler;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
	
    /**
     * The tag is used to identify the class while logging.
     */
    private final String TAG = getClass().getName();
    
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
       _thread = new GameThread(holder, context, new Handler(), getScreensize());
   }  

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent msg) {
	   //Log.e(TAG, "on key down");
       return _thread.getGameState().keyPressed(keyCode, msg);
   }

   //Implemented as part of the SurfaceHolder.Callback interface
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		//Mandatory, just swallowing it for this example

	}

   //Implemented as part of the SurfaceHolder.Callback interface
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		_thread.start();
	}

   //Implemented as part of the SurfaceHolder.Callback interface
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
       _thread.stop();
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