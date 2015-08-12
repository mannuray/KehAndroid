package com.dubmania.dubsmania.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    public VideoAndBoardAdapter(ArrayList<VideoListItem> myVideoList, ArrayList<VideoBoardListItem> myVideoBoardList) {
        mVideoList = myVideoList;
        mVideoBoardList = myVideoBoardList;
        numberOfVedios = 4;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position < numberOfVedios? 0: 1;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,  int viewType) {
        switch (viewType) {
            case 0:
                return new VideoViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_item_list_layout, parent, false));
            case 1:
                return new VideoBoardViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_board_item_list_layout, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(getItemViewType(position)) {
            case 0:
                VideoViewHolderFactory.bindViewHolder(mVideoList.get(position), holder, position);
                return;
            case 1:
                VideoBoardViewHolderFactory.bindViewHolder(mVideoBoardList.get(position - numberOfVedios), holder);
        }
    }

    @Override
    public int getItemCount() {
        return (mVideoList.size() + mVideoBoardList.size());
    }
}
