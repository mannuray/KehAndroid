package com.dubmania.dubsmania.communicator.eventbus;

/**
 * Created by rat on 8/7/2015.
 */
public class SignupPasswordEvent {
    private String mPassword;

    public SignupPasswordEvent(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getPassword() {
        return mPassword;
    }
}
