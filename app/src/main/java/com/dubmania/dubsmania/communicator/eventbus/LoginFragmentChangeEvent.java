package com.dubmania.dubsmania.communicator.eventbus;

/**
 * Created by rat on 8/8/2015.
 */
public class LoginFragmentChangeEvent {
    private int position;

    public LoginFragmentChangeEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
