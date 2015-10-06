package com.dubmania.vidcraft.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.misc.AddLanguageActivity;

public class SplashActivity extends AppCompatActivity {

    private static long SPLASH_TIME = 2000;

    SharedPreferences mPref;
    SharedPreferences.Editor editor;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!checkLanguage()) {
                    Intent intent = new Intent(SplashActivity.this, AddLanguageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_TIME);

    }

    private boolean checkLanguage(){
        boolean language=false;
        SharedPreferences token_prefs =getApplicationContext().getSharedPreferences("token_pref", Context.MODE_PRIVATE);
        if (token_prefs != null) {
            language = token_prefs.getBoolean("add_language", false);
        }

        return language;
    }
}


