package com.dubmania.vidcraft.communicator.networkcommunicator;

import android.util.Log;

import com.dubmania.vidcraft.addvideo.Tag;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rat on 8/14/2015.
 */
public class TagsHandler {
    private TagsDownloaderCallback mCallback;

    public void downloadTags(String mTag, TagsDownloaderCallback callback) {
        downloadTags(ConstantsStore.URL_TAGS, new RequestParams(ConstantsStore.PARAM_TAG_NAME, mTag),callback);
    }

    public void downloadTags(String mUrl, RequestParams params, TagsDownloaderCallback callback) {
        this.mCallback = callback;
        VidsCraftHttpClient.post(mUrl, params, new TagsDownloaderHandler());
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
                mCallback.onTagsDownloadSuccess(mTags);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
            mCallback.onTagsDownloadFailure();
        }
    }

    public abstract static class TagsDownloaderCallback {
        abstract public void onTagsDownloadSuccess(ArrayList<Tag> mTags);
        abstract public void onTagsDownloadFailure();
    }
}
