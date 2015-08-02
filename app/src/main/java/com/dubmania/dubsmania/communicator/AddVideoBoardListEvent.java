package com.dubmania.dubsmania.communicator;

import com.dubmania.dubsmania.Adapters.VideoBoardListItem;

import java.util.ArrayList;

/**
 * Created by rat on 8/2/2015.
 */
public class AddVideoBoardListEvent {
    public ArrayList<VideoBoardListItem> mVideoItemList;

    public AddVideoBoardListEvent(ArrayList<VideoBoardListItem> mVideoItemList) {
        this.mVideoItemList = mVideoItemList;
    }
}
