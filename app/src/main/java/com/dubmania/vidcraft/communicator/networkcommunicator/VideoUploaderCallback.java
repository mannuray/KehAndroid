package com.dubmania.vidcraft.communicator.networkcommunicator;

/**
 * Created by rat on 8/17/2015.
 */
public abstract class VideoUploaderCallback {
    abstract public void onVideosUploadSuccess(long mId);
    abstract public void onVideosUploadFailure();
}
