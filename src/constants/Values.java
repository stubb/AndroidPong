package constants;

import android.net.Uri;

public interface Values {
	
	public static final String PREFERENCES = "pongSettings";
	
	public static final Uri HOMEPAGE_URI = Uri.parse("http://www.htw-berlin.de/");
	
	// constants for settings
	public static final String USER_NAME = "username";
	public static final String USER_PW = "userPW";
	public static final String SSID = "ssid";
	public static final String WIFI_PW = "wifiPW";
	public static final String[] SETTINGS_DATA = {USER_NAME, USER_PW, SSID,
		WIFI_PW};
	public static final String PREF_READING_ERROR = "error while reading preference";

}
