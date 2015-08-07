package com.dubmania.dubsmania.communicator.eventbus;

/**
 * Created by rat on 8/7/2015.
 */
public class SetUsernameEvent {
    private String mUsername;

    public SetUsernameEvent(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getUsername() {
        return mUsername;
    }
}