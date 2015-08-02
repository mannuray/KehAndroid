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
public class VideoAndBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<VideoListItem> mVideoList;
    private ArrayList<VideoBoardListItem> mVideoBoardList;
    private int numberOfVedios;


    // Provide a suitable constructor (depends on the kind of dataset)
    public VideoAndBoardAdapter(ArrayList<VideoListItem> myVideoList, ArrayList<VideoBoardListItem> myVideoBoardList, int myNumberOfVedios) {
        mVideoList = myVideoList;
        mVideoBoardList = myVideoBoardList;
        numberOfVedios = myNumberOfVedios;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position < numberOfVedios? 0: 1;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v;

        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_item_list_layout, parent, false);
                // set the view's size, margins, paddings and layout parameters
                VideoViewHolder vhv = new VideoViewHolder(v);
                return vhv;
            case 1:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_board_item_list_layout, parent, false);
                // set the view's size, margins, paddings and layout parameters
                VideoBoardViewHolder vhb = new VideoBoardViewHolder(v);
                return vhb;
            default:
                return null;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(getItemViewType(position)) {
            case 0:
                VideoViewHolderFactory.bindViewHolder(mVideoList.get(position), holder, position);
                return;
            case 1:
                VideoBoardViewHolderFactory.bindViewHolder(mVideoBoardList.get(position - numberOfVedios), holder, position);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return (mVideoList.size() + mVideoBoardList.size());
    }
}
