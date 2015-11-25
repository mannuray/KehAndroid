package com.dubmania.vidcraft.communicator.networkcommunicator;

import android.util.Log;

import com.dubmania.vidcraft.Adapters.LanguageAndCountryDataHandler;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rat on 9/24/2015.
 */
public class LanguageHandler {

    private LanguageListDownloadCallback mCallback;

    public void downloadLanguage(LanguageListDownloadCallback callback) {
        downloadLanguage(ConstantsStore.URL_GET_LANGUAGES, new RequestParams(ConstantsStore.PARAM_LANGUAGE, true), callback);
    }

    public void downloadLanguageAndCountry(LanguageListDownloadCallback callback) {
        downloadLanguage(ConstantsStore.URL_GET_LANGUAGES, new RequestParams(ConstantsStore.PARAM_LANGUAGE, false), callback);
    }

    public void downloadLanguage(String mUrl, RequestParams params, LanguageListDownloadCallback callback) {
        this.mCallback = callback;
        VidsCraftHttpClient.get(mUrl, params, new LanguageAndCountriesDownloaderHandler());
    }

    private class LanguageAndCountriesDownloaderHandler extends JsonHttpResponseHandler {
        @Override
        public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
            try {
                Log.d("json error", response.toString());
                if(!response.getBoolean(ConstantsStore.PARAM_RESULT)) {
                    mCallback.onLanguageListDownloadFailure();
                    return;
                }

                boolean processCountries = !response.getBoolean(ConstantsStore.PARAM_LANGUAGE);

                JSONArray languageList = response.getJSONArray(ConstantsStore.PARAM_LANGUAGE_LIST);
                ArrayList<LanguageAndCountryDataHandler.Language> mLanguages = new ArrayList<>();
                for( int i = 0; i < languageList.length(); i++ ){
                    JSONObject language = languageList.optJSONObject(i);
                    //Log.i("Langage", " langugage is " + language.optString(ConstantsStore.PARAM_LANGUAGE_TEXT));
                    LanguageAndCountryDataHandler.Language mLanguage = new LanguageAndCountryDataHandler.Language(language.optLong(ConstantsStore.PARAM_LANGUAGE_ID),
                                                    language.optString(ConstantsStore.PARAM_LANGUAGE_TEXT));
                    if(processCountries) {
                        JSONArray countryList = language.optJSONArray("countries");
                        for( int j = 0; j < countryList.length(); j++ ) {
                            JSONObject country = countryList.optJSONObject(j);
                            //Log.i("Langage", " country is " + country.optString(ConstantsStore.PARAM_COUNTRY_TEXT));
                            mLanguage.addCountry(new LanguageAndCountryDataHandler.Country(country.optLong(ConstantsStore.PARAM_COUNTRY_ID), country.optString(ConstantsStore.PARAM_COUNTRY_TEXT)));
                        }
                    }
                    mLanguages.add(mLanguage);
                }

                mCallback.onLanguageListDownloadSuccess(new LanguageAndCountryDataHandler(mLanguages));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
            mCallback.onLanguageListDownloadFailure();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
            //Log.d("json error", response);
            mCallback.onLanguageListDownloadFailure();
        }
    }

    public static abstract class LanguageListDownloadCallback {
        abstract public void onLanguageListDownloadSuccess(LanguageAndCountryDataHandler mData);
        abstract public void onLanguageListDownloadFailure();
    }
}
