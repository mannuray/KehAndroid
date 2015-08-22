package com.dubmania.dubsmania.communicator.eventbus.mainevent;

import com.dubmania.dubsmania.Adapters.VideoBoardListItem;

import java.util.ArrayList;

/**
 * Created by rat on 8/2/2015.
 */
public class AddDiscoverBoardListEvent {
    public ArrayList<VideoBoardListItem> mVideoBoardItemList;

    public AddDiscoverBoardListEvent(ArrayList<VideoBoardListItem> mVideoItemList) {
        this.mVideoBoardItemList = mVideoItemList;
    }
}
