package com.dubmania.dubsmania.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.VideoBoardClickedEvent;

/**
 * Created by rat on 8/2/2015.
 */
public class VideoBoardViewHolderFactory {
    public static void bindViewHolder(VideoBoardListItem mItem, RecyclerView.ViewHolder holder) {
        final VideoBoardListItem item = mItem;
        VideoBoardViewHolder videoBoardHolder = (VideoBoardViewHolder) holder;
        videoBoardHolder.mImageIcon.setImageResource(item.getIcon());
        videoBoardHolder.mVideoCardName.setText(item.getName());
        videoBoardHolder.mVideoCardUserName.setText("Uploaded by " + item.getUser());

        videoBoardHolder.mCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                BusProvider.getInstance().post(new VideoBoardClickedEvent(item.getId(), item.getName(), item.getUser()));
                return false;
            }
        });

    }
}
