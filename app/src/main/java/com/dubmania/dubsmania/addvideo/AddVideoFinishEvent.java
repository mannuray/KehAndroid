package com.dubmania.dubsmania.addvideo;

/**
 * Created by rat on 8/14/2015.
 */
public class AddVideoFinishEvent {
    private String mTitle;
    private String mLanguage;

    public AddVideoFinishEvent(String title, String language) {
        mTitle = title;
        mLanguage = language;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLanguage() {
        return mLanguage;
    }
}
