package com.dubmania.dubsmania.communicator.eventbus;

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
