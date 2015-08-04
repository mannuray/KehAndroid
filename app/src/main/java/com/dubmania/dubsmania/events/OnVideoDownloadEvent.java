package com.dubmania.dubsmania.events;

/**
 * Created by hardik.parekh on 8/3/2015.
 */
public class OnVideoDownloadEvent {

    private String savedLocation;

    public String getSavedLocation() {
        return savedLocation;
    }

    public void setSavedLocation(String location) {
        this.savedLocation = location;
    }

}
