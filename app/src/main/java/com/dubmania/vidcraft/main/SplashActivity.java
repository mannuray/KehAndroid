package com.dubmania.vidcraft.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.dubmania.vidcraft.Adapters.LanguageAndCountryDataHandler;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.networkcommunicator.LanguageListDownloader;
import com.dubmania.vidcraft.misc.AddLanguageActivity;
import com.dubmania.vidcraft.utils.AvailableLanguage;

import java.util.ArrayList;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {

    private static long SPLASH_TIME = 20; // origiran time 2000

    SharedPreferences mPref;
    SharedPreferences.Editor editor;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        boolean dataBaseInitialized = sharedPreferences
                .getBoolean("dataBaseInitialized", false);

        //Log.i("realm", " i is " + dataBaseInitialized);
        if(!dataBaseInitialized) {
            new LanguageListDownloader().downloadLanguage(new LanguageListDownloader.LanguageListDownloadCallback() {
                @Override
                public void onLanguageListDownloadSuccess(LanguageAndCountryDataHandler mData) {
                    ArrayList<LanguageAndCountryDataHandler.Language> languages = mData.getLanguagesArray();
                    Realm realm = Realm.getInstance(getApplicationContext());
                    realm.beginTransaction();

                    for(int i = 0; i < languages.size(); i++) {
                        //Log.i("realm", " i is " + i);
                        AvailableLanguage availableLanguage = realm.createObject( AvailableLanguage.class );
                        LanguageAndCountryDataHandler.Language lan = languages.get(i);
                        availableLanguage.setLanguageId(lan.getId());
                        availableLanguage.setLanguage(lan.getLanguage());
                    }
                    realm.commitTransaction();
                    sharedPreferences.edit()
                            .putBoolean("dataBaseInitialized", true).commit();
                }

                @Override
                public void onLanguageListDownloadFailure() {

                }
            });
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!checkLanguage()) {
                    Intent intent = new Intent(SplashActivity.this, AddLanguageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_TIME);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mPref = getApplicationContext()
                    .getSharedPreferences("token_pref",
                            Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putBoolean("add_language", true);
            editor.commit();

            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            finish();
        }
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


