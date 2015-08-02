package com.dubmania.dubsmania.Adapters;

import java.util.GregorianCalendar;

/**
 * Created by rat on 8/1/2015.
 */
public class MyVideoListItem {
    private int icon;
    private String mVideoName;
    private String mBoardName;
    private GregorianCalendar mDate;

    public MyVideoListItem(int icon, String mVideoName, String mBoardName, GregorianCalendar mDate) {
        this.icon = icon;
        this.mVideoName = mVideoName;
        this.mBoardName = mBoardName;
        this.mDate = mDate;
    }

    public String getmVideoName() {
        return mVideoName;
    }

    public String getmBoardName() {
        return mBoardName;
    }

    public GregorianCalendar getmDate() {
        return mDate;
    }

    public int getIcon() {
        return icon;
    }
}


