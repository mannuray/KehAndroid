package com.dubmania.vidcraft.Adapters;

/**
 * Created by rat on 9/10/2015.
 */
public class ListItem {

    enum ListType {video, board}
    private ListType mType;

    public ListItem(ListType mType) {
        this.mType = mType;
    }

    public ListType getType() {
        return mType;
    }
}
