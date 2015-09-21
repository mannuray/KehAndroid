package com.dubmania.vidcraft.communicator.eventbus.addvideoevent;

/**
 * Created by rat on 8/14/2015.
 */
public class AddVideoEditEvent {
    private Integer mStartPos;
    private Integer mEndPos;

    public AddVideoEditEvent(Integer mStartPos, Integer mEndPos) {
        this.mStartPos = mStartPos;
        this.mEndPos = mEndPos;
    }

    public Integer getStartPos() {
        return mStartPos;
    }

    public Integer getEndPos() {
        return mEndPos;
    }
}
