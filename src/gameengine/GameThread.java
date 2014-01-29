package gameengine;

import singleton.UtilitySingleton;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import constants.Values;

public class GameThread extends Thread {

	/**
	 * Handle to the surface manager object we interact with
	 */
	private SurfaceHolder _surfaceHolder;
	private GameState _state;

	public GameThread(SurfaceHolder surfaceHolder, Handler handler,
			Point screenSize, String gameMode) {
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

		while (gameRunning) {
			try {
				gameRunning = _state.update();
				Canvas canvas = _surfaceHolder.lockCanvas();
				_state.draw(canvas);
				_surfaceHolder.unlockCanvasAndPost(canvas);

				next_game_tick += SKIP_TICKS;
				sleep_time = next_game_tick - SystemClock.uptimeMillis();
				if (sleep_time >= 0) {
					Thread.sleep(sleep_time);
				} else {
					// Shit, we are running behind!
				}
			} catch (NullPointerException e) {
				gameRunning = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (this._state.getGameMode().equals(Values.GAME_MODE_NORMAL)
				|| this._state.getGameMode().equals(Values.GAME_MODE_EXPERT)) {
			UtilitySingleton.getInstance().getCurrentActivity()
					.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									UtilitySingleton.getInstance()
											.getCurrentActivity());

							// set title
							alertDialogBuilder.setTitle("Game over!");
							// set dialog message
							alertDialogBuilder
									.setMessage(
											"Click \"Post\" to save your result or \"Cancel\""
													+ "to go back to the main menu")
									.setCancelable(false);

							if (_state.getGameMode().equals(
									Values.GAME_MODE_NORMAL)) {
								alertDialogBuilder.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// TODO add function to save game in database
												System.out.println("Debug: save!");
												UtilitySingleton.getInstance().getCurrentActivity().finish();
											}
										});
							} else if (_state.getGameMode().equals(
									Values.GAME_MODE_EXPERT)) {
								alertDialogBuilder.setPositiveButton("Save",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// TODO add function to save game in database
												System.out.println("Debug: save!");
												UtilitySingleton.getInstance().getCurrentActivity().finish();
											}
										});
							}
							
							alertDialogBuilder.setNegativeButton(
									"Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int id) {
											UtilitySingleton.getInstance().getCurrentActivity().finish();
											dialog.cancel();
										}
									});
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
					});
		}
	}

	public GameState getGameState() {
		return _state;
	}
}