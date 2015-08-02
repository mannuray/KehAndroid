package com.dubmania.dubsmania.communicator;

import com.dubmania.dubsmania.Adapters.VideoListItem;

import java.util.ArrayList;

/**
 * Created by rat on 8/2/2015.
 */
public class AddSearchVideoItemListEvent {
    private ArrayList<VideoListItem> mVideoItemList;

    public AddSearchVideoItemListEvent(ArrayList<VideoListItem> mVideoItemList) {
        this.mVideoItemList = mVideoItemList;
    }
}
