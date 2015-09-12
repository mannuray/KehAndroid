package com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent;

/**
 * Created by rat on 8/7/2015.
 */
public class LoginEvent {
    private Long mUserId;
    private String mEmail;
    private String mUserName;

    public LoginEvent(Long mUserId, String mEmail, String mUserName) {
        this.mUserId = mUserId;
        this.mEmail = mEmail;
        this.mUserName = mUserName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getUserName() {
        return mUserName;
    }

    public Long getUserId() {
        return mUserId;
    }
}
