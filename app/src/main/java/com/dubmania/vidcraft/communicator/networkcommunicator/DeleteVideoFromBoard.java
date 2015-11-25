package com.dubmania.vidcraft.communicator.networkcommunicator;

import android.util.Log;

import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by mannuk on 10/6/15.
 */
public class DeleteVideoFromBoard {

    private DeleteVideoFromBoardCallback mCallback;

    public void deleteVideoFromBoard(Long boardId, Long videoId, DeleteVideoFromBoardCallback callback) {
        mCallback = callback;
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_BOARD, String.valueOf(boardId));
        params.add(ConstantsStore.PARAM_VIDEO, String.valueOf(videoId));
        VidsCraftHttpClient.post(ConstantsStore.URL_DELETE_VIDEO_FROM_BOARD, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                Log.d("json error", "add to video to board " + response.toString());
                mCallback.onDeleteVideoFromBoardSuccess();
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                mCallback.onDeleteVideoFromBoardFailure();
            }
        });
    }

    public static abstract class DeleteVideoFromBoardCallback {
        abstract public void onDeleteVideoFromBoardSuccess();
        abstract public void onDeleteVideoFromBoardFailure();
    }
}
