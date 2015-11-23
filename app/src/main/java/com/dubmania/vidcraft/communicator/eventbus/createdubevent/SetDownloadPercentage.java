package com.dubmania.vidcraft.communicator.eventbus.createdubevent;

/**
 * Created by rat on 11/23/2015.
 */
public class SetDownloadPercentage {
    private int mPercentage;

    public SetDownloadPercentage(int mPercentage) {
        this.mPercentage = mPercentage;
    }

    public int getPercentage() {
        return mPercentage;
    }
}
