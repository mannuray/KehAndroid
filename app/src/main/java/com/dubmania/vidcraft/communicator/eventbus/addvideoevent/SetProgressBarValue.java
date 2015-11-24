package com.dubmania.vidcraft.communicator.eventbus.addvideoevent;

/**
 * Created by rat on 11/24/2015.
 */
public class SetProgressBarValue {
    private int mPercentage;

    public SetProgressBarValue(int mPercentage) {
        this.mPercentage = mPercentage;
    }

    public int getPercentage() {
        return mPercentage;
    }
}
