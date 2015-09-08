package com.dubmania.dubsmania.communicator.networkcommunicator;

import android.util.Log;

import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

/**
 * Created by rat on 8/11/2015.
 */
public class VideoBoardCreator {
    private VideoBoardCreaterCallback mCallback;

    public void addVideoBoard(String boardName, int iconId, VideoBoardCreaterCallback callback) {
        mCallback = callback;
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_BOARD_NAME, boardName);
        params.add(ConstantsStore.PARAM_BOARD_ICON, String.valueOf(iconId));
        DubsmaniaHttpClient.post(ConstantsStore.URL_ADD_BOARD, params, new JsonHttpResponseHandler(){
            @Override
            public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                try {
                    Log.d("json error", response.toString());
                    if (response.getBoolean("result")) {
                        Log.d("got it ", String.valueOf(statusCode));
                        mCallback.onVideoBoardsCreaterSuccess();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                mCallback.onVideosBoardsCreaterFailure();
            }
        });
    }
}
