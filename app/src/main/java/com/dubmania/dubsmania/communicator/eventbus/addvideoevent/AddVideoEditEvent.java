package com.dubmania.dubsmania.communicator.eventbus.addvideoevent;

/**
 * Created by rat on 8/14/2015.
 */
public class AddVideoEditEvent {
    private String mFilePath;

    public AddVideoEditEvent(String filePath) {
        mFilePath = filePath;
    }

    public String getFilePath() {
        return mFilePath;
    }
}
