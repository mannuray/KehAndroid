package com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent;

/**
 * Created by rat on 8/7/2015.
 */
public class EmailExistEvent {
    private String mEmail;
    private String mUserName;

    public EmailExistEvent(String mEmail, String mUserName) {
        this.mEmail = mEmail;
        this.mUserName = mUserName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getUserName() {
        return mUserName;
    }
}
