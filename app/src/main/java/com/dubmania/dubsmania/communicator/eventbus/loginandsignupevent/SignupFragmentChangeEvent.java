package com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent;

/**
 * Created by rat on 8/8/2015.
 */
public class SignupFragmentChangeEvent {
    private int position;

    public SignupFragmentChangeEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
