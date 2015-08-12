package com.dubmania.dubsmania.Adapters;

import android.graphics.Bitmap;

/**
 * Created by rat on 8/1/2015.
 */
public class MyVideoListItem {
    private Bitmap icon;
    private String mVideoName;
    private String mBoardName;
    private String mFilePath;
    private String mDate;

    public MyVideoListItem(Bitmap icon, String mVideoName, String mBoardName, String mFilePath, String mDate) {
        this.icon = icon;
        this.mVideoName = mVideoName;
        this.mBoardName = mBoardName;
        this.mFilePath = mFilePath;
        this.mDate = mDate;
    }

    public String getmVideoName() {
        return mVideoName;
    }

    public String getmBoardName() {
        return mBoardName;
    }

    public String getmDate() {
        return mDate;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getFilePath() {
        return mFilePath;
    }
}


