package com.dubmania.dubsmania.communicator.eventbus.miscevent;

/**
 * Created by rat on 8/9/2015.
 */
public class CreateDubEvent {
    private Long mId;

    public CreateDubEvent(Long mId) {
        this.mId = mId;
    }

    public Long getId() {
        return mId;
    }
}
