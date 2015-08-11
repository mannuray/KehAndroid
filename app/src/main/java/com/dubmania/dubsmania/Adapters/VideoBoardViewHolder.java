package com.dubmania.dubsmania.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dubmania.dubsmania.R;

/**
 * Created by rat on 8/2/2015.
 */
public class VideoBoardViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public CardView mCard;
    public TextView mVideoCardName;
    public TextView mVideoCardUserName;
    public ImageView mImageIcon;

    public VideoBoardViewHolder(View v) {
        super(v);
        mCard = (CardView) v.findViewById(R.id.card_view);
        mVideoCardName = (TextView) v.findViewById(R.id.videoCardName);
        mVideoCardUserName = (TextView) v.findViewById(R.id.videoCardUserName);
        mImageIcon = (ImageView) v.findViewById(R.id.video_board_icon);
    }
}
