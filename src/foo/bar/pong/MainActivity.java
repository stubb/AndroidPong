package foo.bar.pong;

import singleton.Connector;
import singleton.UtilitySingleton;
import constants.Values;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.Toast;
import android.net.NetworkInfo;

/** 
 * This activity is used for navigation within the app.
 * It's the default acitvity starts used on app start, except on
 * the inital app start. In this case the settingsactivity is started.
 * The naviagtion contains the different game modes, the highscores, settings
 * and visiting the website.
 */
public class MainActivity extends Activity implements OnMenuItemClickListener {
	
	/**
	 * The tag is used to identify the class while logging
	 */
	private final String LOGTAG = getClass().getName();
	
	private SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);	
		
		UtilitySingleton.getInstance().setCurrentActivity(this);
		
		this.settings = getSharedPreferences(Values.CONFIG, MODE_PRIVATE);
		this.checkForSettings();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(!UtilitySingleton.getInstance().isHotSpotRunning()) {
			UtilitySingleton.getInstance().enableHotspot();
		}
		Connector.getInstance().startConnection();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(LOGTAG, "Destroy network thread");
		Connector.getInstance().killConnection();
		Log.i(LOGTAG, "Destroy hotspot");
		UtilitySingleton.getInstance().disableHotspot();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Connector.getInstance().startConnection();
	}
	
	
	/**
	 * Check if the settings are already set or not. If not start the settingsactivity.
	 */
	private void checkForSettings()	{
		if(this.settings.getBoolean(Values.FIRST_START, true)) {
			Intent intent = new Intent(this, SettingsActivity.class);
			Editor editor = this.settings.edit();
			editor.putBoolean(Values.FIRST_START, false);
			editor.putString(Values.WIFI_SSID, Values.SSID);
			editor.putString(Values.WIFI_PW, Values.PKEY);
			editor.commit();
			Toast.makeText(this, Values.INITIAL_MESSAGE, Toast.LENGTH_LONG).show();
			startActivity(intent);
		}
	}
	
	public void startNormalModeGame(View view) {
		Editor editor = this.settings.edit();
		editor.putString(Values.GAME_MODE, Values.GAME_MODE_NORMAL);
		editor.commit();
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	public void startExpertModeGame(View view) {
		Editor editor = this.settings.edit();
		editor.putString(Values.GAME_MODE, Values.GAME_MODE_EXPERT);
		editor.commit();
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}

	public void startTrainingsModeGame(View view) {
		Editor editor = this.settings.edit();
		editor.putString(Values.GAME_MODE, Values.GAME_MODE_TRAINING);
		editor.commit();
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}

	public void showStats(View view) {
		final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
		    //notify user you are online
			Intent intent = new Intent(this, HighscoreActivity.class);
			startActivity(intent);
		} else {
		    //notify user you are not online
			Toast.makeText(this, "Keine Internevtverbindung\n Daten nicht abrufbar!", Toast.LENGTH_LONG).show();
		} 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		for(int i=0; i<menu.size(); i++) {
			menu.getItem(i).setOnMenuItemClickListener(this);
		}
		return true;
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Intent intent = null;
		if(item.getItemId() == R.id.settings) {
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}
		else if(item.getItemId() == R.id.homepage) {
			intent = new Intent(Intent.ACTION_VIEW, Values.HOMEPAGE_URI);
			startActivity(intent);
		}
		return false;
	}
	
}
