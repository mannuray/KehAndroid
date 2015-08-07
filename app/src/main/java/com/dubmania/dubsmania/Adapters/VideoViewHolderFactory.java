package com.dubmania.dubsmania.Adapters;

import android.support.v7.widget.RecyclerView;

import com.dubmania.dubsmania.communicator.eventbus.OnClickListnerEvent;
import com.dubmania.dubsmania.communicator.eventbus.VideoItemMenuEvent;

/**
 * Created by rat on 8/2/2015.
 */
public class VideoViewHolderFactory {
    public static void bindViewHolder(VideoListItem item, RecyclerView.ViewHolder holder, int position) {
        VideoViewHolder videoHolder = (VideoViewHolder) holder;
        videoHolder.mVideoName.setText(item.getName());
        videoHolder.mVideoUserName.setText(item.getUser());
        videoHolder.mFavourite.setIsIndicator(item.isFavourite());

        videoHolder.mMenuIcon.setOnClickListener(new OnClickListnerEvent<VideoItemMenuEvent>(new VideoItemMenuEvent(position)));
        videoHolder.mInfoBoxLayout.setOnClickListener(new OnClickListnerEvent<VideoItemMenuEvent>(new VideoItemMenuEvent(position))); //
    }
}
