package com.zhang.picturedownloader;

import java.io.Serializable;

public class Picture implements Serializable{

	private String url;
	private String fileName;
	
	public Picture(String url, String fileName)
	{
		this.url = url;
		this.fileName = fileName;
	}
	
	public String getUrl()
	{
		return this.url;
	}

	public String getFileName()
	{
		return this.fileName;
	}

}
