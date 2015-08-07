package com.dubmania.dubsmania.communicator.eventbus;

/**
 * Created by rat on 7/29/2015.
 */
public class DrawerNavigationItemClickedEvent {
    public int position;

    public DrawerNavigationItemClickedEvent( int position ) {
        this.position = position;
    }
}
