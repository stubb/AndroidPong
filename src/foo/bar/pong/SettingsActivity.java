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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnCheckedChangeListener{
	
	private EditText[] data;
	private CheckBox checkBox;
	private SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		
		this.getReferences();
		this.checkBox.setOnCheckedChangeListener(this);
		this.settings = getSharedPreferences(Values.CONFIG, MODE_PRIVATE);
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
			Intent intent = new Intent(this, CalibrateActivity.class);
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
	
}
