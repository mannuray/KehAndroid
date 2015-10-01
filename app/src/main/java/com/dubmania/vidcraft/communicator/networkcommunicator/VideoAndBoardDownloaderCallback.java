package com.dubmania.vidcraft.communicator.networkcommunicator;

import com.dubmania.vidcraft.Adapters.ListItem;

import java.util.ArrayList;

/**
 * Created by rat on 9/10/2015.
 */
public abstract class VideoAndBoardDownloaderCallback {
    abstract public void onVideoAndBoardDownloaderSuccess(ArrayList<ListItem> mList);
    abstract public void onVideoAndBoardDownloaderFailure();
}