package com.dubmania.dubsmania.communicator.eventbus.miscevent;

/**
 * Created by rat on 8/2/2015.
 */
public class VideoItemMenuEvent {
    private Long mId;

    public VideoItemMenuEvent(Long mId) {
        this.mId = mId;
    }

    public Long getId() {
        return mId;
    }
}
