package foo.bar.pong;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import constants.Values;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements OnMenuItemClickListener {
	
	private SharedPreferences settings;
	String temp_text = "";
	
	Thread netz_thread = new Thread() {
			public void run() {
				try {
					
					while(true){
						// networkstuff
						int server_port = 4321;
						byte[] message = new byte[1500];
						DatagramPacket p = new DatagramPacket(message, message.length);
						DatagramSocket s = new DatagramSocket(server_port);
						if(isInterrupted()) {
							s.close();
							this.join();
						}
						if(!s.isClosed()){
							s.receive(p);
						}
						temp_text = new String(message, 0, p.getLength());
						if(!s.isClosed()){
							s.close();
						}					
					}
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		this.settings = getSharedPreferences(Values.CONFIG, MODE_PRIVATE);
		this.checkForSettings();
		this.enableHotspot();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("Destroy hotspot");
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
