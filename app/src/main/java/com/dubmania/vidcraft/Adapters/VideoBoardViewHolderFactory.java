package com.dubmania.vidcraft.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoBoardClickedEvent;

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

        videoBoardHolder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new VideoBoardClickedEvent(item.getId(), item.getIcon(), item.getName(), item.getUser()));
            }
        });

    }
}
