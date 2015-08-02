package com.dubmania.dubsmania.Adapters;

/**
 * Created by rat on 7/30/2015.
 */
public class VideoBoardListItem {
    private String name;
    private String user;
    private int icon;

    public VideoBoardListItem(String name, String user, int icon) {
        this.name = name;
        this.user = user;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return "Uploaded by " + user;
    }

    public int getIcon() {
        return icon;
    }
}
