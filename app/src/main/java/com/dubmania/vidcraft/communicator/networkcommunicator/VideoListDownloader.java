package com.dubmania.vidcraft.communicator.networkcommunicator;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.dubmania.vidcraft.Adapters.VideoListItem;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoListDownloader {
    private VideoListDownloaderCallback mCallback;

    public void downloadTrendingVideos(String cursor, Long user_id, ArrayList<Long> languages, VideoListDownloaderCallback callback){
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_TRENDING_CURSOR, cursor);

        params.add(ConstantsStore.PARAM_USER_ID, String.valueOf(user_id));
        JSONArray LanguageArray = new JSONArray();
        for (Long language: languages) {
            try {
                LanguageArray.put(new JSONObject().put(ConstantsStore.PARAM_LANGUAGE_ID, String.valueOf(language)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        params.put(ConstantsStore.PARAM_LANGUAGE, LanguageArray.toString());

        downloadVideos(ConstantsStore.URL_TRENDING, params, callback);
    }

    public void downloadBoardVideo(Long id, Long user_id, VideoListDownloaderCallback callback) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_BOARD_ID, String.valueOf(id));
        params.add(ConstantsStore.PARAM_USER, String.valueOf(user_id));

        downloadVideos(ConstantsStore.URL_GET_BOARD_VIDEOS, params, callback);
    }

    public void downloadVideos(String url, RequestParams params, VideoListDownloaderCallback mCallback) {
        this.mCallback = mCallback;
        VidsCraftHttpClient.post(url, params, new VideoDataDownloaderHandler());
    }

    private class VideoDataDownloaderHandler extends JsonHttpResponseHandler {
        @Override
        public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
            try {
                Log.d("json error", response.toString());
                if(!response.getBoolean("result")) {
                    mCallback.onVideosDownloadFailure();
                    return;
                }
                String cursor = response.getString(ConstantsStore.PARAM_NEXT_CURSOR);

                JSONArray videoList = response.getJSONArray(ConstantsStore.PARAM_VIDEO_LIST);
                ArrayList <VideoListItem> mVideoItemList = new ArrayList<>();
                for( int i = 0; i < videoList.length(); i++ ){
                    JSONObject video = videoList.getJSONObject(i);
                    String thumbnailString = video.getString(ConstantsStore.PARAM_VIDEO_THUMBNAIL);
                    byte[] thumbnailByte = Base64.decode(thumbnailString, 0);
                    Bitmap thumbnail = BitmapFactory.decodeByteArray(thumbnailByte, 0, thumbnailByte.length);
                    mVideoItemList.add(new VideoListItem(Long.valueOf(video.getString(ConstantsStore.PARAM_USER_ID)),
                            video.getString(ConstantsStore.PARAM_VIDEO_TITLE),
                            video.getString(ConstantsStore.PARAM_USER_NAME),
                            video.getBoolean(ConstantsStore.PARAM_VIDEO_FAV), thumbnail));
                }
                mCallback.onVideosDownloadSuccess(mVideoItemList, cursor);
            } catch (JSONException e) {
                e.printStackTrace();
                mCallback.onVideosDownloadFailure();
            }
        }

        @Override
        public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
           // Log.d("json error", errorResponse.toString());
            mCallback.onVideosDownloadFailure();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
            //Log.d("json error", response);
            mCallback.onVideosDownloadFailure();
        }
    }
}

