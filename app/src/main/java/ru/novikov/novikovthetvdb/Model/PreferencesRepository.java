package ru.novikov.novikovthetvdb.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class PreferencesRepository {

    private static final String PREFERENCES_NAME = "NovikovTvDbPref";
    private static final String KEY_TVDB_TOKEN = "TvDbToken";
    private static final String TAG = "PreferencesRepository";

    private SharedPreferences sPref;

    private Context context;

    public PreferencesRepository(Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPreferences(){
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void saveTvDbToken(String token){
        if (token != null)
            Log.i(TAG, "save in shared pref new token");
        SharedPreferences sPref = getSharedPreferences();
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(KEY_TVDB_TOKEN, token);
        ed.apply();
    }

    public String getKeyTvdbToken(){
        SharedPreferences sPref = getSharedPreferences();
        return sPref.getString(KEY_TVDB_TOKEN, null);
    }

}
