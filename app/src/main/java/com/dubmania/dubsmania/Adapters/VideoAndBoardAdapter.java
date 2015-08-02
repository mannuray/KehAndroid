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
        switch (viewType) {
            case 0:
                View vv = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_item_list_layout, parent, false);
                // set the view's size, margins, paddings and layout parameters
                VideoViewHolder vhv = new VideoViewHolder(vv);
                return vhv;
            case 1:
                View vb = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_board_item_list_layout, parent, false);
                // set the view's size, margins, paddings and layout parameters
                VideoBoardViewHolder vhb = new VideoBoardViewHolder(vb);
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
                VideoViewHolder videoHolder = (VideoViewHolder) holder;
                videoHolder.mVideoName.setText(mVideoList.get(position).getName());
                videoHolder.mVideoUserName.setText(mVideoList.get(position).getUser());
                videoHolder.mFavourite.setIsIndicator(mVideoList.get(position).isFavourite());
                videoHolder.mMenuIcon.setOnClickListener(new ViewItemOnClickListner<VideoItemMenuEvent>(new VideoItemMenuEvent(position)));
                return;
            case 1:
                VideoBoardViewHolder videoBoardHolder = (VideoBoardViewHolder) holder;
                videoBoardHolder.mImageIcon.setImageResource(mVideoBoardList.get(position - numberOfVedios).getIcon());
                videoBoardHolder.mVideoCardName.setText(mVideoBoardList.get(position - numberOfVedios).getName());
                videoBoardHolder.mVideoCardUserName.setText(mVideoBoardList.get(position - numberOfVedios).getUser());
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return (mVideoList.size() + mVideoBoardList.size());
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mVideoName;
        public TextView mVideoUserName;
        public ImageView mVideoThumbnail;
        public ImageView mMenuIcon;
        public RatingBar mFavourite;

        public VideoViewHolder(View v) {
            super(v);
            mVideoName = (TextView) v.findViewById(R.id.videoName);
            mVideoUserName = (TextView) v.findViewById(R.id.videoUserName);
            mVideoThumbnail = (ImageView) v.findViewById(R.id.videoThumbnailImage);
            mVideoThumbnail.setImageResource(R.drawable.ic_video_play_button);
            mMenuIcon = (ImageView) v.findViewById(R.id.video_menu_icon);
            mMenuIcon.setImageResource(R.drawable.ic_video_play_button);
            mFavourite = (RatingBar) v.findViewById(R.id.favourite);

        }
    }

    public static class VideoBoardViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mVideoCardName;
        public TextView mVideoCardUserName;
        public ImageView mImageIcon;

        public VideoBoardViewHolder(View v) {
            super(v);
            mVideoCardName = (TextView) v.findViewById(R.id.videoCardName);
            mVideoCardUserName = (TextView) v.findViewById(R.id.videoCardUserName);
            mImageIcon = (ImageView) v.findViewById(R.id.video_board_icon);
        }
    }
}
