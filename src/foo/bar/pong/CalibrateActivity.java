package foo.bar.pong;

import singleton.Connector;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CalibrateActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calibration_layout);
		
		TextView min = (TextView) findViewById(R.id.calibrateMinimum);
		TextView max = (TextView) findViewById(R.id.calibrateMaximum);

		min.setText(String.valueOf(Connector.getInstance().getMin()));
		max.setText(String.valueOf(Connector.getInstance().getMax()));
	}

}
