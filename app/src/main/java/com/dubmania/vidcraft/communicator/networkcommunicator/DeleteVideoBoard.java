package com.dubmania.vidcraft.communicator.networkcommunicator;

import android.util.Log;

import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

/**
 * Created by mannuk on 10/6/15.
 */
public class DeleteVideoBoard {

    private DeleteVideoBoardCallback mCallback;

    public void deleteVideoBoard(Long mBoardId, DeleteVideoBoardCallback callback) {
        mCallback = callback;
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_BOARD_ID, String.valueOf(mBoardId));
        VidsCraftHttpClient.post(ConstantsStore.URL_DELETE_BOARD, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                try {
                    Log.d("json error", response.toString());
                    if (response.getBoolean("result")) {
                        Log.d("got it ", String.valueOf(statusCode));
                        mCallback.onDeleteVideoBoardSuccess();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                mCallback.onDeleteVideoBoardFailure();
            }
        });
    }

    public static abstract class DeleteVideoBoardCallback {
        abstract public void onDeleteVideoBoardSuccess();
        abstract public void onDeleteVideoBoardFailure();
    }

}
