package com.dubmania.dubsmania.communicator.eventbus;

/**
 * Created by rat on 8/2/2015.
 */
public class MyVideoItemShareEvent {
    private String mFilePath;

    public MyVideoItemShareEvent(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public String getFilePath() {
        return mFilePath;
    }
}
