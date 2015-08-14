package com.dubmania.dubsmania.addvideo;

/**
 * Created by rat on 8/14/2015.
 */
public class Tag {
    private Long mId;
    private String mTag;

    public Tag(Long mId, String mTag) {
        this.mId = mId;
        this.mTag = mTag;
    }

    public Long getId() {
        return mId;
    }

    public String getTag() {
        return mTag;
    }
}
