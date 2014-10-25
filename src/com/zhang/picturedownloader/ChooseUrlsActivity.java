package com.zhang.picturedownloader;

import java.util.prefs.Preferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;

public class ChooseUrlsActivity extends PreferenceActivity implements
OnSharedPreferenceChangeListener 
{
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
            super.onCreate(savedInstanceState);
             // add the xml resource                     
            addPreferencesFromResource(R.xml.urls_preference);
            PreferenceManager.setDefaultValues(ChooseUrlsActivity.this, R.xml.urls_preference,
                    false);

            initSummary(getPreferenceScreen());
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }
    
    private void updatePrefSummary(Preference p) {
        
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
        	String text = editTextPref.getText();
        	if ( text != null && text != "")
        	{
                p.setSummary(text);  
        	}
            
        }
    }
    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        updatePrefSummary(findPreference(key));
    }

} 