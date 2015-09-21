package com.dubmania.vidcraft.communicator.eventbus.addvideoevent;

import com.dubmania.vidcraft.addvideo.Tag;

import java.util.ArrayList;

/**
 * Created by rat on 8/14/2015.
 */
public class AddVideoInfoEvent {
    private String mFilePath;
    private ArrayList<Tag> mTags;
    private String mTitle;
    private String mLanguage;

    public AddVideoInfoEvent(String mFilePath, ArrayList<Tag> mTags, String mTitle, String mLanguage) {
        this.mFilePath = mFilePath;
        this.mTags = mTags;
        this.mTitle = mTitle;
        this.mLanguage = mLanguage;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public ArrayList<Tag> getTags() {
        return mTags;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLanguage() {
        return mLanguage;
    }
}
