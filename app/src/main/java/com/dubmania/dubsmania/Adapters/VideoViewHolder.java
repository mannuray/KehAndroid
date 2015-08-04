package com.dubmania.dubsmania.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dubmania.dubsmania.R;

/**
 * Created by rat on 8/2/2015.
 */
public class VideoViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView mVideoName;
    public TextView mVideoUserName;
    public ImageView mVideoThumbnail;
    public ImageView mMenuIcon;
    public RatingBar mFavourite;
    public View mInfoBoxLayout;

    public VideoViewHolder(View v) {
        super(v);
        mVideoName = (TextView) v.findViewById(R.id.videoName);
        mVideoUserName = (TextView) v.findViewById(R.id.videoUserName);
        mVideoThumbnail = (ImageView) v.findViewById(R.id.videoThumbnailImage);
        mVideoThumbnail.setImageResource(R.drawable.ic_video_play_button);
        mMenuIcon = (ImageView) v.findViewById(R.id.video_menu_icon);
        mMenuIcon.setImageResource(R.drawable.ic_video_play_button);
        mFavourite = (RatingBar) v.findViewById(R.id.favourite);
        mInfoBoxLayout = (View) v.findViewById(R.id.discover_info_box);

    }
}
