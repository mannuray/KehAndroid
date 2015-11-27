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
 * Created by mannuk on 11/25/15.
 */
public class AccountLanguageHandler {

    public void getUserLanguage(GetLanguageCallback callback) {
        RequestParams params = new RequestParams();
        VidsCraftHttpClient.post(ConstantsStore.URL_USER_LANGUAGES, params, new JsonHttpResponseHandler() {
            private GetLanguageCallback mCallback;

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if (!response.getBoolean("result")) {
                        mCallback.onGetLanguageCallbackFailure();
                        return;
                    }

                    //Log.d("json error", "add to video to board " + response.toString());
                    JSONArray languagesList = response.getJSONArray(ConstantsStore.PARAM_LANGUAGE_LIST);
                    ArrayList<LanguageAndCountryDataHandler.Language> mLanguageList = new ArrayList<>();
                    for (int i = 0; i < languagesList.length(); i++) {
                        JSONObject language = languagesList.getJSONObject(i);
                        mLanguageList.add(new LanguageAndCountryDataHandler.Language(language.getLong(ConstantsStore.PARAM_LANGUAGE_ID),
                                language.getString(ConstantsStore.PARAM_LANGUAGE_TEXT)));
                    }
                    mCallback.onGetLanguageCallbackSuccess(mLanguageList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mCallback.onGetLanguageCallbackFailure();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                mCallback.onGetLanguageCallbackFailure();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                //Log.d("json error", response);
                mCallback.onGetLanguageCallbackFailure();
            }

            public JsonHttpResponseHandler init(GetLanguageCallback mCallback) {
                this.mCallback = mCallback;
                return this;
            }
        }.init(callback));
    }

    public void putUserLanguage(Long id, PutLanguageCallback callback) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_LANGUAGE_ID, String.valueOf(id));
        VidsCraftHttpClient.put(ConstantsStore.URL_USER_LANGUAGES, params, new JsonHttpResponseHandler() {
            private PutLanguageCallback mCallback;

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("json error", "add to video to board " + response.toString());
                mCallback.onPutLanguageCallbackSuccess();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                mCallback.onPutLanguageCallbackFailure();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                //Log.d("json error", response);
                mCallback.onPutLanguageCallbackFailure();
            }

            public JsonHttpResponseHandler init(PutLanguageCallback mCallback) {
                this.mCallback = mCallback;
                return this;
            }
        }.init(callback));
    }

    public void deleteUserLanguage(Long id, DeleteLanguageCallback callback) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_LANGUAGE_ID, String.valueOf(id));
        VidsCraftHttpClient.delete(ConstantsStore.URL_USER_LANGUAGES, params, new JsonHttpResponseHandler() {
            private DeleteLanguageCallback mCallback;

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("json error", "add to video to board " + response.toString());
                mCallback.onDeleteLanguageCallbackSuccess();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                mCallback.onDeleteLanguageCallbackFailure();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                //Log.d("json error", response);
                mCallback.onDeleteLanguageCallbackFailure();
            }

            public JsonHttpResponseHandler init(DeleteLanguageCallback mCallback) {
                this.mCallback = mCallback;
                return this;
            }
        }.init(callback));
    }

    public abstract static class GetLanguageCallback {
        abstract public void onGetLanguageCallbackSuccess(ArrayList<LanguageAndCountryDataHandler.Language> mLanguageList);
        abstract public void onGetLanguageCallbackFailure();
    }

    public abstract static class PutLanguageCallback {
        abstract public void onPutLanguageCallbackSuccess();
        abstract public void onPutLanguageCallbackFailure();
    }

    public abstract static class DeleteLanguageCallback {
        abstract public void onDeleteLanguageCallbackSuccess();
        abstract public void onDeleteLanguageCallbackFailure();
    }
}
