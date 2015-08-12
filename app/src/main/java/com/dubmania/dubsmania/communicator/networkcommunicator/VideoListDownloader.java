package com.dubmania.dubsmania.communicator.networkcommunicator;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.dubmania.dubsmania.Adapters.VideoListItem;
import com.dubmania.dubsmania.utils.ConstantsStore;
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
    private VideoListDownloaderCallback mCallback;

    private boolean user = false;
    private int toProcess;
    private int processing;
    private RequestParams mParams;

    public void downloadTrendingVideos(String region, Integer start, Integer end, String user, VideoListDownloaderCallback callback){
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_REGION, region);
        params.add(ConstantsStore.PARAM_START, String.valueOf(start));
        params.add(ConstantsStore.PARAM_END, String.valueOf(end));
        params.add(ConstantsStore.PARAM_USER, user);// TO DO get user name

        downloadVideos(ConstantsStore.GET_TRENDING_VIDEOS_URL, params, callback);
    }

    public void downloadDiscoverVideos(Integer page, String user, VideoListDownloaderCallback callback) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_START, String.valueOf(page * 10));
        params.add(ConstantsStore.PARAM_END, String.valueOf((page + 1) * 10));
        params.add(ConstantsStore.PARAM_USER, user);// TO DO get user name

        downloadVideos(ConstantsStore.URL_GET_DISCOVER_VIDEOS, params, callback);
    }

    public void downloadBoardVideo(Long id, String user, VideoListDownloaderCallback callback) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_BOARD_ID, String.valueOf(id));
        params.add(ConstantsStore.PARAM_USER, user); // change this param for current user

        downloadVideos(ConstantsStore.URL_GET_BOARD_VIDEOS, params, callback);
    }

    public void searchVideos(String tag, VideoListDownloaderCallback callback) {
        downloadVideos(ConstantsStore.URL_SEARCH_VIDEOS, new RequestParams(ConstantsStore.PARAM_TAGS, tag), callback);

    }

    public void downloadVideos(String url, RequestParams params, VideoListDownloaderCallback mCallback) {
        this.mCallback = mCallback;
        mVideoItemMap = new HashMap<>();
        if(params.has("user")) {
            user = true;
            mParams = params;
        }
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
        DubsmaniaHttpClient.get(ConstantsStore.GET_FAV_URL, mParams, new VideoFavDownloader());
    }

    private void downloadVideoIcons() {
        toProcess = mVideoItemMap.size();
        for (Map.Entry<Long, VideoListItem> entry : mVideoItemMap.entrySet())
        {
            DubsmaniaHttpClient.get(ConstantsStore.GET_ICON_URL, new RequestParams("id", entry.getKey().toString()), new VideoIconDownloader(entry.getKey()));
        }
    }

    private void processedIcon() {
        processing++;
        if(toProcess == processing) {
            ArrayList<VideoListItem> videos = new ArrayList<>();
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
                toProcess = videoList.length();
                for( int i = 0; i < videoList.length(); i++ ){
                    JSONObject video = videoList.getJSONObject(i);
                    Long id = Long.valueOf(video.getString("id"));
                    Log.d("json error id", id.toString());
                    mVideoItemMap.put(id, new VideoListItem(id, video.getString("name"), video.getString("user"), video.getString("desc")));
                }
                if(user)
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
                    Long id = Long.valueOf(video.getString("id"));
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

