package com.dubmania.dubsmania.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.MyVideoItemShareEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by rat on 8/1/2015.
 */
public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.ViewHolder> {
    private ArrayList<MyVideoListItem> mDataset;
    private SimpleDateFormat fmt;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyVideoAdapter(ArrayList<MyVideoListItem> myDataset) {
        mDataset = myDataset;
        fmt  = new SimpleDateFormat("dd/MM//yyyy  HH:mm");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyVideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_vedio_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        holder.mImageIcon.setImageResource(mDataset.get(position).getIcon());
        holder.mVideoName.setText(mDataset.get(position).getmVideoName());
        holder.mVideoBoardName.setText(mDataset.get(position).getmBoardName());
        holder.mDateTime.setText(fmt.format(mDataset.get(position).getmDate().getTime()));

        holder.mShare.setOnClickListener(new ViewItemOnClickListner<MyVideoItemShareEvent>(new MyVideoItemShareEvent(position)));
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
        public TextView mVideoBoardName;
        public TextView mShare;
        public TextView mDateTime;
        public ImageView mImageIcon;

        public ViewHolder(View v) {
            super(v);
            mImageIcon = (ImageView) v.findViewById(R.id.my_video_thumbnail_image);
            mVideoName = (TextView) v.findViewById(R.id.my_video_name);
            mShare = (TextView) v.findViewById(R.id.my_video_share);
            mVideoBoardName = (TextView) v.findViewById(R.id.my_video_board_name);
            mDateTime = (TextView) v.findViewById(R.id.my_video_date_time);
        }
    }
}
