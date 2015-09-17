package com.dubmania.vidcraft.communicator.networkcommunicator;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;

/**
 * Created by rat on 8/9/2015.
 */
public class VideoDownloader {
    File mVideoFile;
    VideoDownloaderCallback mCallback;

    public void downloadVideo(String url, Long id, File mFile, VideoDownloaderCallback mCallback) {
        this.mCallback = mCallback;
        mVideoFile = mFile;
        DubsmaniaHttpClient.post(url, new RequestParams("id", id.toString()), new VideoDataDownloaderHandler(mVideoFile));
    }

    private class VideoDataDownloaderHandler extends FileAsyncHttpResponseHandler {

        public VideoDataDownloaderHandler(File file) {
            super(file);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
            mCallback.onVideosDownloadFailure();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, File response) {
            mCallback.onVideosDownloadSuccess(response);
        }
    }
}
