package com.dubmania.vidcraft.communicator.eventbus.miscevent;

/**
 * Created by mannuk on 10/14/15.
 */
public class VideoDeletedEvent {
    private Long mVideoId;

    public VideoDeletedEvent(Long mVideoId) {
        this.mVideoId = mVideoId;
    }

    public Long getmVideoId() {
        return mVideoId;
    }
}
