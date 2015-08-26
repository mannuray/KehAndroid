package com.dubmania.dubsmania.communicator.eventbus.createdubevent;

import java.io.File;

/**
 * Created by rat on 8/14/2015.
 */
public class SetRecordFilesEvent {
    private File mVideoFile;

    public SetRecordFilesEvent(File videoFile) {
        mVideoFile = videoFile;
    }

    public File getVideoFile() {
        return mVideoFile;
    }
}
