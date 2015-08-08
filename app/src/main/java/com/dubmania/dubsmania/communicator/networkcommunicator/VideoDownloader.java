package com.dubmania.dubsmania.communicator.networkcommunicator;

import android.content.Context;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;

/**
 * Created by rat on 8/9/2015.
 */
public class VideoDownloader {
    File mVideo;
    Context mContext;
    VideoDownloaderCallback mCallback;

    public VideoDownloader(Context context) {
        mContext = context;
    }

    public void downloadVideo(String url, Long id, VideoDownloaderCallback mCallback) throws IOException {
        this.mCallback = mCallback;
        mVideo = File.createTempFile(id.toString(), "mp4", mContext.getCacheDir());
        DubsmaniaHttpClient.get(url, new RequestParams("id", id.toString()), new VideoDataDownloaderHandler(mVideo));
    }

    private class VideoDataDownloaderHandler extends FileAsyncHttpResponseHandler {

        public VideoDataDownloaderHandler(File file) {
            super(file);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, File response) {
            mCallback.onVideosDownloadSuccess(response);
        }
    }
}
