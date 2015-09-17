package com.dubmania.vidcraft.communicator.eventbus.miscevent;

/**
 * Created by rat on 8/11/2015.
 */
public class VideoBoardViewScrollEndedEvent {
    private int mId;
    private int currentPage;

    public VideoBoardViewScrollEndedEvent(int mId, int currentPage) {
        this.mId = mId;
        this.currentPage = currentPage;
    }

    public int getId() {
        return mId;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
