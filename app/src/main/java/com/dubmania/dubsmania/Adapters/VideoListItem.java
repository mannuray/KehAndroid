package com.dubmania.dubsmania.Adapters;

import android.graphics.Bitmap;

/**
 * Created by rat on 7/29/2015.
 */
public class VideoListItem {
    private Long id;
    private String name;
    private String user;
    private boolean favourite;
    private Bitmap thumbnail;

    public VideoListItem(Long id, String name, String user, boolean favourite, Bitmap thumbnail) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.favourite = favourite;
        this.thumbnail = thumbnail;
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

    public boolean isFavourite() {
        return favourite;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }
}
