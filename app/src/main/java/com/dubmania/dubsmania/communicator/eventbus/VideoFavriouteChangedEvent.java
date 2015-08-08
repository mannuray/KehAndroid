package com.dubmania.dubsmania.communicator.eventbus;

/**
 * Created by rat on 8/8/2015.
 */
public class VideoFavriouteChangedEvent {
    private boolean mFavrioute;
    private int position;

    public VideoFavriouteChangedEvent(boolean mFavrioute, int position) {
        this.mFavrioute = mFavrioute;
        this.position = position;
    }

    public boolean ismFavrioute() {
        return mFavrioute;
    }

    public int getPosition() {
        return position;
    }
}
