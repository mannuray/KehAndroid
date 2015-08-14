package com.dubmania.dubsmania.communicator.networkcommunicator;

import com.dubmania.dubsmania.addvideo.Tag;

import java.util.ArrayList;

/**
 * Created by rat on 8/14/2015.
 */
public abstract class TagsDownloaderCallback {
    abstract public void onTagsDownloadSuccess(ArrayList<Tag> mTags);
    abstract public void onTagsDownloadFailure();
}
