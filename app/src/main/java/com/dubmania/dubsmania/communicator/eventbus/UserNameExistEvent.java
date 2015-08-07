package com.dubmania.dubsmania.communicator.eventbus;

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
