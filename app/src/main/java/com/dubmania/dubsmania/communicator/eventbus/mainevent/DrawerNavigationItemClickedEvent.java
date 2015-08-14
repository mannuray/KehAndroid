package com.dubmania.dubsmania.communicator.eventbus.mainevent;

/**
 * Created by rat on 7/29/2015.
 */
public class DrawerNavigationItemClickedEvent {
    public int position;

    public DrawerNavigationItemClickedEvent( int position ) {
        this.position = position;
    }
}
