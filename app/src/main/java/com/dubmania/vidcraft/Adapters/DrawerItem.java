package com.dubmania.vidcraft.Adapters;

/**
 * Created by rat on 7/26/2015.
 */

public class DrawerItem {
    private String title;
    private int icon;

    public DrawerItem() {
    }

    public DrawerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
