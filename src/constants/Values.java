package constants;

import android.net.Uri;

public interface Values {
	
	public static final String CONFIG = "config";
	public static final String FIRST_START = "first";
	public static final String INITIAL_MESSAGE = "Initial start. Please fill in"
			+" your information for further connection purposes.";
	
	// ssid & pw
	public static final String SSID = "MuscleRecovery";
	public static final String PKEY = "MuscleRecovery";
	
	public static final int SERVER_PORT = 4321;
	
	// uri to homepage
	public static final Uri HOMEPAGE_URI = Uri.parse("http://www.google.com/");
	
	// constants for settings
	public static final String NAME = "name";
	public static final String SURNAME = "surname";
	public static final String EMAIL = "email";
	public static final String USER_NAME = "username";
	public static final String USER_PW = "userPW";
	public static final String WIFI_SSID = "ssid";
	public static final String WIFI_PW = "wifiPW";
	// USER_PW is in here twice for sheer convenience
	public static final String[] SETTINGS_DATA = {NAME, SURNAME, EMAIL, USER_NAME, USER_PW, USER_PW, WIFI_SSID,
		WIFI_PW};
	public static final int POS_NAME = 0;
	public static final int POS_SURNAME = 1;
	public static final int POS_EMAIL = 2;
	public static final int POS_USER_NAME = 3;
	public static final int POS_USER_PW = 4;
	public static final int POS_CONFIRM_PW = 5;
	public static final int POS_WIFI_SSID = 6;
	public static final int POS_WIFI_PW = 7;
	public static final String onSettingsResultSave = "Settings successfully"
			+ " saved.";
	public static final String onSettingsResultRestore = "Settings successfully"
			+ " restored.";
	
	public static final String ERROR_CONFIRM_PW = "Confirmed password was not"
			+ " identical with the given password.";
	public static final String ERROR_NO_CONNECTION = "No connection to the Arduino Board found.";
	
	public static final String CALIBRATED_MIN_VAL = "calMinVal";
	public static final String CALIBRATED_MAX_VAL = "calMaxVal";
	
	public static final String GAME_MODE = "normal";
	public static final String GAME_MODE_NORMAL = "normal";
	public static final String GAME_MODE_EXPERT = "expert";
	public static final String GAME_MODE_TRAINING = "training";
	
	// result extras
	public static final String MIN_MAX_RESULT = "minMaxResult";
	
}
