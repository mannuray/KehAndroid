package com.dubmania.vidcraft.Adapters;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rat on 7/29/2015.
 */
public class VideoListItem extends ListItem implements Parcelable {
    private Long id;
    private String name;
    private String user;
    private boolean favourite;
    private Bitmap thumbnail;

    public VideoListItem(Long id, String name, String user, boolean favourite, Bitmap thumbnail) {
        super(ListType.video);
        this.id = id;
        this.name = name;
        this.user = user;
        this.favourite = favourite;
        this.thumbnail = thumbnail;
    }

    protected VideoListItem(Parcel in) {
        super(ListType.video);
        name = in.readString();
        user = in.readString();
        favourite = in.readByte() != 0;
        thumbnail = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<VideoListItem> CREATOR = new Creator<VideoListItem>() {
        @Override
        public VideoListItem createFromParcel(Parcel in) {
            return new VideoListItem(in);
        }

        @Override
        public VideoListItem[] newArray(int size) {
            return new VideoListItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(user);
        dest.writeByte((byte) (favourite ? 1 : 0));
        dest.writeParcelable(thumbnail, flags);
    }
}
