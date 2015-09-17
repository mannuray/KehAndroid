package com.dubmania.vidcraft.communicator.eventbus.createdubevent;

/**
 * Created by rat on 8/14/2015.
 */
public class CreateDubShareEvent {
    private int mAppId;

    public CreateDubShareEvent(int appId) {
        mAppId = appId;
    }

    public int getAppId() {
        return mAppId;
    }
}
