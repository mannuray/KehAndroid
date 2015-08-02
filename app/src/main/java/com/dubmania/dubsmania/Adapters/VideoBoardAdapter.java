package com.dubmania.dubsmania.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dubmania.dubsmania.R;

import java.util.ArrayList;

/**
 * Created by rat on 7/28/2015.
 */
public class VideoBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<VideoBoardListItem> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public VideoBoardAdapter(ArrayList<VideoBoardListItem> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_board_item_list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        VideoBoardViewHolder vh = new VideoBoardViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        VideoBoardViewHolderFactory.bindViewHolder(mDataset.get(position), holder, position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}