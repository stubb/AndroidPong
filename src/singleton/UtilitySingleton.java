package singleton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import constants.Values;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class UtilitySingleton {
	
	private static UtilitySingleton instance;
	
	private boolean hotSpotRunning;
	private Activity currentActivity;
	
	private UtilitySingleton() {
		this.hotSpotRunning = false;
	}
	
	public static UtilitySingleton getInstance() {
		if(instance == null) {
			instance = new UtilitySingleton();
		}
		return instance;
	}
		
	public Activity getCurrentActivity() {
		return this.currentActivity;
	}
	
	public void setCurrentActivity(Activity activity) {
		this.currentActivity = activity;
	}
	
	public void switchHotSpotState() {
		if(hotSpotRunning) {
			this.hotSpotRunning = false;
			this.disableHotspot();
		}
		else {
			this.hotSpotRunning = true;
			this.enableHotspot();
		}
	}
	
	private void enableHotspot() {
		WifiManager wifi = (WifiManager) this.currentActivity.getSystemService(Context.WIFI_SERVICE);
		Method[] wmMethods = wifi.getClass().getDeclaredMethods();
		wifi.setWifiEnabled(false);
		for(Method method: wmMethods){
		  if(method.getName().equals("setWifiApEnabled")){
		    WifiConfiguration netConfig = new WifiConfiguration();
		    SharedPreferences settings = this.currentActivity.getSharedPreferences(Values.CONFIG, Context.MODE_PRIVATE);
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
	
	private void disableHotspot() {
	    try
	    {
	    	WifiManager wifi = (WifiManager) this.currentActivity.getSystemService(Context.WIFI_SERVICE);
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
