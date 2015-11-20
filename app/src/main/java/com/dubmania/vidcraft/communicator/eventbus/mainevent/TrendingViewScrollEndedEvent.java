package com.dubmania.vidcraft.communicator.eventbus.mainevent;

/**
 * Created by rat on 8/8/2015.
 */
public class TrendingViewScrollEndedEvent {
    private int mId;
    private String cursor;

    public TrendingViewScrollEndedEvent(int mId, String cursor) {
        this.mId = mId;
        this.cursor = cursor;
    }

    public int getmId() {
        return mId;
    }

    public String getCursor() {
        return cursor;
    }
}
