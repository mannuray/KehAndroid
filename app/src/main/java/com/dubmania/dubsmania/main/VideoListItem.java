package com.dubmania.dubsmania.main;

/**
 * Created by rat on 7/29/2015.
 */
public class VideoListItem {
    private String name;
    private String user;
    private boolean favourite;

    public VideoListItem(String name, String user, boolean favourite) {
        this.name = name;
        this.user = user;
        this.favourite = favourite;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public boolean isFavourite() {
        return favourite;
    }
}
