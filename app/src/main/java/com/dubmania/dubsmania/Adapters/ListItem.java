package com.dubmania.dubsmania.Adapters;

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
