package com.dubmania.dubsmania.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.MyVideoItemShareEvent;
import com.dubmania.dubsmania.communicator.eventbus.OnClickListnerEvent;
import com.dubmania.dubsmania.communicator.eventbus.OnTouchListnerEvent;

import java.util.ArrayList;

/**
 * Created by rat on 8/1/2015.
 */
public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.ViewHolder> {
    private ArrayList<MyVideoListItem> mDataset;

    public MyVideoAdapter(ArrayList<MyVideoListItem> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MyVideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_vedio_item_layout, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mImageIcon.setImageBitmap(mDataset.get(position).getIcon());
        holder.mVideoName.setText(mDataset.get(position).getmVideoName());
        holder.mVideoBoardName.setText(mDataset.get(position).getmBoardName());
        holder.mDateTime.setText(mDataset.get(position).getmDate());

        holder.mShare.setOnClickListener(new OnClickListnerEvent<>(new MyVideoItemShareEvent(mDataset.get(position).getFilePath())));
        holder.mImageIcon.setOnTouchListener(new OnTouchListnerEvent<>(new VideoPlayEvent(mDataset.get(position).getFilePath())));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mVideoName;
        public TextView mVideoBoardName;
        public TextView mShare;
        public TextView mDateTime;
        public ImageView mImageIcon;

        public ViewHolder(View v) {
            super(v);
            mImageIcon = (ImageView) v.findViewById(R.id.my_video_thumbnail_image);
            mVideoName = (TextView) v.findViewById(R.id.my_video_name);
            mShare = (TextView) v.findViewById(R.id.my_video_share);
            mVideoBoardName = (TextView) v.findViewById(R.id.my_video_board_name);
            mDateTime = (TextView) v.findViewById(R.id.my_video_date_time);
        }
    }
}
