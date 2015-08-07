package com.dubmania.dubsmania.communicator.eventbus;

/**
 * Created by rat on 8/7/2015.
 */
public class FragmentCallbackEvent {
    private int position;

    public FragmentCallbackEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
