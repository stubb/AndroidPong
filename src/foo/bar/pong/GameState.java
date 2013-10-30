package foo.bar.pong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;

public class GameState {


    /**
     * The tag is used to identify the class while logging.
     */
    private final String TAG = getClass().getName();
    
	private Context ctx = null;
	
	//screen width and height
	Integer _screenWidth = 300;
	Integer _screenHeight = 420;

	//The ball
	final int _ballSize = 10;
	int _ballX = 100;
	int _ballY = 100;
	int _ballVelocityX = 3;
	int _ballVelocityY = 3;

	//The bats
	final int spaceToBorder = 20;
	final int _batLength = 75;
	final int _batHeight = 10;
	int _topBatX = (_screenWidth/2) - (_batLength / 2);
	int _topBatY = spaceToBorder;
	int _bottomBatX = (_screenWidth/2) - (_batLength / 2);	
	int _bottomBatY = 600;
	final int _batSpeed = 3;

	public GameState(Context context)
	{
		ctx = context;
		setScreenDimensions();
	}

	@SuppressLint("NewApi")
	private void setScreenDimensions() {
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		Point size = new Point(1,1);
		
		Log.e(TAG, "bla");

		if (android.os.Build.VERSION.SDK_INT >= 13) {
			display.getSize(size);
			_screenWidth = size.x;
			_screenHeight = size.y;
		}
		else { 
			_screenWidth = display.getWidth(); 
			_screenHeight = display.getHeight();
		}
		Log.e(TAG, _screenWidth.toString());
		Log.e(TAG, _screenHeight.toString());
				
	}
	
	//The update method
	public void update() {

		_ballX += _ballVelocityX;
		_ballY += _ballVelocityY;

		//DEATH!
		if(_ballY >= _screenHeight || _ballY <= 0) {
			_ballX = _screenWidth / 2;
			_ballY = _screenHeight / 2;
		}  	
		
		//Collisions with the sides
		if(_ballX >= _screenWidth || _ballX <= 0) {
			_ballVelocityX *= -1;
		}
		
		//Collisions with the bats     	
		if(_ballX > _topBatX && _ballX < _topBatX+_batLength && _ballY < _topBatY) {   	
			_ballVelocityY *= -1;
		}
		
		//Collisions with the bats     	
		if(_ballX > _bottomBatX && _ballX < _bottomBatX+_batLength && _ballY > _bottomBatY) {
			_ballVelocityY *= -1;
		}
	}

	public boolean keyPressed(int keyCode, KeyEvent msg)
	{
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) //left
		{
			_topBatX += _batSpeed;
			_bottomBatX -= _batSpeed;
		}

		if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) //right
		{
			_topBatX -= _batSpeed;
			_bottomBatX += _batSpeed;
		}

		return true;
	}
	
	//the draw method
	public void draw(Canvas canvas, Paint paint) {

		
		//Clear the screen
		canvas.drawRGB(20, 20, 20);

		paint.setARGB(200, 255, 0, 0);		

		// border
		Paint p = new Paint();
		
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(1);
		p.setARGB(255, 255, 0, 0);

		// border
		canvas.drawRect(new Rect(0, 0, _screenWidth - 1, _screenHeight - 1), p);
		
		//set the colour
		paint.setARGB(200, 0, 200, 0);

		//draw the ball
		canvas.drawRect(new Rect(_ballX,_ballY,_ballX + _ballSize,_ballY + _ballSize), paint);

		//draw the bats
		canvas.drawRect(new Rect(_topBatX, _topBatY, _topBatX + _batLength, _topBatY + _batHeight), paint); //top bat
		canvas.drawRect(new Rect(_bottomBatX, _bottomBatY, _bottomBatX + _batLength, _bottomBatY + _batHeight), paint); //bottom bat
		
		paint.setARGB(200, 255, 0, 0);		
		// border
		//canvas.drawRect(new Rect(0, 0, _screenWidth, _screenHeight), p);
	}
}