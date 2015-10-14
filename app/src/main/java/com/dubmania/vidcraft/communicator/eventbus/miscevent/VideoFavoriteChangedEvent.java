package com.dubmania.vidcraft.communicator.eventbus.miscevent;

/**
 * Created by rat on 8/8/2015.
 */
public class VideoFavoriteChangedEvent {
    private boolean mFavrioute;
    private Long mId;

    public VideoFavoriteChangedEvent(boolean mFavrioute, Long id) {
        this.mFavrioute = mFavrioute;
        this.mId = id;
    }

    public boolean ismFavrioute() {
        return mFavrioute;
    }

    public Long getId() {
        return mId;
    }
}
