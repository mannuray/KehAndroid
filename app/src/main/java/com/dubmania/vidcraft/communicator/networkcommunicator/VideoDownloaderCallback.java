package com.dubmania.vidcraft.communicator.networkcommunicator;

import java.io.File;

/**
 * Created by rat on 8/9/2015.
 */
public abstract class VideoDownloaderCallback {
    abstract public void onVideosDownloadSuccess(File mFile);
    abstract public void onVideosDownloadFailure();
    abstract public void onProgress(int mPercentage);
}
