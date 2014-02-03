package gameengine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import singleton.UtilitySingleton;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.view.SurfaceHolder;
import android.widget.Toast;
import constants.Values;

/**
 * This is an own thread holding the complete game.
 */
public class GameThread extends Thread {

	/**
	 * Handle to the surface manager object we interact with to draw the game.
	 */
	private SurfaceHolder _surfaceHolder;
	private GameState _state;
	private SharedPreferences settings;
	private Context ctx;

	public GameThread(Context context, SurfaceHolder surfaceHolder, Handler handler,
			Point screenSize, String gameMode) {
		ctx = context;
		_surfaceHolder = surfaceHolder;
		_state = new GameState(screenSize, gameMode);
		settings = ctx.getSharedPreferences(Values.CONFIG, ctx.MODE_PRIVATE);
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
								alertDialogBuilder.setPositiveButton("Post",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// TODO add function to save game in database
												sendNormalModeData();
												System.out.println("Debug: save normal!");
												UtilitySingleton.getInstance().getCurrentActivity().finish();
											}
										});
							} else if (_state.getGameMode().equals(
									Values.GAME_MODE_EXPERT)) {
								alertDialogBuilder.setPositiveButton("Post",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// TODO add function to save game in database
												sendExpertModeData();
												System.out.println("Debug: save expert!");
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
	
	/**
	 * Sends the normal mode highscore data to the server.
	 */
	private void sendNormalModeData() {
		Thread networkThread = new Thread() {
			public void run() {

				String[] data = new String[4];
				TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
				data[0] = telephonyManager.getDeviceId();
				data[1] = Integer.valueOf(_state.getGame().p1_getReturns()).toString();
				data[2] = Long.valueOf(new Date().getTime()).toString();
				data[3] = "Normal";
				
				Gson gson = new Gson();
				String json = gson.toJson(data);
				System.out.println(json);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("data", json));
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(Values.HOMEPAGE_URI + "MuscleRecovery_DataReceiver.jsp");
					httpPost.setEntity(new UrlEncodedFormEntity(params));
					HttpResponse httpResponse = httpClient.execute(httpPost);
					System.out.println(httpResponse.getStatusLine().getStatusCode());
					Toast.makeText(ctx, json, MAX_PRIORITY).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
	
			}
		};
		networkThread.start();
	}
	
	/**
	 * Sends the expert game mode highscore data to the server.
	 */
	private void sendExpertModeData() {
		Thread networkThread = new Thread() {
			public void run() {

				String[] data = new String[4];
				TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
				data[0] = telephonyManager.getDeviceId();
				data[1] = Long.valueOf(_state.getGame().getGameTime()).toString();
				data[2] = Long.valueOf(new Date().getTime()).toString();
				data[3] = "Expert";
				
				Gson gson = new Gson();
				String json = gson.toJson(data);
				System.out.println(json);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("data", json));
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(Values.HOMEPAGE_URI + "MuscleRecovery_DataReceiver.jsp");
					httpPost.setEntity(new UrlEncodedFormEntity(params));
					HttpResponse httpResponse = httpClient.execute(httpPost);
				} catch (Exception e) {
					e.printStackTrace();
				}
	
			}
		};
		networkThread.start();
	}
	
}