package com.dubmania.vidcraft.Adapters;

import android.graphics.Color;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dubmania.vidcraft.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by rat on 7/28/2015.
 */
public class VideoAndBoardAdapter extends EndlessRecyclerAdapter<ListItem> {
    protected int VIEW_VIDO = 1;
    protected int VIEW_BORD = 2;
    protected int VIEW_PROG = 0;

    public VideoAndBoardAdapter(ArrayList<ListItem> mList, RecyclerView mRecyclerView) {
        super(mList, mRecyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        if(mDataset.get(position) == null)
            return VIEW_PROG;
        if(mDataset.get(position).getType() == ListItem.ListType.video)
            return VIEW_VIDO;
        else if(mDataset.get(position).getType() == ListItem.ListType.board)
            return VIEW_BORD;
        return -1;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,  int viewType) {
        switch (viewType) {
            case 1:
                return new VideoViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_item_list_layout, parent, false));
            case 2:
                return new VideoBoardViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_board_item_list_layout, parent, false));
            case 0:
                return new ProgressViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_progress_card_view, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(getItemViewType(position)) {
            case 1:
                VideoViewHolderFactory.bindViewHolder((VideoListItem)mDataset.get(position), holder, position);
                return;
            case 2:
                VideoBoardViewHolderFactory.bindViewHolder((VideoBoardListItem)mDataset.get(position), holder);
                return;
            case 0:
                ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static void setImageColor(ImageView image,int color){
        DrawableCompat.setTint(image.getDrawable(), color);
    }


    public int randomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
