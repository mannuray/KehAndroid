package com.dubmania.vidcraft.communicator.eventbus.loginandsignupevent;

/**
 * Created by rat on 8/7/2015.
 */
public class SetSignupEmailEvent {
    private String email;

    public SetSignupEmailEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
