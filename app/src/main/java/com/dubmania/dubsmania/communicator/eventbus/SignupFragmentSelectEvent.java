package com.dubmania.dubsmania.communicator.eventbus;

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
