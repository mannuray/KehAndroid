package com.dubmania.vidcraft.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dubmania.vidcraft.R;
import java.util.ArrayList;

/**
 * Created by rat on 7/28/2015.
 */
public class VideoAdapter extends EndlessRecyclerAdapter<VideoListItem> {

    public VideoAdapter(ArrayList<VideoListItem> myDataSet, RecyclerView recyclerView) {
        super(myDataSet, recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        if(viewType==VIEW_ITEM) {
            return new VideoViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_item_list_layout, parent, false));
        }else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_progress_card_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VideoViewHolder){
            VideoViewHolderFactory.bindViewHolder(mDataset.get(position), holder, position);
        }else{
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        super.setOnLoadMoreListener(onLoadMoreListener);
    }
}
