package com.dubmania.dubsmania.communicator.networkcommunicator;

import android.util.Log;

import com.dubmania.dubsmania.Adapters.VideoBoardListItem;
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

    public VideoBoardsDownloader() {

    }

    public void getVideoBoards(String url, RequestParams params, VideoBoardDownloaderCallback callback) {
        mCallback = callback;
        DubsmaniaHttpClient.get(url, params, new VideoBoardDownloaderHandler());
    }

    public void getTrendingVideo(String mRegion, Integer start, Integer end,VideoBoardDownloaderCallback callback ) {
        mCallback = callback;
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_REGION, mRegion);
        params.add(ConstantsStore.PARAM_START, String.valueOf(start));
        params.add(ConstantsStore.PARAM_END, String.valueOf(end));

        getVideoBoards(ConstantsStore.URL_GET_TRENDING_BOARDS, params, callback);
    }

    private class VideoBoardDownloaderHandler extends JsonHttpResponseHandler {
        @Override
        public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
            try {
                Log.d("json error", response.toString());
                JSONArray videoBoardList = response.getJSONArray("board_list");
                ArrayList<VideoBoardListItem> mBoardList = new ArrayList<>();
                for( int i = 0; i < videoBoardList.length(); i++ ){
                    JSONObject board = videoBoardList.getJSONObject(i);
                    Long id = Long.valueOf(board.getString("id"));
                    mBoardList.add(new VideoBoardListItem(id, board.getString("name"), board.getString("user"), board.getInt("iconid")));
                }
                mCallback.onVideoBoardsDownloadSuccess(mBoardList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
