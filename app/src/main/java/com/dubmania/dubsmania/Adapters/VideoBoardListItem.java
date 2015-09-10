package com.dubmania.dubsmania.Adapters;

/**
 * Created by rat on 7/30/2015.
 */
public class VideoBoardListItem extends ListItem{
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
}
