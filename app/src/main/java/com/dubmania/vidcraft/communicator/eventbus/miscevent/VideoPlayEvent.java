package com.dubmania.vidcraft.communicator.eventbus.miscevent;

/**
 * Created by rat on 8/12/2015.
 */
public class VideoPlayEvent {
    private String mFilePath;

    public VideoPlayEvent(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public String getFilePath() {
        return mFilePath;
    }
}
