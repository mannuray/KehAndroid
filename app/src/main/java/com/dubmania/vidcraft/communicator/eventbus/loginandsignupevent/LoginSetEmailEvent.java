package com.dubmania.vidcraft.communicator.eventbus.loginandsignupevent;

/**
 * Created by rat on 8/7/2015.
 */
public class LoginSetEmailEvent {
    private String mEmail;

    public LoginSetEmailEvent(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getEmail() {
        return mEmail;
    }
}
