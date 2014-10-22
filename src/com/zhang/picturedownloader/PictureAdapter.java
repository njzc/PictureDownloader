package com.zhang.picturedownloader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

	private final Context context;

	public PictureAdapter(Context context, ArrayList<Picture> list) {
		super(context, R.layout.pictures_list_row, list);
		this.context = context;
		this.pictureList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.pictures_list_row, parent,
				false);
		
		Picture picture = pictureList.get(position);
		
		TextView tvUrl = (TextView) rowView.findViewById(R.id.tvUrl);
		tvUrl.setText(picture.getUrl());
		
		TextView tvFileName = (TextView) rowView.findViewById(R.id.tvFileName);
		tvFileName.setText(picture.getFileName());
		
		ImageView ivThumbnail = (ImageView) rowView.findViewById(R.id.ivThumbnail);
		FileInputStream input;
		
		try {
			input = context.openFileInput(picture.getFileName());
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			ivThumbnail.setImageBitmap(bitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rowView;
	}
}
