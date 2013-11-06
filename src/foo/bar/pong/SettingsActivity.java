package foo.bar.pong;

import constants.Values;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends Activity {
	
	private EditText[] data;
	private SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.getEditTexts();
		this.settings = getPreferences(MODE_PRIVATE);
		//this.load();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
	}
	
	private void getEditTexts() {
		this.data = new EditText[Values.SETTINGS_DATA.length];
		this.data[0] = (EditText) this.findViewById(R.id.userNameEditText);
		this.data[1] = (EditText) this.findViewById(R.id.userPWEditText);
		this.data[2] = (EditText) this.findViewById(R.id.ssidEditText);
		this.data[3] = (EditText) this.findViewById(R.id.wifiPWEditText);
	}
	
	public void save(View view) {
		Editor editor = this.settings.edit();
		for(int i=0; i<this.data.length;i++) {
			editor.putString(Values.SETTINGS_DATA[i],
					this.data[i].getText().toString());
		}
		editor.commit();
	}
	
	public void restore(View view) {
		//this.load();
	}

	private void load() {
		for(int i=0; i<this.data.length;i++) {
			data[i].setText(this.settings.getString(Values.SETTINGS_DATA[i],
					Values.PREF_READING_ERROR));
		}
	}
	
}
