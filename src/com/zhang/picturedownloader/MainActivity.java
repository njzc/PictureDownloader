package com.zhang.picturedownloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btChooseUrls;
	private Button btDownload;
	private Button btViewPictures;
	private ProgressBar pbDownloading;

	private DownloadTask dt;

	private ArrayList<String> urlList = new ArrayList<String>();
	private int successCount = 0;
	private boolean isDownloading = false;
	private ArrayList<Picture> pictureList;

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
				Log.d("url values", url.getKey() + ": " + urlValue);
				if (urlValue.startsWith("http") && urlValue.length() > 10) {
					urlList.add(urlValue);
					validUrlCount++;
				}
			}

			Log.d("url values", "Valid url count: " + validUrlCount);
			if (validUrlCount == 6) {
				btDownload.setEnabled(true);
			} else {
				btDownload.setEnabled(false);
			}
		}
	}

	public void btChooseUrls_onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this, ChooseUrlsActivity.class);
		startActivityForResult(intent, CHOOSE_URLS_RESULT);
	}

	public void btDownload_onClick(View v) {
		if (isDownloading) {
			dt.cancel(true);
		} else {
			dt = new DownloadTask();
			dt.execute();
		}
	}
	
	public void btViewPictures_onClick(View v)
	{
		Intent intent = new Intent(MainActivity.this, PicturesListActivity.class);
		intent.putExtra("pictureList", pictureList);
		startActivity(intent);
	}

	private void showDialog(String title, String message) {
		// show dialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder.setMessage(message).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private class DownloadTask extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			btDownload.setText(getResources().getString(R.string.cancel_downloads));
			btChooseUrls.setEnabled(false);
			btViewPictures.setEnabled(false);
			isDownloading = true;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			Log.d("Progress Update", values[0]);
			String fileName = values[1];
			String urlString = values[2];
			pbDownloading.setProgress(Integer.valueOf(values[0]));
			
			if ( fileName == "" ) // download file failed
			{
				Toast.makeText(getApplicationContext(),
						"Download failed: " + urlString,
						Toast.LENGTH_SHORT).show();
			}
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			btChooseUrls.setEnabled(true);
			btViewPictures.setEnabled(true);
			
			btDownload.setText(getResources().getString(R.string.download));
			showDialog(getResources().getText(R.string.dialog_complete_title)
					.toString(), "Downloaded " + successCount + " of "
					+ urlList.size());
			isDownloading = false;
		}
		
		@Override
		protected void onCancelled()
		{
			btChooseUrls.setEnabled(true);
			btViewPictures.setEnabled(true);

			btDownload.setText(getResources().getString(R.string.download));
			showDialog(getResources().getText(R.string.dialog_cancel_title)
					.toString(), "Downloaded " + successCount + " of "
					+ urlList.size());
			isDownloading = false;
		}

		@Override
		protected Void doInBackground(Void... result) {
			// TODO Auto-generated method stub

			successCount = 0;
			pictureList = new ArrayList<Picture>();
			
			for (int i = 0; i < urlList.size(); i++) {
				if (isCancelled()) {
					break;
				}
				try {
					final String urlString = urlList.get(i);
					URL url = new URL(urlString);
					
					String fileName = downloadPicture(url);
					if ( fileName != "") {
						pictureList.add(new Picture(urlString, fileName));
						successCount++;
					}
					publishProgress(String.valueOf(i + 1), fileName, urlString);
					Thread.sleep(100);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}

		
		
		private String downloadPicture(URL url) {
			InputStream input = null;
			FileOutputStream output = null;
			HttpURLConnection connection = null;
			try {
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					Log.d("download", "response code error: " + url.toString());
					return "";
				}

				String outputFileName = url.getFile();
				outputFileName = outputFileName.substring(outputFileName.lastIndexOf("/") + 1);
				int fileSize = connection.getContentLength();
				int total = 0;
				// download the file
				input = connection.getInputStream();
				output = openFileOutput(outputFileName, Context.MODE_PRIVATE);

				byte data[] = new byte[4096];
				int count;
				while ((count = input.read(data)) != -1) {
					// allow canceling
					if (isCancelled()) {
						input.close();
						break;
					}
					total += count;
					output.write(data, 0, count);
				}
				if ( total == fileSize )
				{
					Log.d("download", "download succeed: " + url.toString());
					return outputFileName;
				}
				else
				{
					Log.d("download", "download failed: " + url.toString());
					return "";
				}

			} catch (Exception e) {
				Log.d("download", "download error: " + e.toString());
				return "";
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ioe) {
				}

				if (connection != null)
					connection.disconnect();
			}

		}
	}
}
