package com.dai.temperatureconverter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharedPreference {
    private SharedPreferences prefs;
    private Editor editor;

    public SharedPreference(Activity activity){
        super();
        prefs = activity.getSharedPreferences(activity.getString(R.string.prefsFileKey), Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public void save(String key, String text){
        editor.putString(key,text);
        editor.apply();
    }
    public String getValue(String key){
        String text = prefs.getString(key,"");
        return text;
    }
}

