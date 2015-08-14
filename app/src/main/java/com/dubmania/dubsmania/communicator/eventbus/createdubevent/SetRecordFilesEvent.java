package com.dubmania.dubsmania.communicator.eventbus.createdubevent;

import java.io.File;

/**
 * Created by rat on 8/14/2015.
 */
public class SetRecordFilesEvent {
    private File mAudioFile;
    private File mVideoFile;

    public SetRecordFilesEvent(File audioFile, File videoFile) {
        mAudioFile = audioFile;
        mVideoFile = videoFile;
    }

    public File getAudioFile() {
        return mAudioFile;
    }

    public File getVideoFile() {
        return mVideoFile;
    }
}
