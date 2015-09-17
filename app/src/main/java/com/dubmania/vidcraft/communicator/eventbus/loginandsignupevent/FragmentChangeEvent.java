package com.dubmania.vidcraft.communicator.eventbus.loginandsignupevent;

/**
 * Created by rat on 8/7/2015.
 */
public class FragmentChangeEvent {
    private int position;

    public FragmentChangeEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
