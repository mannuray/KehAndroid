package com.dubmania.dubsmania.communicator.networkcommunicator;

import android.util.Log;

import com.dubmania.dubsmania.addvideo.Tag;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rat on 8/14/2015.
 */
public class TagsDownloader {
    private TagsDownloaderCallback callback;

    public void downloadTags(String mTag, TagsDownloaderCallback callback) {
        downloadTags(ConstantsStore.URL_GET_TAGS, new RequestParams(ConstantsStore.PARAM_TAG_NAME, mTag),callback);
    }

    public void downloadTags(String mUrl, RequestParams params, TagsDownloaderCallback callback) {
        this.callback = callback;
        DubsmaniaHttpClient.post(mUrl, params, new TagsDownloaderHandler());
    }

    private class TagsDownloaderHandler extends JsonHttpResponseHandler {
        @Override
        public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
            try {
                Log.d("json error", response.toString());
                JSONArray tagList = response.getJSONArray(ConstantsStore.PARAM_TAGS);
                ArrayList<Tag> mTags = new ArrayList<>();
                for( int i = 0; i < tagList.length(); i++ ){
                    JSONObject tag = tagList.getJSONObject(i);
                    mTags.add(new Tag(tag.getLong(ConstantsStore.PARAM_TAG_ID), tag.getString(ConstantsStore.PARAM_TAG_NAME)));
                }
                callback.onTagsDownloadSuccess(mTags);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
