package com.dubmania.dubsmania.communicator.networkcommunicator;

import android.os.Environment;

import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.OnVideoDownloadEvent;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;

/**
 * Created by hardik.parekh on 8/3/2015.
 */
public class VideoDownloadClient {
    public void downloadVideo(final String videoName) {
        File myFile = new File(Environment.getExternalStorageDirectory() ,videoName);
        DubsmaniaHttpClient.get("clips.vorwaerts-gmbh.de/" + videoName, null, new FileAsyncHttpResponseHandler(myFile) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, File responseBody) {
                //Add Decompressing Logic Here
                OnVideoDownloadEvent event = new OnVideoDownloadEvent();
                event.setSavedLocation(getTargetFile().getAbsolutePath());
                BusProvider.getInstance().post(event);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, File responseBody) {

            }
        });
    }
}
