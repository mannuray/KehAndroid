package com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent;

/**
 * Created by rat on 8/7/2015.
 */
public class SetDobEvent {
    private String Dob;

    public SetDobEvent(String dob) {
        Dob = dob;
    }

    public String getDob() {
        return Dob;
    }
}
