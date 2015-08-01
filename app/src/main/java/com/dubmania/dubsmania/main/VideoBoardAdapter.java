package com.dubmania.dubsmania.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dubmania.dubsmania.R;

import java.util.ArrayList;

/**
 * Created by rat on 7/28/2015.
 */
public class VideoBoardAdapter extends RecyclerView.Adapter<VideoBoardAdapter.ViewHolder> {
    private ArrayList<VideoBoardListItem> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public VideoBoardAdapter(ArrayList<VideoBoardListItem> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VideoBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_board_item_list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        holder.mVideoCardName.setText(mDataset.get(position).getName());
        holder.mVideoCardUserName.setText(mDataset.get(position).getUser());

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
        public TextView mVideoCardName;
        public TextView mVideoCardUserName;
        public ImageView mImageIcon;

        public ViewHolder(View v) {
            super(v);
            mVideoCardName = (TextView) v.findViewById(R.id.videoCardName);
            mVideoCardUserName = (TextView) v.findViewById(R.id.videoCardUserName);
            mImageIcon = (ImageView) v.findViewById(R.id.video_board_icon);
        }
    }
}