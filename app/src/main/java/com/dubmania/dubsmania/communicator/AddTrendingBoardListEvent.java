package com.dubmania.dubsmania.communicator;

import com.dubmania.dubsmania.Adapters.VideoBoardListItem;

import java.util.ArrayList;

/**
 * Created by rat on 8/2/2015.
 */
public class AddTrendingBoardListEvent {
    public ArrayList<VideoBoardListItem> mVideoBoardItemList;

    public AddTrendingBoardListEvent(ArrayList<VideoBoardListItem> mVideoItemList) {
        this.mVideoBoardItemList = mVideoItemList;
    }
}
