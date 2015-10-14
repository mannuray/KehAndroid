package com.dubmania.vidcraft.communicator.eventbus.miscevent;

/**
 * Created by mannuk on 10/14/15.
 */
public class VideoBoardDeletedEvent {
    private Long mBoardId;

    public VideoBoardDeletedEvent(Long mBoardId) {
        this.mBoardId = mBoardId;
    }

    public Long getBoardId() {
        return mBoardId;
    }
}
