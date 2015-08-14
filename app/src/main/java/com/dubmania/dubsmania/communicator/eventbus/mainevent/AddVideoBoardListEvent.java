package com.dubmania.dubsmania.communicator.eventbus.mainevent;

import com.dubmania.dubsmania.Adapters.VideoBoardListItem;

/**
 * Created by rat on 8/2/2015.
 */
public class AddVideoBoardListEvent {
    private VideoBoardListItem mVideoBoard;

    public AddVideoBoardListEvent(VideoBoardListItem mVideoBoard) {
        this.mVideoBoard = mVideoBoard;
    }

    public VideoBoardListItem getVideoBoard() {
        return mVideoBoard;
    }
}
