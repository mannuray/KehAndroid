package com.dubmania.vidcraft.utils;

import io.realm.RealmObject;

/**
 * Created by rat on 8/24/2015.
 */
public class LanguageStore extends RealmObject {
    private long id;
    private String language;
    private boolean supported;
    private boolean installed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isSupported() {
        return supported;
    }

    public void setSupported(boolean supported) {
        this.supported = supported;
    }

    public boolean isInstalled() {
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }
}
