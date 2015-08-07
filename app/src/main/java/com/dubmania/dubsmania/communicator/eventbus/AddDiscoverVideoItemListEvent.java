package com.dubmania.dubsmania.communicator.eventbus;

import com.dubmania.dubsmania.Adapters.VideoListItem;

import java.util.ArrayList;

/**
 * Created by rat on 8/2/2015.
 */
public class AddDiscoverVideoItemListEvent {
    public ArrayList<VideoListItem> mVideoItemList;

    public AddDiscoverVideoItemListEvent(ArrayList<VideoListItem> mVideoItemList) {
        this.mVideoItemList = mVideoItemList;
    }
}
