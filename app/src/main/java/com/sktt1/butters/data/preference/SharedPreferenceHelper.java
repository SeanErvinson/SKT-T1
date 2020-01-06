package com.sktt1.butters.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String INIT_LAUNCH = "initLaunch";
    private static final String PREFERENCE = "skt_pref";

    public SharedPreferenceHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setInitialLaunch(boolean status) {
        editor.putBoolean(INIT_LAUNCH, status);
        editor.commit();
    }

    public boolean isInitialLaunch() {
        return sharedPreferences.getBoolean(INIT_LAUNCH, true);
    }
}
