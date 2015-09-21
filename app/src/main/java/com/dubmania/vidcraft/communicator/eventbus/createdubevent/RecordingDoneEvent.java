package com.dubmania.vidcraft.communicator.eventbus.createdubevent;

import java.io.File;

/**
 * Created by rat on 8/26/2015.
 */
public class RecordingDoneEvent {
    private File mAudioFile;

    public RecordingDoneEvent(File mAudioFlileList) {
        this.mAudioFile = mAudioFlileList;
    }

    public File getAudioFile() {
        return mAudioFile;
    }
}
