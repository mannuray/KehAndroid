package com.dubmania.dubsmania.communicator.eventbus.miscevent;

/**
 * Created by rat on 8/9/2015.
 */
public class CreateDubEvent {
    private Long mId;
    private String mTitle;

    public CreateDubEvent(Long mId, String title) {
        this.mId = mId;
        mTitle = title;
    }

    public Long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }
}
