package com.dubmania.vidcraft.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.MyVideoItemShareEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.OnClickListnerEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoPlayEvent;

import java.util.ArrayList;

/**
 * Created by rat on 8/1/2015.
 */
public class MyVideoAdapter extends EndlessRecyclerAdapter<MyVideoListItem> {

    public MyVideoAdapter(ArrayList<MyVideoListItem> mDataset, RecyclerView mRecyclerView) {
        super(mDataset, mRecyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==VIEW_ITEM) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_vedio_item_layout, parent, false));
        }else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_progress_card_view, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.mImageIcon.setImageBitmap(mDataset.get(position).getIcon());
            viewHolder.mVideoName.setText(mDataset.get(position).getmVideoName());
            viewHolder.mDateTime.setText(mDataset.get(position).getmDate());

            viewHolder.mInformationBox.setOnClickListener(new OnClickListnerEvent<>(new MyVideoItemShareEvent(mDataset.get(position).getFilePath())));
            viewHolder.mImageIcon.setOnClickListener(new OnClickListnerEvent<>(new VideoPlayEvent(mDataset.get(position).getFilePath())));
        }else{
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mInformationBox;
        public TextView mVideoName;
        public TextView mShare;
        public TextView mDateTime;
        public ImageView mImageIcon;

        public ViewHolder(View v) {
            super(v);
            mInformationBox = v.findViewById(R.id.informationBox);
            mImageIcon = (ImageView) v.findViewById(R.id.my_video_thumbnail_image);
            mVideoName = (TextView) v.findViewById(R.id.my_video_name);
            mShare = (TextView) v.findViewById(R.id.my_video_share);
            mDateTime = (TextView) v.findViewById(R.id.my_video_date_time);
        }
    }
}
