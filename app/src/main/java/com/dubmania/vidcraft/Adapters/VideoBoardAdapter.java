package com.dubmania.vidcraft.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dubmania.vidcraft.R;

import java.util.ArrayList;

/**
 * Created by rat on 7/28/2015.
 */
public class VideoBoardAdapter extends EndlessRecyclerAdapter<VideoBoardListItem> {

    public VideoBoardAdapter(ArrayList<VideoBoardListItem> mDataset, RecyclerView mRecyclerView) {
        super(mDataset, mRecyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        if(viewType == VIEW_ITEM) {
            return new VideoBoardViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_item_list_layout, parent, false));
        }else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_progress_card_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VideoBoardViewHolder){
            Log.i("BORAD", " positino is " + position);
            VideoBoardViewHolderFactory.bindViewHolder(mDataset.get(position), holder);
        }else{
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}