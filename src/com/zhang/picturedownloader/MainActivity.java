package com.zhang.picturedownloader;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button btChooseUrls;
	private Button btDownload;
	private Button btViewPictures;

	private static final int CHOOSE_URLS_RESULT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btChooseUrls = (Button) findViewById(R.id.btChooseUrls);
		btDownload = (Button) findViewById(R.id.btDownload);
		btViewPictures = (Button) findViewById(R.id.btViewPictures);

		btChooseUrls.setEnabled(true);
		btDownload.setEnabled(false);
		btViewPictures.setEnabled(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == CHOOSE_URLS_RESULT) {
			SharedPreferences sharedPrefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			Map<String, ?> urls = sharedPrefs.getAll();
			int validUrlCount = 0;
			for (Map.Entry<String, ?> url : urls.entrySet()) {
				String urlValue = url.getValue().toString();
				Log.d("url values", url.getKey() + ": "
						+ urlValue);
				if ( urlValue.startsWith("http") && urlValue.length() > 10)
				{
					validUrlCount++;
				}
			}
			
			Log.d("url values", "Valid url count: " + validUrlCount);
			if ( validUrlCount == 6)
			{
				btDownload.setEnabled(true);
				btViewPictures.setEnabled(true);
			}
			else
			{
				btDownload.setEnabled(false);
				btViewPictures.setEnabled(false);
			}
		}
	}

	public void btChooseUrls_onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this, ChooseUrlsActivity.class);
		startActivityForResult(intent, CHOOSE_URLS_RESULT);
	}
}
