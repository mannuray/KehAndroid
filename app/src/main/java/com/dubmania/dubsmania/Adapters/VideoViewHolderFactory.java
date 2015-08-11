package com.dubmania.dubsmania.Adapters;

import android.support.v7.widget.RecyclerView;

import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.CreateDubEvent;
import com.dubmania.dubsmania.communicator.eventbus.OnClickListnerEvent;
import com.dubmania.dubsmania.communicator.eventbus.VideoFavriouteChangedEvent;
import com.dubmania.dubsmania.communicator.eventbus.VideoItemMenuEvent;

/**
 * Created by rat on 8/2/2015.
 */
public class VideoViewHolderFactory {
    public static void bindViewHolder(VideoListItem item, RecyclerView.ViewHolder holder, int position) {
        VideoViewHolder videoHolder = (VideoViewHolder) holder;
        videoHolder.mVideoName.setText(item.getName());
        videoHolder.mVideoUserName.setText(item.getUser());
        videoHolder.mFavourite.setChecked(item.isFavourite());
        videoHolder.mVideoThumbnail.setImageBitmap(item.getThumbnail());

        videoHolder.mMenuIcon.setOnClickListener(new OnClickListnerEvent<>(new VideoItemMenuEvent(position)));
        videoHolder.mInfoBoxLayout.setOnClickListener(new OnClickListnerEvent<>(new CreateDubEvent(item.getId()))); //
        videoHolder.mFavourite.setOnCheckedChangeListener((buttonView, isChecked) -> BusProvider.getInstance().post(new VideoFavriouteChangedEvent(isChecked, item.getId())));
    }
}
