package com.dubmania.vidcraft.utils.database;

import android.graphics.Bitmap;

import io.realm.RealmObject;

/**
 * Created by mannuk on 11/19/15.
 */
public class VideoRealmObject extends RealmObject {
    private long id;
    private String name;
    private String user;
    private boolean favourite;
    private String thumbnail;

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

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
