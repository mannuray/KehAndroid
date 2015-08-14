package com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent;

/**
 * Created by rat on 8/7/2015.
 */
public class UserNameExistEvent {
    private boolean mUserNameExist;

    public UserNameExistEvent(boolean mUserNameExist) {
        this.mUserNameExist = mUserNameExist;
    }

    public boolean isUserNameExist() {
        return mUserNameExist;
    }
}
