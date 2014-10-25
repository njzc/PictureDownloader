package com.zhang.picturedownloader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class PictureViewActivity extends Activity {
	
	private Picture picture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_view);
		
		Intent intent = getIntent();
	    picture= (Picture)intent.getSerializableExtra("picture");
	    if ( picture != null )
	    {
	    	TextView tvUrl = (TextView) findViewById(R.id.tvViewUrl);
			tvUrl.setText(picture.getUrl());
			
			TextView tvFileName = (TextView) findViewById(R.id.tvViewFileName);
			tvFileName.setText(picture.getFileName());
			
			ImageView ivPicture = (ImageView) findViewById(R.id.ivPicture);
			FileInputStream input;
			
			try {
				input = openFileInput(picture.getFileName());
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				input.close();
				ivPicture.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.picture_view, menu);
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
}
