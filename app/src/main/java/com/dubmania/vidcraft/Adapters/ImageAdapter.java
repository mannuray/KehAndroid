package com.dubmania.vidcraft.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import com.dubmania.vidcraft.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> mThumbIds;


    // Constructor
    public ImageAdapter(Context c, ArrayList<Integer> myThumbIds) {
        mContext = c;
        mThumbIds = myThumbIds;
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setBackground(mContext.getResources().getDrawable(R.drawable.add_video_board_selector));
        imageView.setImageResource(mThumbIds.get(position));
        return imageView;
    }
}