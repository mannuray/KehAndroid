package com.dubmania.dubsmania.communicator.eventbus.mainevent;

import com.dubmania.dubsmania.Adapters.ListItem;

import java.util.ArrayList;

/**
 * Created by rat on 8/2/2015.
 */
public class AddDiscoverItemListEvent {
    public ArrayList<ListItem> mItemList;

    public AddDiscoverItemListEvent(ArrayList<ListItem> mItemList) {
        this.mItemList = mItemList;
    }
}
