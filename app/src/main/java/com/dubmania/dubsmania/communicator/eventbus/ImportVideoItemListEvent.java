package com.dubmania.dubsmania.communicator.eventbus;

/**
 * Created by rat on 8/5/2015.
 */
public class ImportVideoItemListEvent {
    private String uri;

    public ImportVideoItemListEvent(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
