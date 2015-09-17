package com.dubmania.vidcraft.communicator.eventbus.loginandsignupevent;

/**
 * Created by rat on 8/7/2015.
 */
public class SignupFragmentSelectEvent {
    private int page;

    public SignupFragmentSelectEvent(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
