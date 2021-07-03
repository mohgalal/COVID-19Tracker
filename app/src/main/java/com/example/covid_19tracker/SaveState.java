package com.example.covid_19tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveState {

    public static final String DARK_MODE="dark_mode";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    Context context;

    public void setDarkModeState(boolean b){
        editor.putBoolean(DARK_MODE,b);
        editor.apply();
    }

    public boolean getDarkModeState(){
        return sharedPreferences.getBoolean(DARK_MODE,false);
    }

    public SaveState(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("preferences",Context.MODE_PRIVATE);
    }
}
