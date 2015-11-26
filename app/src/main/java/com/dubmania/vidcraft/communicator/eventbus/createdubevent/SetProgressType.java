package com.dubmania.vidcraft.communicator.eventbus.createdubevent;

/**
 * Created by mannuk on 11/26/15.
 */
public class SetProgressType {
    private boolean mProgressType;

    public SetProgressType(boolean mProgressType) {
        this.mProgressType = mProgressType;
    }

    public boolean getProgressType() {
        return mProgressType;
    }
}
