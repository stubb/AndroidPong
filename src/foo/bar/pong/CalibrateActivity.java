package foo.bar.pong;

import constants.Values;
import singleton.Connector;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.TextView;

public class CalibrateActivity extends Activity {
	
	private SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calibration_layout);
		
		this.settings = getSharedPreferences(Values.CONFIG, MODE_PRIVATE);
		
	//	TextView min = (TextView) findViewById(R.id.calibrateMinimum);
	//	TextView max = (TextView) findViewById(R.id.calibrateMaximum);

	/*	System.out.println("Debug: "+Connector.getInstance().getMin());
		System.out.println("Debug: "+Connector.getInstance().getMax());
		
		min.setText(String.valueOf(Connector.getInstance().getMin()));
		max.setText(String.valueOf(Connector.getInstance().getMax()));
		*/
		this.writeCalibratedValues();
	}
	
	private void writeCalibratedValues() {
		Editor editor = this.settings.edit();
	//	editor.putInt(Values.CALIBRATED_MIN_VAL, Connector.getInstance().getMin());
	//	editor.putInt(Values.CALIBRATED_MAX_VAL, Connector.getInstance().getMax());
		editor.commit();
	}

}
