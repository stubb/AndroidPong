package gameengine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
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
	
	int screenWidth = 300;
	int screenHeight = 420;
	
	//The Arena
	int borderSize = 5; 

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
	int _topBatX = (screenWidth/2) - (_batLength / 2);
	int _topBatY = spaceToBorder;
	int _bottomBatX = (screenWidth/2) - (_batLength / 2);	
	int _bottomBatY = 600;
	final int _batSpeed = 3;

	public GameState(Point screenSize)
	{
		setScreenDimensions(screenSize);
		borderSize = (int)(screenWidth * 0.02);
	}

	private void setScreenDimensions(Point screenSize) {
		screenWidth = screenSize.x;
		screenHeight = screenSize.y;
	}
	
	//The update method
	public void update() {

		_ballX += _ballVelocityX;
		_ballY += _ballVelocityY;

		//DEATH!
		if(_ballY >= screenHeight || _ballY <= 0) {
			_ballX = screenWidth / 2;
			_ballY = screenHeight / 2;
		}  	
		
		//Collisions with the sides
		if(_ballX >= screenWidth || _ballX <= 0) {
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
		Boolean bla = false;
		//Log.e(TAG, "vol up");
		if(keyCode == KeyEvent.KEYCODE_VOLUME_UP) //left
		{
			_topBatX += _batSpeed;
			_bottomBatX -= _batSpeed;
			//Log.e(TAG, "vol up");
			bla = true;
		}

		if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) //right
		{
			_topBatX -= _batSpeed;
			_bottomBatX += _batSpeed;
			bla = true;
		}
		//Log.e(TAG, bla.toString());
		return bla;
	}
	
	private void drawArena(Canvas canvas) {
		Paint paint = new Paint();
		
		// Border
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(borderSize);
		paint.setARGB(255, 255, 255, 255);
		canvas.drawRect(new Rect(borderSize/2, borderSize/2, screenWidth - borderSize/2, screenHeight - borderSize/2), paint);
		
		// Middle Line
		paint.setPathEffect(new DashPathEffect(new float[]{borderSize, borderSize * 0.6f}, 0));
		canvas.drawLine(0, screenHeight / 2, screenWidth, screenHeight / 2, paint);
	}
	//the draw method
	public void draw(Canvas canvas) {

		Paint paint = new Paint();
		//Clear the screen
		canvas.drawRGB(20, 20, 20);
		
		Paint paintText = new Paint(); 
		canvas.drawPaint(paintText); 
		paintText.setColor(Color.WHITE); 
		paintText.setTextSize(16); 
		canvas.drawText("My Text", 50, 100, paintText); 
		

		paint.setARGB(200, 255, 0, 0);
		drawArena(canvas);
		

		
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