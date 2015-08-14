package com.dubmania.dubsmania.communicator.eventbus.addvideoevent;

/**
 * Created by rat on 8/5/2015.
 */
public class SearchVideoItemListEvent {
    private String mFilePath;

    public SearchVideoItemListEvent(String uri) {
        this.mFilePath = uri;
    }

    public String getFilePath() {
        return mFilePath;
    }
}
