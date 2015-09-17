package com.dubmania.vidcraft.communicator.networkcommunicator;

import com.dubmania.vidcraft.Adapters.VideoListItem;

import java.util.ArrayList;

/**
 * Created by rat on 8/8/2015.
 */
public abstract class VideoListDownloaderCallback {

    abstract public void onVideosDownloadSuccess(ArrayList<VideoListItem> videos);
    abstract public void onVideosDownloadFailure();
}
