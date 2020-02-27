package com.sktt1.butters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sktt1.butters.data.preference.SharedPreferenceHelper;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME = 1000;
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPreferenceHelper = new SharedPreferenceHelper(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if (!sharedPreferenceHelper.isInitialLaunch()) {
                    i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    sharedPreferenceHelper.setInitialLaunch(false);
                    i = new Intent(getApplicationContext(), SliderActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME);
    }
}
