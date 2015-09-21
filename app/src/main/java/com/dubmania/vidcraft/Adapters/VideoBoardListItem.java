package com.dubmania.vidcraft.Adapters;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rat on 7/30/2015.
 */
public class VideoBoardListItem extends ListItem implements Parcelable {
    private Long mId;
    private String name;
    private String user;
    private int icon;

    public VideoBoardListItem(Long mId, String name, String user, int icon) {
        super(ListType.board);
        this.mId = mId;
        this.name = name;
        this.user = user;
        this.icon = icon;
    }

    protected VideoBoardListItem(Parcel in) {
        super(ListType.board);
        name = in.readString();
        user = in.readString();
        icon = in.readInt();
    }

    public static final Creator<VideoBoardListItem> CREATOR = new Creator<VideoBoardListItem>() {
        @Override
        public VideoBoardListItem createFromParcel(Parcel in) {
            return new VideoBoardListItem(in);
        }

        @Override
        public VideoBoardListItem[] newArray(int size) {
            return new VideoBoardListItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public int getIcon() {
        return icon;
    }

    public Long getId() {
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(user);
        dest.writeInt(icon);
    }
}
