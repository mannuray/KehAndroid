package com.dubmania.vidcraft.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dubmania.vidcraft.R;

import java.util.ArrayList;

/**
 * Created by rat on 7/28/2015.
 */
public class VideoAndBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ListItem> mList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public VideoAndBoardAdapter(ArrayList<ListItem> myList) {
        this.mList = myList;
    }

    @Override
    public int getItemViewType(int position) {
        if(mList.get(position).getType() == ListItem.ListType.video)
            return 0;
        else if(mList.get(position).getType() == ListItem.ListType.board)
            return 1;
        return -1;
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
                VideoViewHolderFactory.bindViewHolder((VideoListItem)mList.get(position), holder, position);
                return;
            case 1:
                VideoBoardViewHolderFactory.bindViewHolder((VideoBoardListItem)mList.get(position), holder);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}