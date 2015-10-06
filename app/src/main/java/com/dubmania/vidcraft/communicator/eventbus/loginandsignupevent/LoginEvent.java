package com.dubmania.vidcraft.communicator.eventbus.loginandsignupevent;

/**
 * Created by rat on 8/7/2015.
 */
public class LoginEvent {
    private Long mUserId;
    private String mUserName;
    private String mEmail;

    public LoginEvent(Long mUserId, String mUserName, String mEmail) {
        this.mUserId = mUserId;
        this.mUserName = mUserName;
        this.mEmail = mEmail;
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
