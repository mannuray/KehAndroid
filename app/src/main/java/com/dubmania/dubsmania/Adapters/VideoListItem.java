package com.dubmania.dubsmania.Adapters;

import android.graphics.Bitmap;

/**
 * Created by rat on 7/29/2015.
 */
public class VideoListItem {
    private Long id;
    private String name;
    private String user;
    private String desc;
    private boolean favourite;
    private Bitmap thumbnail;

    public VideoListItem(Long id, String name, String user, String desc, boolean favourite) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.desc = desc;
        this.favourite = favourite;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
