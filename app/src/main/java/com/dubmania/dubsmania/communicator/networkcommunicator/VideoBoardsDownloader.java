package com.dubmania.dubsmania.communicator.networkcommunicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import com.dubmania.dubsmania.Adapters.VideoBoardListItem;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rat on 8/11/2015.
 */
public class VideoBoardsDownloader {

    private VideoBoardDownloaderCallback mCallback;
    private Context mContext;

    public VideoBoardsDownloader(Context mContext) {
        this.mContext = mContext;
    }

    public void getVideoBoards(String url, RequestParams params, VideoBoardDownloaderCallback callback) {
        mCallback = callback;
        DubsmaniaHttpClient.post(url, params, new VideoBoardDownloaderHandler());
    }

    public void getUserBoards(String user, VideoBoardDownloaderCallback callback) {
        getVideoBoards(ConstantsStore.URL_GET_BOARDS, new RequestParams(ConstantsStore.PARAM_USER, user), callback);
    }

    private class VideoBoardDownloaderHandler extends JsonHttpResponseHandler {
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
            }
        }
    }
}
