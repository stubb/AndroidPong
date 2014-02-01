package singleton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import constants.Values;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

/**
 * This singleton serves for convenience. It manages the actual activity and
 * allows access to it's functionality from everywhere in the application.
 * Also it can switch the blue tooth connection on and off.
 */
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
	
	public boolean isHotSpotRunning() {
		return this.hotSpotRunning;
	}
	
//	public void switchHotSpotState() {
//		if(hotSpotRunning) {
//			this.hotSpotRunning = false;
//			this.disableHotspot();
//		}
//		else {
//			this.hotSpotRunning = true;
//			this.enableHotspot();
//		}
//	}
	
	/**
	 * set the parameters for the blue tooth hotspot and enable it
	 */
	public void enableHotspot() {
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
		      this.hotSpotRunning = true;
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
	
	/**
	 * disables the calibrated blue tooth hotspot
	 */
	public void disableHotspot() {
	    try
	    {
	    	WifiManager wifi = (WifiManager) this.currentActivity.getSystemService(Context.WIFI_SERVICE);
	        Method[] wmMethods = wifi.getClass().getDeclaredMethods();

	        for (Method method : wmMethods) {
	            if (method.getName().equals("setWifiApEnabled")) {
	                try {
	                    method.invoke(wifi, null, false);
	                    this.hotSpotRunning = false;
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
