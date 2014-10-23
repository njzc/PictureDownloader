package com.zhang.picturedownloader;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class PicturesListActivity extends Activity {

	ArrayList<Picture> pictureList;
	ListView lvPictures;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pictures_list);

	    Intent intent = getIntent();
	    pictureList = (ArrayList<Picture>)intent.getSerializableExtra("pictureList");
	    if ( pictureList != null )
	    {
		    PictureAdapter adapter = new PictureAdapter(this, pictureList);
		    lvPictures = (ListView) findViewById(R.id.lvPictures);
		    
		    lvPictures.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					Intent pictureViewIntent = new Intent(PicturesListActivity.this, PictureViewActivity.class);
					pictureViewIntent.putExtra("picture", pictureList.get(position));
					startActivity(pictureViewIntent);
				}
				}
			);
			    
		    lvPictures.setAdapter(adapter);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pictures_list, menu);
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
