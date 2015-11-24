package com.dubmania.vidcraft.communicator.networkcommunicator;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.FileCache;
import com.dubmania.vidcraft.utils.VidCraftApplication;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.io.File;
import java.io.IOException;

/**
 * Created by rat on 8/9/2015.
 */
public class VideoDownloader {
    VideoDownloaderCallback mCallback;
    private FileCache mCache;
    private boolean mDownloading = false;
    private RequestHandle mHandle;
    private File mCurrentFile;

    public VideoDownloader() {
        this.mCache = new FileCache(VidCraftApplication.getContext().getExternalCacheDir());
    }

    public void downloadVideo(String url, Long id, final VideoDownloaderCallback mCallback) {
        this.mCallback = mCallback;
        try {
            final File f = mCache.getFile(String.valueOf(id));
            mCallback.onVideosDownloadSuccess(f);

        } catch (IOException e) {
            mDownloading = true;
            mCurrentFile = mCache.getTempFile(String.valueOf(id));
            mHandle = DubsmaniaHttpClient.post(url, new RequestParams(ConstantsStore.PARAM_VIDEO_ID, id.toString()), new VideoDataDownloaderHandler(mCurrentFile));
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

    private class VideoDataDownloaderHandler extends FileAsyncHttpResponseHandler {

        private long fileSize = 1;

        public VideoDataDownloaderHandler(File file) {
            super(file);
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
        public void onSuccess(int statusCode, Header[] headers, File response) {
            mDownloading = false;
            mCurrentFile = null;
            mCallback.onVideosDownloadSuccess(response);
        }

        @Override
        public void onProgress(long byteWritten, long totalSize) {
            mCallback.onProgress((int)(byteWritten/fileSize)*100);
            //Log.i("Progress", "progress called " + byteWritten + " " + fileSize);
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
    }
}
