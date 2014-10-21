package com.zhang.picturedownloader;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

	private Button btChooseUrls;
	private Button btDownload;
	private Button btViewPictures;
	private ProgressBar pbDownloading;
	
	private int progressStatus = 0;
	private ArrayList<String> urlList = new ArrayList<String>();

	private static final int CHOOSE_URLS_RESULT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btChooseUrls = (Button) findViewById(R.id.btChooseUrls);
		btDownload = (Button) findViewById(R.id.btDownload);
		btViewPictures = (Button) findViewById(R.id.btViewPictures);
		pbDownloading = (ProgressBar) findViewById(R.id.pbDownloading);

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
			urlList.clear();
			for (Map.Entry<String, ?> url : urls.entrySet()) {
				String urlValue = url.getValue().toString();
				Log.d("url values", url.getKey() + ": "
						+ urlValue);
				if ( urlValue.startsWith("http") && urlValue.length() > 10)
				{
					urlList.add(urlValue);
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
	
	public void btDownload_onClick(View v)
	{
		DownloadTask dt = new DownloadTask();
		dt.execute();
	}
	
	private class DownloadTask extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			btChooseUrls.setEnabled(false);
			btViewPictures.setEnabled(false);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			Log.d("Progress Update", values[0]);
			pbDownloading.setProgress(Integer.valueOf(values[0]));
			//tvProgress.setText(values[0]);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			btChooseUrls.setEnabled(true);
			btViewPictures.setEnabled(true);
		}
		@Override
		protected Void doInBackground(Void... result) {
			// TODO Auto-generated method stub

			for (int i = 0; i < urlList.size(); i++ )
			{		
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				publishProgress(String.valueOf(i + 1));
			}
			return null;
		}
		
	}
}
