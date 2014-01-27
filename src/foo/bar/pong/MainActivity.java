package foo.bar.pong;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import singleton.Connector;
import constants.Values;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
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
		this.settings = getSharedPreferences(Values.CONFIG, MODE_PRIVATE);
		this.checkForSettings();
		this.enableHotspot();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Connector.getInstance().startConnection();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(LOGTAG, "Destroy network thread");
		Connector.getInstance().killConnection();
		Log.i(LOGTAG, "Destroy hotspot");
		this.stopHotspot();
	}
	
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
		editor.putString(Values.GAME_MODE, "normal");
		editor.commit();
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	public void startExpertModeGame(View view) {
		Editor editor = this.settings.edit();
		editor.putString(Values.GAME_MODE, "expert");
		editor.commit();
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}

	public void startTrainingsModeGame(View view) {
		Editor editor = this.settings.edit();
		editor.putString(Values.GAME_MODE, "training");
		editor.commit();
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}

	public void showStats(View view) {
// TODO: connect zeug wieder einkommentieren und anpassen mit uebergabe
//		final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
//		if (activeNetwork != null && activeNetwork.isConnected()) {
//		    //notify user you are online
			Intent intent = new Intent(this, HighscoreActivity.class);//StatisticsActivity.class);
			startActivity(intent);
//		} else {
//		    //notify user you are not online
//			Toast.makeText(this, "Keine Internevtverbindung\n Daten nicht abrufbar!", Toast.LENGTH_LONG).show();
//		} 
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
	
	private void enableHotspot() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		Method[] wmMethods = wifi.getClass().getDeclaredMethods();
		wifi.setWifiEnabled(false);
		for(Method method: wmMethods){
		  if(method.getName().equals("setWifiApEnabled")){
		    WifiConfiguration netConfig = new WifiConfiguration();
		    SharedPreferences settings = this.getSharedPreferences(Values.CONFIG, Context.MODE_PRIVATE);
		    netConfig.SSID = settings.getString(Values.SSID, "MuscleRecovery");
		    netConfig.preSharedKey = settings.getString(Values.PKEY, "MuscleRecovery");
		    netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		    netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		    netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		    netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		    netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		    netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);  

		    try {
		      method.invoke(wifi, netConfig,true);
		    } catch (IllegalArgumentException e) {
		      e.printStackTrace();
		    } catch (IllegalAccessException e) {
		      e.printStackTrace();
		    } catch (InvocationTargetException e) {
		      e.printStackTrace();
		    }
		  }
		}
	}
	
	private void stopHotspot() {
	    try
	    {
	    	WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	        Method[] wmMethods = wifi.getClass().getDeclaredMethods();

	        for (Method method : wmMethods) {
	            if (method.getName().equals("setWifiApEnabled")) {
	                try {
	                    method.invoke(wifi, null, false);
	                } catch (IllegalArgumentException e) {
	                    e.printStackTrace();
	                } catch (IllegalAccessException e) {
	                    e.printStackTrace();
	                } catch (InvocationTargetException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
}
