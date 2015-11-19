package com.dubmania.vidcraft.utils.database;

import io.realm.RealmObject;

/**
 * Created by mannuk on 11/19/15.
 */
public class VideoBoardRealmObject extends RealmObject {
    private long id;
    private String name;
    private String user;
    private int icon;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
