package com.dubmania.dubsmania.communicator.eventbus.addvideoevent;

/**
 * Created by rat on 8/14/2015.
 */
public class AddVideoChangeFragmentEvent {
    private int mPosition;

    public AddVideoChangeFragmentEvent(int position) {
        this.mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }
}
