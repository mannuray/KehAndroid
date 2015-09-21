package com.dubmania.vidcraft.communicator.networkcommunicator;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import com.dubmania.vidcraft.addvideo.Tag;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by rat on 8/11/2015.
 */
public class VideoUploader {
    private VideoUploaderCallback mCallback;

    public void addVideo(final String mFilePath, String title, ArrayList<Tag> tags, Long language, VideoUploaderCallback callback) {
        Log.i("File 1", mFilePath);
        final File mVideoFile = new File(mFilePath);
        mCallback = callback;

        // encode image to byte array
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mVideoFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] thumbnail = stream.toByteArray();
        String imageEncoded = Base64.encodeToString(thumbnail, Base64.DEFAULT);
        InputStream myInputStream = new ByteArrayInputStream(imageEncoded.getBytes());

        JSONArray tagsArray = new JSONArray();
        for (Tag tag: tags)
        {
            try {
                tagsArray.put(new JSONObject().put(ConstantsStore.PARAM_TAG_ID, tag.getId()).put(ConstantsStore.PARAM_TAG_NAME, tag.getTag()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RequestParams params = new RequestParams();
        params.put(ConstantsStore.PARAM_VIDEO_TITLE, title);
        params.put(ConstantsStore.PARAM_VIDEO_THUMBNAIL, myInputStream, "thumbnail.png");
        params.put(ConstantsStore.PARAM_TAGS, tagsArray.toString());
        params.put(ConstantsStore.PARAM_VIDEO_LANGUAGE, language);

        Log.i("URL", "ule is " + ConstantsStore.URL_ADD_VIDEO);
        DubsmaniaHttpClient.post(ConstantsStore.URL_ADD_VIDEO, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                try {
                    Log.i("URL", "response  is " + response.toString());
                    String uploadURL = response.getString(ConstantsStore.PARAM_VIDEO_UPLOAD_URL);
                    Log.i("URL", "upload ule is " + uploadURL);
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("file", mVideoFile);
                    requestParams.setForceMultipartEntityContentType(true);
                    DubsmaniaHttpClient.postAbsolute(uploadURL, requestParams, new VideoUploadHandler());

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                Log.i("URL", "upload failed first");
                mCallback.onVideosUploadFailure();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.i("URL", " status code is " + String.valueOf(statusCode) + " respos " + response);

            }
        });
    }

    private class VideoUploadHandler extends JsonHttpResponseHandler {
        @Override
        public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
            try {
                if(response.getBoolean("result")) {
                    mCallback.onVideosUploadSuccess();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mCallback.onVideosUploadFailure();
        }

        @Override
        public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
            // add code to remove upload failue see how it can be handled
            mCallback.onVideosUploadFailure();
            Log.i("URL", "file upload failed");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
            Log.i("URL", " status code is " + String.valueOf(statusCode) + " respos " + response);

        }
    }
}
