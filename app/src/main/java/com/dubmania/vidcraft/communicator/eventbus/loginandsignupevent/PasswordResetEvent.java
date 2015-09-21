package com.dubmania.vidcraft.communicator.eventbus.loginandsignupevent;

/**
 * Created by rat on 8/7/2015.
 */
public class PasswordResetEvent {
    private String mEmail;

    public PasswordResetEvent(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getEmail() {
        return mEmail;
    }
}
