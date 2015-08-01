package com.dubmania.dubsmania.main;

import java.sql.Time;
import java.util.Date;

/**
 * Created by rat on 8/1/2015.
 */
public class MyVideoListItem {
    private int icon;
    private String mVideoName;
    private String mBoardName;
    private Date mDate;
    private Time mTime;

    public MyVideoListItem(int icon, String mVideoName, String mBoardName, Date mDate, Time mTime) {
        this.icon = icon;
        this.mVideoName = mVideoName;
        this.mBoardName = mBoardName;
        this.mDate = mDate;
        this.mTime = mTime;
    }

    public String getmVideoName() {
        return mVideoName;
    }

    public String getmBoardName() {
        return mBoardName;
    }

    public Date getmDate() {
        return mDate;
    }

    public Time getmTime() {
        return mTime;
    }

    public int getIcon() {
        return icon;
    }
}


