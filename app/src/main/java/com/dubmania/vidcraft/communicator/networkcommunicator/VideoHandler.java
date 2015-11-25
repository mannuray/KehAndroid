package com.dubmania.vidcraft.communicator.networkcommunicator;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import com.dubmania.vidcraft.addvideo.Tag;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.FileCache;
import com.dubmania.vidcraft.utils.VidCraftApplication;
import com.loopj.android.http.Base64;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by rat on 8/9/2015.
 */
public class VideoHandler {
    private FileCache mCache;
    private boolean mDownloading = false;
    private RequestHandle mHandle;
    private File mCurrentFile;

    public VideoHandler() {
        this.mCache = new FileCache(VidCraftApplication.getContext().getExternalCacheDir());
    }

    public void downloadVideo(String url, Long id, final VideoDownloaderCallback mCallback) {
        try {
            final File f = mCache.getFile(String.valueOf(id));
            mCallback.onVideosDownloadSuccess(f);

        } catch (IOException e) {
            mDownloading = true;
            mCurrentFile = mCache.getTempFile(String.valueOf(id));
            mHandle = VidsCraftHttpClient.get(url, new RequestParams(ConstantsStore.PARAM_VIDEO_ID, id.toString()), new VideoDownloaderHandler(mCurrentFile).init(mCallback));
        }
    }

    public void cancelDownload() {
        //Log.i("Progress", "cancel called");
        if (mDownloading) {
            mHandle.cancel(true);
            if(mCurrentFile != null)
                mCurrentFile.delete();
            mCurrentFile = null;
        }
    }

    private class VideoDownloaderHandler extends FileAsyncHttpResponseHandler {

        private long fileSize = 1;
        VideoDownloaderCallback mCallback;

        public VideoDownloaderHandler(File file) {
            super(file);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, File response) {
            mDownloading = false;
            mCurrentFile = null;
            mCallback.onVideosDownloadSuccess(response);
        }

        @Override
        public void onProgress(long byteWritten, long totalSize) {
            mCallback.onProgress((int) (byteWritten / fileSize) * 100);
            //Log.i("Progress", "progress called " + byteWritten + " " + fileSize);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
            //Log.i("Progress", "failure called");
            mDownloading = false;
            if(mCurrentFile != null)
                mCurrentFile.delete();
            mCurrentFile = null;
            mCallback.onVideosDownloadFailure();
        }

        @Override
        public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
            super.onPreProcessResponse(instance, response);
            //Log.i("Progress", "pre process progress called " );
            Header[] headers = response.getAllHeaders();
            for (Header header : headers) {
                //Log.i("Progress", " hedar is " + header.getName() + " " + header.getValue());
                if (header.getName().equalsIgnoreCase("content-length")) {
                    String value = header.getValue();
                    fileSize = Long.valueOf(value);
                    //Log.i("Progress", "pre process progress called " + fileSize);
                }
            }
        }

        public FileAsyncHttpResponseHandler init(VideoDownloaderCallback mCallback) {
            this.mCallback = mCallback;
            return this;
        }
    }

    // uploader

    public void addVideo(final String mFilePath, String title, ArrayList<Tag> tags, Long language, VideoUploaderCallback callback) {
        final File mVideoFile = new File(mFilePath);

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

        Log.i("URL", "ule is " + ConstantsStore.URL_VIDEO);
        VidsCraftHttpClient.post(ConstantsStore.URL_VIDEO, params, new JsonHttpResponseHandler() {

            private VideoUploaderCallback mCallback;

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                try {
                    Log.i("URL", "response  is " + response.toString());
                    String uploadURL = response.getString(ConstantsStore.PARAM_VIDEO_UPLOAD_URL);
                    Long id = response.getLong(ConstantsStore.PARAM_VIDEO_ID);
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("file", mVideoFile);
                    requestParams.setForceMultipartEntityContentType(true);
                    VidsCraftHttpClient.postAbsolute(uploadURL, requestParams, new VideoUploadHandler(id, mCallback));

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

            public JsonHttpResponseHandler init(VideoUploaderCallback mCallback) {
                this.mCallback = mCallback;
                return this;
            }
        }.init(callback));
    }

    private class VideoUploadHandler extends JsonHttpResponseHandler {
        private long mId;
        private VideoUploaderCallback mCallback;

        private VideoUploadHandler(long mId, VideoUploaderCallback mCallback) {
            this.mId = mId;
            this.mCallback = mCallback;
        }

        @Override
        public void  onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
            try {
                if(response.getBoolean("result")) {
                    mCallback.onVideosUploadSuccess(mId);
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

    public abstract static class VideoDownloaderCallback {
        abstract public void onVideosDownloadSuccess(File mFile);
        abstract public void onVideosDownloadFailure();
        abstract public void onProgress(int mPercentage);
    }

    public abstract static class VideoUploaderCallback {
        abstract public void onVideosUploadSuccess(long mId);
        abstract public void onVideosUploadFailure();
    }
}
