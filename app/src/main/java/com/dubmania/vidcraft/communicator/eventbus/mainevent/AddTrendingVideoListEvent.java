package com.dubmania.vidcraft.communicator.eventbus.mainevent;

import com.dubmania.vidcraft.Adapters.VideoListItem;

import java.util.ArrayList;

/**
 * Created by rat on 8/2/2015.
 */
public class AddTrendingVideoListEvent {
    public ArrayList<VideoListItem> mVideoItemList;
    private String cursor;

    public AddTrendingVideoListEvent(ArrayList<VideoListItem> mVideoItemList, String cursor) {
        this.mVideoItemList = mVideoItemList;
        this.cursor = cursor;
    }

    public String getCursor() {
        return cursor;
    }
}
