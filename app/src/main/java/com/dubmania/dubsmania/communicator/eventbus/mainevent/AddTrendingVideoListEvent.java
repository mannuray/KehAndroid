package com.dubmania.dubsmania.communicator.eventbus.mainevent;

import com.dubmania.dubsmania.Adapters.VideoListItem;

import java.util.ArrayList;

/**
 * Created by rat on 8/2/2015.
 */
public class AddTrendingVideoListEvent {
    public ArrayList<VideoListItem> mVideoItemList;

    public AddTrendingVideoListEvent(ArrayList<VideoListItem> mVideoItemList) {
        this.mVideoItemList = mVideoItemList;
    }
}
