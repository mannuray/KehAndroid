package com.dubmania.vidcraft.communicator.eventbus.miscevent;

/**
 * Created by rat on 8/11/2015.
 */
public class VideoBoardClickedEvent {
    private Long mId;
    private String mBoardName;
    private String mBoardUsername;

    public VideoBoardClickedEvent(Long mId, String mBoardName, String mBoardUsername) {
        this.mId = mId;
        this.mBoardName = mBoardName;
        this.mBoardUsername = mBoardUsername;
    }

    public Long getId() {
        return mId;
    }

    public String getBoardName() {
        return mBoardName;
    }

    public String getBoardUsername() {
        return mBoardUsername;
    }
}
