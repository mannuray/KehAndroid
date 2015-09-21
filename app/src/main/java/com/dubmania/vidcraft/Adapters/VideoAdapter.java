package com.dubmania.vidcraft.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dubmania.vidcraft.R;

import java.util.ArrayList;

/**
 * Created by rat on 7/28/2015.
 */
public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<VideoListItem> mDataset;

    public VideoAdapter(ArrayList<VideoListItem> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoViewHolderFactory.bindViewHolder(mDataset.get(position), holder, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
