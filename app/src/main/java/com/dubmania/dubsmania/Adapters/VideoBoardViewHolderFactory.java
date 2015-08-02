package com.dubmania.dubsmania.Adapters;

import android.support.v7.widget.RecyclerView;

/**
 * Created by rat on 8/2/2015.
 */
public class VideoBoardViewHolderFactory {
    public static void bindViewHolder(VideoBoardListItem item, RecyclerView.ViewHolder holder, int position) {
        VideoBoardViewHolder videoBoardHolder = (VideoBoardViewHolder) holder;
        videoBoardHolder.mImageIcon.setImageResource(item.getIcon());
        videoBoardHolder.mVideoCardName.setText(item.getName());
        videoBoardHolder.mVideoCardUserName.setText(item.getUser());

    }
}
