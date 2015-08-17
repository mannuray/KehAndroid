package com.dubmania.dubsmania.communicator.networkcommunicator;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.dubmania.dubsmania.addvideo.Tag;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
    VideoUploaderCallback mCallback;

    public void addVideo(String mFilePath, String title, ArrayList<Tag> tags, VideoUploaderCallback callback) {
        File mVideoFile = new File(mFilePath);
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
        try {
            params.put(ConstantsStore.PARAM_VIDEO_TITLE, title);
            params.put(ConstantsStore.PARAM_VIDEO_THUMBNAIL, myInputStream, "thumbnail.png");
            params.put(ConstantsStore.PARAM_VIDEO_FILE, mVideoFile);
            params.put(ConstantsStore.PARAM_TAGS, tagsArray.toString());
        } catch(FileNotFoundException ignored) {}

        DubsmaniaHttpClient.post(ConstantsStore.URL_ADD_VIDEO, params, new JsonHttpResponseHandler() {
            @Override
            public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                mCallback.onVideosUploadSuccess();
            }
        });
    }
}
