package com.zhang.picturedownloader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PictureAdapter extends ArrayAdapter<Picture> {
	private ArrayList<Picture> pictureList;

	private final Activity context;
	
	private static class ViewHolder {
	    public TextView tvUrl;
	    public TextView tvFileName;
	    public ImageView ivThumbnail;
	  }

	public PictureAdapter(Activity context, ArrayList<Picture> list) {
		super(context, R.layout.pictures_list_row, list);
		this.context = context;
		this.pictureList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if ( rowView == null ){
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.pictures_list_row,null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.tvUrl = (TextView) rowView.findViewById(R.id.tvUrl);
			viewHolder.tvFileName = (TextView) rowView.findViewById(R.id.tvFileName);
			viewHolder.ivThumbnail = (ImageView) rowView.findViewById(R.id.ivThumbnail);
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder)rowView.getTag();
		Picture picture = pictureList.get(position);
		
		holder.tvUrl.setText(picture.getUrl());
		holder.tvFileName.setText(picture.getFileName());
		
		FileInputStream input;
		
		try {
			input = context.openFileInput(picture.getFileName());
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			input.close();
			holder.ivThumbnail.setImageBitmap(bitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rowView;
	}
}
