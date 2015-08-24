package com.dubmania.dubsmania.misc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddLanguageActivity extends AppCompatActivity {
    private NumberPicker mCountryPicker;
    NumberPicker mLanguagePicker;
    private LanguageData mLanguageData;
    private int mLanguagePosition;
    private int mCountryPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_language);


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
        inflater.inflate(R.menu.menu_add_language, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        mLanguagePicker.setMinValue(0);
        mLanguagePicker.setMaxValue(mLanguageData.getLanguageSize());
        mLanguagePicker.setDisplayedValues(mLanguageData.getLanguages());
        mLanguagePicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                mLanguagePosition = scrollState;
                mCountryPicker.setDisplayedValues(null);
                mCountryPicker.setMinValue(0);
                mLanguagePicker.setMaxValue(mLanguageData.getCountriesSize(scrollState));
                mCountryPicker.setDisplayedValues(mLanguageData.getContries(scrollState));
                mCountryPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
                    @Override
                    public void onScrollStateChange(NumberPicker view, int scrollState) {
                        mCountryPosition = scrollState;
                    }
                });
            }
        });
    }

    private void populateData() {
        DubsmaniaHttpClient.post(ConstantsStore.URL_GET_LANGUAGES, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                try {
                    Log.d("json error", response.toString());
                    mLanguageData = new LanguageData();
                    JSONArray languageList = response.getJSONArray(ConstantsStore.PARAM_LANGUAGE_LIST);
                    for( int i = 0; i < languageList.length(); i++ ){
                        JSONObject language = languageList.getJSONObject(i);
                        LanguageData.Language l = new LanguageData.Language(language.getLong(ConstantsStore.PARAM_LANGUAGE_ID), ConstantsStore.PARAM_LANGUAGE_TEXT);
                        JSONArray countryList = response.getJSONArray(ConstantsStore.PARAM_COUNTRY_LIST);
                        for( int j = 0; j < languageList.length(); j++ ) {
                            JSONObject country = countryList.getJSONObject(i);
                            l.addCountry(new LanguageData.Country(country.getLong(ConstantsStore.PARAM_COUNTRY_ID), ConstantsStore.PARAM_COUNTRY_TEXT));
                        }
                    }

                    setData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                // show layout for retry
            }
        });
    }

    private static class LanguageData {

        ArrayList<Language> mLanguages;

        public LanguageData() {
            mLanguages = new ArrayList<>();
        }

        public String[] getLanguages() {
            String[] languages = new String[mLanguages.size()];
            for(int i = 0; i < mLanguages.size(); i++) {
                languages[i] = mLanguages.get(i).getLanguage();
            }
            return languages;
        }

        public String[] getContries(int pos) {
            return mLanguages.get(pos).getContries();
        }

        public int getLanguageSize() {
            return mLanguages.size();
        }

        public int getCountriesSize(int pos) {
            return mLanguages.get(pos).getCountriesSize();
        }

        public static class Language {
            private Long id;
            private String mLanguage;
            private ArrayList<Country> mCountries;

            public Language(Long id, String mLanguage) {
                this.id = id;
                this.mLanguage = mLanguage;
                mCountries = new ArrayList<>();
            }

            public Long getId() {
                return id;
            }

            public String getLanguage() {
                return mLanguage;
            }

            public void addCountry(Country country) {
                mCountries.add(country);
            }

            public String[] getContries() {
                String[] countries = new String[mCountries.size()];
                for(int i = 0; i < mCountries.size(); i++) {
                    countries[i] = mCountries.get(i).getCountry();
                }
                return countries;
            }

            public int getCountriesSize() {
                return mCountries.size();
            }
        }

        public static class Country {
            private Long id;
            private String mCountry;

            public Country(Long id, String mCountry) {
                this.id = id;
                this.mCountry = mCountry;
            }

            public Long getId() {
                return id;
            }

            public String getCountry() {
                return mCountry;
            }
        }
    }
}
