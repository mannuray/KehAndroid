package com.dubmania.dubsmania.communicator.networkcommunicator;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.dubmania.dubsmania.Adapters.VideoListItem;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoListDownloader {
    private Map<Long, VideoListItem> mVideoItemMap;
    private VideoDownloaderCallback mCallback;
    private Integer user;
    private int toProcess;
    private int proccessing;

    public void downloadVideos(String url, RequestParams params,VideoDownloaderCallback mCallback, Integer user) {
        this.mCallback = mCallback;
        this.user = user;
        mVideoItemMap = new HashMap<>();
        DubsmaniaHttpClient.get(url, params, new VideoDataDownloaderHandler());
    }

    private void downloadVideosFav() throws JSONException {
        /* do this later
        JSONObject entity = new JSONObject();
        JSONArray videos = new JSONArray();
        for (Map.Entry<Integer, VideoListItem> entry : mVideoItemMap.entrySet())
        {
            videos.put(entry.getKey());
        }

        entity.put("id", videos);
        DubsmaniaHttpClient.post("searchservice/getfav", new RequestParams("data", entity.toString()), new VideoDataDownloaderHandler());
        */
        DubsmaniaHttpClient.get("searchservice/getfav", new RequestParams("user", "mannuk"), new VideoFavDownloader());
    }

    private void downloadVideoIcons() {
        toProcess = mVideoItemMap.size();
        for (Map.Entry<Long, VideoListItem> entry : mVideoItemMap.entrySet())
        {
            DubsmaniaHttpClient.get("searchservice/geticon", new RequestParams("id", entry.getKey().toString()), new VideoIconDownloader(entry.getKey()));
        }
    }

    private void processedIcon() {
        proccessing++;
        if(toProcess == proccessing) {
            ArrayList<VideoListItem> videos = new ArrayList<VideoListItem>();
            for (Map.Entry<Long, VideoListItem> entry : mVideoItemMap.entrySet())
            {
                videos.add(entry.getValue());
            }
            mCallback.onVideosDownloadSuccess(videos);
        }
    }

    private class VideoDataDownloaderHandler extends JsonHttpResponseHandler {
        @Override
        public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
            try {
                Log.d("json error", response.toString());
                JSONArray videoList = response.getJSONArray("video_list");
                Log.d("json error after", response.toString());
                toProcess = videoList.length();
                for( int i = 0; i < videoList.length(); i++ ){
                    JSONObject video = videoList.getJSONObject(i);
                    Long id = new Long(video.getString("id"));
                    Log.d("json error id", id.toString());
                    Log.d("json error name", video.getString("name"));
                    mVideoItemMap.put(id, new VideoListItem(id, video.getString("name"), video.getString("user"), video.getString("desc")));
                }
                downloadVideosFav();
                downloadVideoIcons();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class VideoFavDownloader extends JsonHttpResponseHandler {
        @Override
        public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
            try {
                Log.d("json error", response.toString());
                JSONArray videoList = response.getJSONArray("video_fav_list");
                for( int i = 0; i < videoList.length(); i++ ){
                    JSONObject video = videoList.getJSONObject(i);
                    Long id = new Long(video.getString("id"));
                    Log.d("json error id fav", id.toString());
                    mVideoItemMap.get(id).setFavourite(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class VideoIconDownloader extends AsyncHttpResponseHandler {

        private Long mId;
        public VideoIconDownloader(Long id) {
            mId = id;
        }

        @Override
        public void  onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] response) {
            Bitmap thumbnail = BitmapFactory.decodeByteArray(response, 0, response.length);
            Log.d("json array icon", mId.toString());
            mVideoItemMap.get(mId).setThumbnail(thumbnail);
            processedIcon();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        }
    }
}

