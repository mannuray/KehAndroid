package com.dubmania.vidcraft.misc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.dubmania.vidcraft.Adapters.LanguageAndCountryDataHandler;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.vidcraft.communicator.networkcommunicator.LanguageListDownloader;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.dubmania.vidcraft.communicator.networkcommunicator.LanguageListDownloader.*;

public class AddLanguageActivity extends AppCompatActivity {
    Toolbar mToolbar;
    private NumberPicker mCountryPicker;
    NumberPicker mLanguagePicker;
    private LanguageAndCountryDataHandler mLanguageData;
    private int mLanguagePosition;
    private int mCountryPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_language);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        mLanguagePicker = (NumberPicker) findViewById(R.id.language_picker);
        mCountryPicker = (NumberPicker) findViewById(R.id.country_picker);

        Button start = (Button) findViewById(R.id.add_language);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TO DO decide what to do based on country and language
                // add to data store and finish
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
        mLanguagePicker.setDisplayedValues(mLanguageData.getLanguages());
        mLanguagePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mLanguagePosition = newVal;
                mCountryPicker.setDisplayedValues(null);
                mCountryPicker.setMinValue(0);
                mCountryPicker.setMaxValue(mLanguageData.getCountriesSize(mLanguagePosition) - 1);
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
                setData();
            }

            @Override
            public void onLanguageListDownloadFailure() {

            }
        });
    }
}
