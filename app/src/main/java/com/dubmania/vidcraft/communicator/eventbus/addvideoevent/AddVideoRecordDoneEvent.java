package com.dubmania.vidcraft.communicator.eventbus.addvideoevent;

/**
 * Created by mannuk on 9/16/15.
 */
public class AddVideoRecordDoneEvent {
    private String mFilePath;;

    public AddVideoRecordDoneEvent(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public String getFilePath() {
        return mFilePath;
    }
}
