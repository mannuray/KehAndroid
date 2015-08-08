package com.dubmania.dubsmania.communicator.networkcommunicator;

import com.dubmania.dubsmania.Adapters.VideoListItem;

import java.util.ArrayList;

/**
 * Created by rat on 8/8/2015.
 */
public abstract class VideoDownloaderCallback {

    abstract public void onVideosDownloadSuccess(ArrayList<VideoListItem> videos);
    abstract public void onVideosDownloadFailure();
}
