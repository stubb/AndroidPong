package foo.bar.pong;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import singleton.Connector;
import constants.Values;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
		this.data[0] = (EditText) findViewById(R.id.nameEditText);
		this.data[1] = (EditText) findViewById(R.id.surnameEditText);
		this.data[2] = (EditText) findViewById(R.id.emailEditText);
		this.data[3] = (EditText) findViewById(R.id.userNameEditText);
		this.data[4] = (EditText) findViewById(R.id.userPWEditText);
		this.data[5] = (EditText) findViewById(R.id.confirmPWEditText);
		this.data[6] = (EditText) findViewById(R.id.ssidEditText);
		this.data[7] = (EditText) findViewById(R.id.wifiPWEditText);
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
			registerUser();
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
        if(!Connector.getInstance().hasConnection()) {
                Connector.getInstance().startConnection();
        }
        Intent intent = new Intent(this, CalibrationActivity.class);
        this.startActivityForResult(intent,REQUEST_CALIBRATION);
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
	
	private void registerUser() {
		String[] data = {};
		data[0] = this.settings.getString("EMAIL", "");
		data[1] = this.settings.getString("NAME", "");
		data[2] = this.settings.getString("SURNAME", "");
		data[3] = this.settings.getString("USER_NAME", "");
		data[6] = this.settings.getString("USER_PW", "");
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		data[5] = telephonyManager.getDeviceId();
		Gson gson = new Gson();
		String json = gson.toJson(data);
		System.out.println(json);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("array", json));
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("");
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse httpResponse = httpClient.execute(httpPost);
		}
        catch(Exception e)  
        {  
            e.printStackTrace();
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
	
	public void runCalibration(View view) {
		Intent intent = new Intent(this, CalibrationActivity.class);
        this.startActivityForResult(intent,REQUEST_CALIBRATION);
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
