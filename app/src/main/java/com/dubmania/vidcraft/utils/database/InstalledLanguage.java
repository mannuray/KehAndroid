package com.dubmania.vidcraft.utils.database;

import io.realm.RealmObject;

/**
 * Created by rat on 9/25/2015.
 */

// just need to differentiate it from Avalable  languages

public class InstalledLanguage extends RealmObject {
    private long languageId;
    private long countryId;
    private String language;
    private String country;

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
