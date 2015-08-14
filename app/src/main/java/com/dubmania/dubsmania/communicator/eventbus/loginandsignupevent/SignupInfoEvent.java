package com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent;

/**
 * Created by rat on 8/9/2015.
 */
public class SignupInfoEvent {
    private String mUsername;
    private String mEmail;
    private String mPassword;
    private String mDob;

    public SignupInfoEvent(String mUsername, String mEmail, String mPassword, String mDob) {
        this.mUsername = mUsername;
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mDob = mDob;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getDob() {
        return mDob;
    }
}
