package com.dubmania.dubsmania.utils;

import io.realm.RealmObject;

/**
 * Created by rat on 8/12/2015.
 */
public class SavedDubsData extends RealmObject {
    private String filePath;
    private String title;
    private String creationDate;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String mFilePath) {
        this.filePath = mFilePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String mCreationDate) {
        this.creationDate = mCreationDate;
    }
}
