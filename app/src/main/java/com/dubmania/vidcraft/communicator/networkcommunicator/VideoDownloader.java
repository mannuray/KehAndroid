package com.dubmania.vidcraft.communicator.networkcommunicator;

import android.util.Log;

import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.FileCache;
import com.dubmania.vidcraft.utils.VidCraftApplication;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;

/**
 * Created by rat on 8/9/2015.
 */
public class VideoDownloader {
    VideoDownloaderCallback mCallback;
    private FileCache mCache;

    public VideoDownloader() {
        this.mCache = new FileCache(VidCraftApplication.getContext().getExternalCacheDir());
    }

    public void downloadVideo(String url, Long id, final VideoDownloaderCallback mCallback) {
        this.mCallback = mCallback;
        try {
            final File f = mCache.getFile(String.valueOf(id));
            // to simulate time so that event get delivered
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100); // hope the time is enough, need to verify by testing
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mCallback.onVideosDownloadSuccess(f);

                }
            }).start();

        } catch (IOException e) {
            DubsmaniaHttpClient.post(url, new RequestParams(ConstantsStore.PARAM_VIDEO_ID, id.toString()), new VideoDataDownloaderHandler(mCache.getTempFile(String.valueOf(id))));
        }
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
