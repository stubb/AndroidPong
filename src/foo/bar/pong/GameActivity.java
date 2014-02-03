package foo.bar.pong;

import singleton.UtilitySingleton;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * The activity where the complete pong game lives.
 */
public class GameActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.court_layout);
		
		UtilitySingleton.getInstance().setCurrentActivity(this);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

}
