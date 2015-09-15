package com.dubmania.dubsmania.communicator.eventbus.miscevent;

import android.view.View;

/**
 * Created by rat on 8/2/2015.
 */
public class VideoItemMenuEvent {
    private Long mId;
    private String mTitle;
    private View mView;

    public VideoItemMenuEvent(Long mId, String mTitle, View view) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mView = view;
    }

    public Long getId() {
        return mId;
    }

    public View getView() {
        return mView;
    }

    public String getTitle() {
        return mTitle;
    }
}
