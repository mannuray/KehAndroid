package com.dubmania.vidcraft.communicator.networkcommunicator;

import com.dubmania.vidcraft.Adapters.VideoBoardListItem;

import java.util.ArrayList;

/**
 * Created by rat on 8/11/2015.
 */
public abstract class VideoBoardDownloaderCallback {
    abstract public void onVideoBoardsDownloadSuccess(ArrayList<VideoBoardListItem> boards);
    abstract public void onVideosBoardsDownloadFailure();
}
