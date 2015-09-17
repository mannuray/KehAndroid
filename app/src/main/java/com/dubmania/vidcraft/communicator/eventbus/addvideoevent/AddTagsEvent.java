package com.dubmania.vidcraft.communicator.eventbus.addvideoevent;

import com.dubmania.vidcraft.addvideo.Tag;

import java.util.ArrayList;

/**
 * Created by rat on 8/14/2015.
 */
public class AddTagsEvent {
    private ArrayList<Tag> mTags;

    public AddTagsEvent(ArrayList<Tag> tags) {
        mTags = tags;
    }

    public ArrayList<Tag> getTags() {
        return mTags;
    }
}
