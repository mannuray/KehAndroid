package com.dubmania.vidcraft.communicator.networkcommunicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import com.dubmania.vidcraft.Adapters.VideoBoardListItem;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rat on 8/11/2015.
 */
public class VideoBoardHandler {

    private Context mContext;

    public VideoBoardHandler(Context mContext) {
        this.mContext = mContext;
    }

    public void addVideoBoard(String boardName, int iconId, VideoBoardCreateCallback callback) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_BOARD_NAME, boardName);
        params.add(ConstantsStore.PARAM_BOARD_ICON, String.valueOf(iconId));
        VidsCraftHttpClient.post(ConstantsStore.URL_BOARD, params, new JsonHttpResponseHandler() {

            private VideoBoardCreateCallback mCallback;

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                try {
                    Log.d("json error", response.toString());
                    if (response.getBoolean("result")) {
                        Log.d("got it ", String.valueOf(statusCode));
                        mCallback.onVideoBoardCreateSuccess(response.getLong("id"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    mCallback.onVideoBoardCreateFailure();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                mCallback.onVideoBoardCreateFailure();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                //Log.d("json error", response);
                mCallback.onVideoBoardCreateFailure();
            }

            public JsonHttpResponseHandler init(VideoBoardCreateCallback mCallback) {
                this.mCallback = mCallback;
                return this;
            }
        }.init(callback));
    }

    public void getVideoBoards(String url, RequestParams params, VideoBoardDownloaderCallback callback) {
        VidsCraftHttpClient.get(url, params, new VideoBoardDownloaderHandler().init(callback));
    }

    public void getUserBoards(String user, VideoBoardDownloaderCallback callback) {
        getVideoBoards(ConstantsStore.URL_BOARD, new RequestParams(ConstantsStore.PARAM_USER, user), callback);
    }

    private class VideoBoardDownloaderHandler extends JsonHttpResponseHandler {

        private VideoBoardDownloaderCallback mCallback;

        @Override
        public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
            try {
                Log.d("json error", response.toString());
                JSONArray videoBoardList = response.getJSONArray(ConstantsStore.PARAM_BOARD_LIST);
                ArrayList<VideoBoardListItem> mBoardList = new ArrayList<>();

                TypedArray mBoardIcons = mContext.getResources()
                        .obtainTypedArray(R.array.video_board_icons);
                for( int i = 0; i < videoBoardList.length(); i++ ){
                    JSONObject board = videoBoardList.getJSONObject(i);
                    Long id = Long.valueOf(board.getString(ConstantsStore.PARAM_BOARD_ID));
                    mBoardList.add(new VideoBoardListItem(id, board.getString(ConstantsStore.PARAM_BOARD_NAME),
                            board.getString(ConstantsStore.PARAM_USER_NAME),
                            mBoardIcons.getResourceId(board.getInt(ConstantsStore.PARAM_BOARD_ICON), -1)));
                }
                mBoardIcons.recycle();
                mCallback.onVideoBoardsDownloadSuccess(mBoardList);
            } catch (JSONException e) {
                e.printStackTrace();
                mCallback.onVideosBoardsDownloadFailure();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            mCallback.onVideosBoardsDownloadFailure();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
            //Log.d("json error", response);
            mCallback.onVideosBoardsDownloadFailure();
        }

        public JsonHttpResponseHandler init(VideoBoardDownloaderCallback mCallback) {
            this.mCallback = mCallback;
            return this;
        }
    }

    public void deleteVideoBoard(Long mBoardId, DeleteVideoBoardCallback callback) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_BOARD_ID, String.valueOf(mBoardId));
        VidsCraftHttpClient.delete(ConstantsStore.URL_BOARD, params, new JsonHttpResponseHandler() {

            private DeleteVideoBoardCallback mCallback;

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
                    mCallback.onDeleteVideoBoardFailure();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                mCallback.onDeleteVideoBoardFailure();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                //Log.d("json error", response);
                mCallback.onDeleteVideoBoardFailure();
            }

            public JsonHttpResponseHandler init(DeleteVideoBoardCallback mCallback) {
                this.mCallback = mCallback;
                return this;
            }

        }.init(callback));
    }

    public abstract static class VideoBoardCreateCallback {
        abstract public void onVideoBoardCreateSuccess(Long boardId);
        abstract public void onVideoBoardCreateFailure();
    }

    public abstract static class VideoBoardDownloaderCallback {
        abstract public void onVideoBoardsDownloadSuccess(ArrayList<VideoBoardListItem> boards);
        abstract public void onVideosBoardsDownloadFailure();
    }

    public abstract static class DeleteVideoBoardCallback {
        abstract public void onDeleteVideoBoardSuccess();
        abstract public void onDeleteVideoBoardFailure();
    }
}
