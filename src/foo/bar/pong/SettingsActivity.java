package foo.bar.pong;

import singleton.Connector;
import constants.Values;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnCheckedChangeListener{
	
	public static final int REQUEST_CALIBRATION = 0;
	
	private int min;
	private int max;
	private TextView minTV;
	private TextView maxTV;
	private EditText[] data;
	private CheckBox checkBox;
	private SharedPreferences settings;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		this.settings = getSharedPreferences(Values.CONFIG, MODE_PRIVATE);
		this.getReferences();
		this.checkBox.setOnCheckedChangeListener(this);
		this.load();
	}
	
	private void getReferences() {
		this.data = new EditText[Values.SETTINGS_DATA.length];
		this.data[0] = (EditText) findViewById(R.id.userNameEditText);
		this.data[1] = (EditText) findViewById(R.id.userPWEditText);
		this.data[2] = (EditText) findViewById(R.id.confirmPWEditText);
		this.data[3] = (EditText) findViewById(R.id.ssidEditText);
		this.data[4] = (EditText) findViewById(R.id.wifiPWEditText);
		this.checkBox = (CheckBox) findViewById(R.id.showPWCheckBox);
		
		this.minTV = (TextView) findViewById(R.id.minimumTV);
		this.maxTV = (TextView) findViewById(R.id.maximumTV);
		
		this.minTV.setText(String.valueOf(this.settings.getInt(Values.CALIBRATED_MIN_VAL,
				0)));
		this.maxTV.setText(String.valueOf(this.settings.getInt(Values.CALIBRATED_MAX_VAL,
				0)));
	}
	
	public void save(View view) {
		if(this.data[Values.POS_USER_PW].getText().toString().equals(
				this.data[Values.POS_CONFIRM_PW].getText().toString())) {
			Editor editor = this.settings.edit();
			for(int i=0; i<this.data.length;i++) {
				editor.putString(Values.SETTINGS_DATA[i],
						this.data[i].getText().toString());
			}
			editor.commit();
			Toast.makeText(this, Values.onSettingsResultSave,
					Toast.LENGTH_SHORT).show();
			this.finish();
		}
		else {
			Toast.makeText(this, Values.ERROR_CONFIRM_PW, Toast.LENGTH_LONG).show();
		}
	}
	
	public void restore(View view) {
		this.load();
		Toast.makeText(this, Values.onSettingsResultRestore,
				Toast.LENGTH_SHORT).show();
		this.finish();
	}
	
	public void calibrate(View view) {
		if(Connector.getInstance().hasConnection()) {
			Intent intent = new Intent(this, CalibrationActivity.class);
			startActivity(intent);
		}
		else {
			Toast.makeText(this, Values.ERROR_NO_CONNECTION, Toast.LENGTH_LONG).show();
		}
	}

	private void load() {
		for(int i=0; i<this.data.length;i++) {
			this.data[i].setText(this.settings.getString(Values.SETTINGS_DATA[i],
					""));
		}
		if(this.settings.getString(Values.SETTINGS_DATA[0],"").equals("")) {
			Button btn = (Button) findViewById(R.id.BtnRestoreSettings);
			btn.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		System.out.println("Debug: "+buttonView.getId()+", "+
				isChecked);
		if(isChecked) {
			this.data[Values.POS_WIFI_PW].setInputType(
					InputType.TYPE_CLASS_TEXT |
					InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}
		else {
			this.data[Values.POS_WIFI_PW].setInputType(
					InputType.TYPE_CLASS_TEXT | 
					InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		this.data[Values.POS_WIFI_PW].invalidate();
	}
	
	public synchronized void runCalibration(View view) {
		Intent intent = new Intent(this, CalibrationActivity.class);
        this.startActivityForResult(intent,REQUEST_CALIBRATION);
	}
	
	private synchronized void startCalibration(boolean max) {
		Integer[] results = new Integer[10];
		for(int i=0; i<10; i++) {
			results[i] = calibrate(max);
			System.out.println("Debug: "+results[i]);
			try {
				wait(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(max) {
			this.max = this.getAverage(results);
			this.maxTV.setText(String.valueOf(this.max));
		}
		else {
			this.min = this.getAverage(results);
			this.minTV.setText(String.valueOf(this.min));
		}
	}
	
	private int calibrate(boolean max) {
		if(max) {
			return Connector.getInstance().getMax();
		}
		else {
			return Connector.getInstance().getMin();
		}
	}
	
	private int getAverage(Integer[] values) {
		int sum = 0;
		for(int i=0; i<values.length; i++) {
			sum += values[i];
		}
		return (Math.round(sum/values.length));
	}
	
	private void writeCalibratedValues() {
		Editor editor = this.settings.edit();
		editor.putInt(Values.CALIBRATED_MIN_VAL, this.min);
		editor.putInt(Values.CALIBRATED_MAX_VAL, this.max);
		editor.commit();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CALIBRATION) {
			if(resultCode == RESULT_OK) {
				int[] minMax = data.getIntArrayExtra(Values.MIN_MAX_RESULT);
			}
		}
	}
	
}
