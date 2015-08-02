package com.dubmania.dubsmania.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.VideoItemMenuEvent;
import com.dubmania.dubsmania.misc.ViewItemOnClickListner;

import java.util.ArrayList;

/**
 * Created by rat on 7/28/2015.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private ArrayList<VideoListItem> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public VideoAdapter(ArrayList<VideoListItem> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item_list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mVideoName.setText(mDataset.get(position).getName());
        holder.mVideoUserName.setText(mDataset.get(position).getUser());
        holder.mFavourite.setIsIndicator(mDataset.get(position).isFavourite());

        holder.mMenuIcon.setOnClickListener(new ViewItemOnClickListner<VideoItemMenuEvent>(new VideoItemMenuEvent(position)));
        //holder.mVideoThumbnail.`(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mVideoName;
        public TextView mVideoUserName;
        public ImageView mVideoThumbnail;
        public ImageView mMenuIcon;
        public RatingBar mFavourite;

        public ViewHolder(View v) {
            super(v);
            mVideoName = (TextView) v.findViewById(R.id.videoName);
            mVideoUserName = (TextView) v.findViewById(R.id.videoUserName);
            mVideoThumbnail = (ImageView) v.findViewById(R.id.videoThumbnailImage);
            mVideoThumbnail.setImageResource(R.drawable.ic_video_play_button);
            mMenuIcon = (ImageView) v.findViewById(R.id.video_menu_icon);
            mVideoThumbnail.setImageResource(R.drawable.ic_video_play_button);
            mFavourite = (RatingBar) v.findViewById(R.id.favourite);

        }
    }
}
