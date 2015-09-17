package com.dubmania.vidcraft.communicator.eventbus.addvideoevent;

/**
 * Created by rat on 8/14/2015.
 */
public class AddVideoFinishEvent {
    private String mTitle;
    private Long mLanguage;

    public AddVideoFinishEvent(String title, Long language) {
        mTitle = title;
        mLanguage = language;
    }

    public String getTitle() {
        return mTitle;
    }

    public Long getLanguage() {
        return mLanguage;
    }
}
