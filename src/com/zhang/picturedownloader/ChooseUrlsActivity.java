package com.zhang.picturedownloader;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ChooseUrlsActivity extends PreferenceActivity 
{
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
            super.onCreate(savedInstanceState);
             // add the xml resource                     
            addPreferencesFromResource(R.xml.urls_preference);
            

    }


} 