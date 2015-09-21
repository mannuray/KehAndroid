package com.dubmania.vidcraft.communicator.eventbus.feedbackevent;

/**
 * Created by rat on 9/9/2015.
 */
public class FragmentFeedbackCreateEvent {
    private Long mVideoId;

    public FragmentFeedbackCreateEvent(Long mVideoId) {
        this.mVideoId = mVideoId;
    }

    public Long getVideoId() {
        return mVideoId;
    }
}
