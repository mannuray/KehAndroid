package com.dubmania.dubsmania.communicator;

/**
 * Created by rat on 8/2/2015.
 */
public class RecyclerViewScrollEndedEvent {
    private int mId;
    private int current_page;

    public RecyclerViewScrollEndedEvent(int mId, int current_page) {
        this.mId = mId;
        this.current_page = current_page;
    }

    public int getmId() {
        return mId;
    }

    public int getCurrent_page() {
        return current_page;
    }
}
