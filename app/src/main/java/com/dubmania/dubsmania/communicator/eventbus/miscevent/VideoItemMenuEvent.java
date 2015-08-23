package com.dubmania.dubsmania.communicator.eventbus.miscevent;

import android.view.View;

/**
 * Created by rat on 8/2/2015.
 */
public class VideoItemMenuEvent {
    private Long mId;
    private View mView;

    public VideoItemMenuEvent(Long mId, View view) {
        this.mId = mId;
        this.mView = view;
    }

    public Long getId() {
        return mId;
    }

    public View getView() {
        return mView;
    }
}
