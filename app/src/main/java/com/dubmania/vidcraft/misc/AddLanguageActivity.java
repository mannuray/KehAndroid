package com.dubmania.vidcraft.misc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dubmania.vidcraft.Adapters.LanguageAndCountryDataHandler;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.vidcraft.communicator.networkcommunicator.LanguageListDownloader;
import com.dubmania.vidcraft.main.HomeActivity;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.InstalledLanguage;
import com.dubmania.vidcraft.utils.SavedDubsData;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

import io.realm.Realm;

import static com.dubmania.vidcraft.communicator.networkcommunicator.LanguageListDownloader.*;

public class AddLanguageActivity extends AppCompatActivity {
    Toolbar mToolbar;
    private NumberPicker mCountryPicker;
    NumberPicker mLanguagePicker;
    private LanguageAndCountryDataHandler mLanguageData;
    private int mLanguagePosition;
    private int mCountryPosition;
    public SharedPreferences token_prefs;
    ProgressBar progressBar;
    LinearLayout languagePickerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_language);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
       /* actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/


        mLanguagePicker = (NumberPicker) findViewById(R.id.language_picker);
        mCountryPicker = (NumberPicker) findViewById(R.id.country_picker);
        progressBar=(ProgressBar)findViewById(R.id.progressBar3);
        languagePickerLayout=(LinearLayout)findViewById(R.id.language_picker_layout);

        RelativeLayout start = (RelativeLayout) findViewById(R.id.add_language);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getInstance(getApplicationContext());
                realm.beginTransaction();
                InstalledLanguage installedLanguage = realm.createObject( InstalledLanguage.class );
                LanguageAndCountryDataHandler.Language lan = mLanguageData.getLanguage(mLanguagePosition);
                installedLanguage.setLanguageId(lan.getId());
                installedLanguage.setLanguage(lan.getLanguage());
                LanguageAndCountryDataHandler.Country con = lan.getCountry(mCountryPosition);
                installedLanguage.setCountryId(con.getId());
                installedLanguage.setCountry(con.getCountry());
                realm.commitTransaction();
                token_prefs = getApplicationContext()
                        .getSharedPreferences("token_pref",
                                Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = token_prefs.edit();
                editor.putBoolean("add_language", true);
                editor.commit();

                Intent intent = new Intent(AddLanguageActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        populateData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empty, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        mLanguagePicker.setMinValue(0);
        mLanguagePicker.setMaxValue(mLanguageData.getLanguageSize() - 1);
        mLanguagePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mLanguagePicker.setDisplayedValues(mLanguageData.getLanguages());
        int pos = mLanguagePicker.getValue();
        mCountryPicker.setMinValue(0);
        mCountryPicker.setMaxValue(mLanguageData.getCountriesSize(mLanguagePosition) - 1);
        mCountryPicker.setDisplayedValues(mLanguageData.getCountries(pos));
        mLanguagePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mLanguagePosition = newVal;
                mCountryPicker.setDisplayedValues(null);
                mCountryPicker.setMinValue(0);
                mCountryPicker.setMaxValue(mLanguageData.getCountriesSize(mLanguagePosition) - 1);
                mCountryPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                mCountryPicker.setDisplayedValues(mLanguageData.getCountries(mLanguagePosition));
                mCountryPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        mCountryPosition = newVal;
                    }
                });
            }
        });
    }

    private void populateData() {
        new LanguageListDownloader().downloadLanguageAndCountry(new LanguageListDownloader.LanguageListDownloadCallback() {
            @Override
            public void onLanguageListDownloadSuccess(LanguageAndCountryDataHandler mData) {
                mLanguageData = mData;
                progressBar.setVisibility(View.GONE);
                languagePickerLayout.setVisibility(View.VISIBLE);
                setData();
            }

            @Override
            public void onLanguageListDownloadFailure() {

            }
        });
    }
}
