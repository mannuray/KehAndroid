package com.dubmania.dubsmania.communicator.networkcommunicator;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.dubmania.dubsmania.addvideo.Tag;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by rat on 8/11/2015.
 */
public class VideoUploader {

    public void addVideo(String mFilePath, String title, String desc, ArrayList<Tag> tags) {
        File mVideoFile = new File(mFilePath);
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mVideoFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] thumbnail = stream.toByteArray();

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
            params.put(ConstantsStore.PARAM_VIDEO_DESC, desc);
            params.put(ConstantsStore.PARAM_ICON_FILE, thumbnail);
            params.put(ConstantsStore.PARAM_VIDEO_FILE, mVideoFile);
            params.put(ConstantsStore.PARAM_TAGS, tagsArray.toString());
        } catch(FileNotFoundException ignored) {}

        DubsmaniaHttpClient.post(ConstantsStore.URL_ADD_VIDEO, params, new JsonHttpResponseHandler() {
            @Override
            public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {

            }
        });
    }
}
