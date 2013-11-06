package foo.bar.pong;

import android.net.Uri;
import android.os.Bundle;
import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;

public class MainActivity extends Activity implements OnMenuItemClickListener {

	public static final String URL = "http://www.htw-berlin.de/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
	}
	
	public void startGame(View view) {
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	public void hostGame(View view) {
	}

	public void joinGame(View view) {
	}

	public void showStats(View view) {
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		for(int i=0; i<menu.size(); i++) {
			menu.getItem(i).setOnMenuItemClickListener(this);
		}
		return true;
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Intent intent = null;
		if(item.getItemId() == R.id.settings) {
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}
		else if(item.getItemId() == R.id.homepage) {
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
			startActivity(intent);
		}
		return false;
	}

}
