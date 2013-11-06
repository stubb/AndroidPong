package gameengine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.KeyEvent;

public class GameState {
	
	// Bildschirmgroessen.
	private int screenWidth;
	private int screenHeight;
	
	// Der Wert der dafuer sorgt, dass alles schoen relativ ist.
	private int multiplicator;
	
	// Spieler 1 Schlaeger.
	private int p1_batX;
	private int p1_batY;
	private int p1_batLength;
	private int p1_batHeight;
	
	// Spieler 2 Schlaeger.
	private int p2_batX;
	private int p2_batY;
	private int p2_batLength;
	private int p2_batHeight;
	
	// Schlaeger Bewegungsgeschwindigkeit.
	private int batSpeed;
	
	// Spielstaende.
	private int p1_score;
	private int p2_score;

	//The ball
	private int ballSize = 10;
	private int ballX = 100;
	private int ballY = 100;
	private int ballSpeedX;
	private int ballSpeedY;

	public GameState(Point screenSize)
	{
		setScreenDimensions(screenSize);
		
		// multiplicator wird auf 2% der Bildschirmhoehe (lange Seite) festgelegt.
		multiplicator = (int)(screenWidth * 0.02);
		
		ballSize = multiplicator * 2;
		ballX = screenWidth / 2;
		ballY = screenHeight / 2;
		
		p1_batLength = (int)(screenWidth * 0.2);
		p1_batHeight = multiplicator;
		p1_batX = screenWidth / 2 - p1_batLength / 2;
		p1_batY = screenHeight - 3 * multiplicator;
		
		p2_batLength = screenWidth;//(int)(screenWidth * 0.2);
		p2_batHeight = multiplicator;
		p2_batX = 0;//screenWidth / 2 - p1_batLength / 2;
		p2_batY = 2 * multiplicator;
		
		batSpeed = multiplicator;
		ballSpeedX = multiplicator / 5;
		ballSpeedY = multiplicator / 5;
		
		p1_score = 0;
		p2_score = 0;
	}

	private void setScreenDimensions(Point screenSize) {
		screenWidth = screenSize.x;
		screenHeight = screenSize.y;
	}
	
	//The update method
	public void update() {

		ballX += ballSpeedX;
		ballY += ballSpeedY;

		// Player 2 Death.
		if (ballY <= 0) {
			p1_score += 1;
			resetBall();
		}
		// Player 1 Death.
		if (ballY + ballSize / 2 >= screenHeight) {
			p2_score += 1;
			resetBall();
		}
		
		// Seitenkollision.
		if (ballX - ballSize / 2 <= 0 || ballX + ballSize / 2 >= screenWidth) {
			ballSpeedX *= -1;
		}
				
		// Kollision mit Player 1
		if (ballY + ballSize / 2 >= p1_batY && (ballX - ballSize / 2 >= p1_batX && ballX + ballSize / 2 <= p1_batX + p1_batLength)) {
			ballSpeedY *= -1;
		}
		
		// Kollision mit Player 2
		if (ballY - ballSize / 2 <= p2_batY + p2_batHeight && (ballX - ballSize / 2 >= p2_batX && ballX + ballSize / 2 <= p2_batX + p2_batLength)) {
			ballSpeedY *= -1;
		}
	}
	
	private void resetBall() {
		ballX = screenWidth / 2;
		ballY = screenHeight / 2;
	}

	// DONE
	public boolean keyPressed(int keyCode, KeyEvent msg)
	{
		Boolean pressed = false;
		if(keyCode == KeyEvent.KEYCODE_VOLUME_UP) //left
		{
			if (p1_batX - batSpeed >= 0)
				p1_batX -= batSpeed;
			pressed = true;
		}

		if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) //right
		{
			if (p1_batX + p1_batLength + batSpeed <= screenWidth)
				p1_batX += batSpeed;
			pressed = true;
		}
		return pressed;
	}
	
	// DONE
	private void clearScreen(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
	}
	// DONE	
	private void drawArena(Canvas canvas) {
		
		Paint paint = new Paint();
		
		// Border
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(multiplicator);
		paint.setARGB(255, 255, 255, 255);
		canvas.drawRect(new Rect(multiplicator / 2, multiplicator / 2, screenWidth - multiplicator / 2, screenHeight - multiplicator / 2), paint);
		
		// Middle Line
		paint.setStyle(Paint.Style.STROKE);
		paint.setPathEffect(new DashPathEffect(new float[]{multiplicator, multiplicator * 0.6f}, 0));
		canvas.drawLine(0, screenHeight / 2, screenWidth, screenHeight / 2, paint);
	}
	// DONE	
	private void drawScore(Canvas canvas) {
		
		Paint paint = new Paint();
		
		paint.setARGB(255, 255, 255, 255);
		paint.setTextSize(5 * multiplicator);
		
		// Player 1
		canvas.drawText(Integer.toString(p1_score), 0.85f * screenWidth, 0.6f * screenHeight, paint);
		
		// Player 2
		canvas.drawText(Integer.toString(p2_score), 0.85f * screenWidth, 0.4f * screenHeight + 4 * multiplicator, paint);
		
		canvas.drawText(Integer.toString(multiplicator), 100, 100, paint);
	}
	// DONE	
	private void drawPlayerBars(Canvas canvas) {
		
		Paint paint = new Paint();
		
		paint.setARGB(255, 255, 255, 255);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(multiplicator);
		
		// Player 1
		canvas.drawRect(new Rect(p1_batX, p1_batY, p1_batX + p1_batLength, p1_batY + p1_batHeight), paint);
		
		// Player 2
		canvas.drawRect(new Rect(p2_batX, p2_batY, p2_batX + p2_batLength, p2_batY + p2_batHeight), paint);
		
	}
	// DONE	
	private void drawBall(Canvas canvas) {
		
		Paint paint = new Paint();
		
		paint.setARGB(255, 0, 128, 0);
		
		canvas.drawCircle(ballX, ballY, ballSize, paint);
	}
	
	//the draw method
	public void draw(Canvas canvas) {
		clearScreen(canvas);
		drawArena(canvas);
		drawScore(canvas);
		drawPlayerBars(canvas);
		drawBall(canvas);
	}
}