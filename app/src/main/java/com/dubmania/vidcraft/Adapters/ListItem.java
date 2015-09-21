package com.dubmania.vidcraft.Adapters;

import android.os.Parcelable;

/**
 * Created by rat on 9/10/2015.
 */
public abstract class ListItem implements Parcelable {

    enum ListType {video, board}
    private ListType mType;

    public ListItem(ListType mType) {
        this.mType = mType;
    }

    public ListType getType() {
        return mType;
    }
}
