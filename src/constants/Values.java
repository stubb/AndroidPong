package constants;

import android.net.Uri;

public interface Values {
	
	public static final String firstStart = "first";
	
	// uri to homepage
	public static final Uri HOMEPAGE_URI = Uri.parse("http://www.google.com/");
	
	// constants for settings
	public static final String USER_NAME = "username";
	public static final String USER_PW = "userPW";
	public static final String SSID = "ssid";
	public static final String WIFI_PW = "wifiPW";
	public static final String[] SETTINGS_DATA = {USER_NAME, USER_PW, SSID,
		WIFI_PW};
	public static final int POS_WIFI_PW = 3;
	public static final String onSettingsResultSave = "Settings successfully"
			+ " saved.";
	public static final String onSettingsResultRestore = "Settings successfully"
			+ " restored.";
	
}
