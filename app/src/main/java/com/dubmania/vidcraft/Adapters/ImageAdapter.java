package com.dubmania.vidcraft.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.nio.channels.Selector;
import java.util.ArrayList;
import com.dubmania.vidcraft.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> mThumbIds;
    private ArrayList<String> mThumbColors;


    // Constructor
    public ImageAdapter(Context c, ArrayList<Integer> myThumbIds, ArrayList<String> myColors) {
        mContext = c;
        mThumbIds = myThumbIds;
        mThumbColors= myColors;
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

        GradientDrawable selected = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.rectangular_background);
        selected.setStroke(3, Color.parseColor(mThumbColors.get(position)));
        StateListDrawable background = new StateListDrawable(); // mContext.getResources().getDrawable(R.drawable.add_video_board_selector);
        background.addState(new int[]{android.R.attr.state_activated }, selected);
        imageView.setBackground(background); //mContext.getResources().getDrawable(R.drawable.add_video_board_selector));
        imageView.setImageResource(mThumbIds.get(position));
        return imageView;
    }
}