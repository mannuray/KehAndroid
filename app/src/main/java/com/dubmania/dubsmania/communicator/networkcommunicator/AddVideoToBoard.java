package com.dubmania.dubsmania.communicator.networkcommunicator;

import android.util.Log;

import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by rat on 8/11/2015.
 */
public class AddVideoToBoard {

    private AddVideoToBoardCallback mCallback;

    public void addVideoToBoard(Long boardId, Long videoId, AddVideoToBoardCallback callback) {
        mCallback = callback;
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_BOARD_ID, String.valueOf(boardId));
        params.add(ConstantsStore.PARAM_VIDEO_ID, String.valueOf(videoId));
        DubsmaniaHttpClient.get(ConstantsStore.URL_ADD_VIDEO_TO_BOARD, params, new JsonHttpResponseHandler() {
            @Override
            public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                Log.d("json error", "add to video to board " + response.toString());
                mCallback.onAddVideoToBoardSuccess();
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                mCallback.onAddVideoToBoardFailure();
            }
        });
    }
}
