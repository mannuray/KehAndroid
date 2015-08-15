package com.dubmania.dubsmania.communicator.eventbus.mainevent;

import com.dubmania.dubsmania.Adapters.VideoBoardListItem;

import java.util.ArrayList;

/**
 * Created by rat on 8/2/2015.
 */
public class AddVideoBoardListEvent {
    private ArrayList<VideoBoardListItem> mVideoBoard;

    public AddVideoBoardListEvent(ArrayList<VideoBoardListItem> mVideoBoard) {
        this.mVideoBoard = mVideoBoard;
    }

    public ArrayList<VideoBoardListItem> getVideoBoard() {
        return mVideoBoard;
    }
}
