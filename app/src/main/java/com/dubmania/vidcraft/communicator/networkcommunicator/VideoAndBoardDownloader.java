package com.dubmania.vidcraft.communicator.networkcommunicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.dubmania.vidcraft.Adapters.ListItem;
import com.dubmania.vidcraft.Adapters.VideoBoardListItem;
import com.dubmania.vidcraft.Adapters.VideoListItem;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.addvideo.Tag;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rat on 9/10/2015.
 */
public class VideoAndBoardDownloader {
    private VideoAndBoardDownloaderCallback mCallback;
    private Context mContext;

    public VideoAndBoardDownloader(Context mContext) {
        this.mContext = mContext;
    }

    public void search(String tag, Long user, VideoAndBoardDownloaderCallback callback) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_TAGS, tag);
        params.add(ConstantsStore.PARAM_USER_ID, String.valueOf(user));
        downloadVideoAndBoard(ConstantsStore.URL_SEARCH_VIDEOS, params, callback);
    }

    public void discover(int page, Long user, ArrayList<Long> languages, VideoAndBoardDownloaderCallback callback) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_PAGE, String.valueOf(page));
        params.add(ConstantsStore.PARAM_USER_ID, String.valueOf(user));


        JSONArray LanguageArray = new JSONArray();
        for (Long language: languages) {
            try {
                LanguageArray.put(new JSONObject().put(ConstantsStore.PARAM_LANGUAGE_ID, String.valueOf(language)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        params.put(ConstantsStore.PARAM_LANGUAGE, LanguageArray.toString());

        downloadVideoAndBoard(ConstantsStore.URL_DISCOVER, params, callback);
    }

    public void downloadVideoAndBoard(String url, RequestParams params, VideoAndBoardDownloaderCallback mCallback) {
        this.mCallback = mCallback;
        DubsmaniaHttpClient.post(url, params, new VideoAndBoardDownloadHandler());
    }

    private class VideoAndBoardDownloadHandler extends JsonHttpResponseHandler {
        @Override
        public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
            try {
                Log.d("json error", response.toString());
                if(!response.getBoolean("result")) {
                    mCallback.onVideoAndBoardDownloaderFailure();
                    return;
                }

                TypedArray mBoardIcons = mContext.getResources()
                        .obtainTypedArray(R.array.video_board_icons);

                JSONArray itemList = response.getJSONArray(ConstantsStore.PARAM_ITEM_LIST);
                ArrayList<ListItem> mList = new ArrayList<>();
                for( int i = 0; i < itemList.length(); i++ ){
                    JSONObject item = itemList.getJSONObject(i);
                    switch (item.getInt(ConstantsStore.PARAM_ITEM_TYPE)) {
                        case ConstantsStore.PARAM_ITEM_TYPE_VIDEO:
                            String thumbnailString = item.getString(ConstantsStore.PARAM_VIDEO_THUMBNAIL);
                            byte[] thumbnailByte = Base64.decode(thumbnailString, 0);
                            Bitmap thumbnail = BitmapFactory.decodeByteArray(thumbnailByte, 0, thumbnailByte.length);
                            mList.add(new VideoListItem(Long.valueOf(item.getString(ConstantsStore.PARAM_USER_ID)),
                                    item.getString(ConstantsStore.PARAM_VIDEO_TITLE),
                                    item.getString(ConstantsStore.PARAM_USER_NAME),
                                    item.getBoolean(ConstantsStore.PARAM_VIDEO_FAV), thumbnail));
                            break;
                        case ConstantsStore.PARAM_ITEM_TYPE_BOARD:
                            Long id = Long.valueOf(item.getString(ConstantsStore.PARAM_BOARD_ID));
                            mList.add(new VideoBoardListItem(id, item.getString(ConstantsStore.PARAM_BOARD_NAME),
                                    item.getString(ConstantsStore.PARAM_USER_NAME),
                                    mBoardIcons.getResourceId(item.getInt(ConstantsStore.PARAM_BOARD_ICON), -1)));
                    }
                }
                mCallback.onVideoAndBoardDownloaderSuccess(mList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
            //Log.d("json error", errorResponse.toString());
            mCallback.onVideoAndBoardDownloaderFailure();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
            Log.d("json error", response);
            mCallback.onVideoAndBoardDownloaderFailure();
        }
    }
}
