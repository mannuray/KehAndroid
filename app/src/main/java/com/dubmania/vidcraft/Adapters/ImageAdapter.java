package com.dubmania.vidcraft.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

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
            imageView = new SquareImageView(mContext);
        }
        else
        {
            imageView = (SquareImageView) convertView;
        }

        GradientDrawable selected = new GradientDrawable();
        selected.setCornerRadius(3);
        selected.setColor(Color.parseColor("#ffffffff"));
        // seems we cannot get new drawarble, it return the same refrence
        //(GradientDrawable) mContext.getResources().getDrawable(R.drawable.rectangular_background);
        selected.setStroke(3, Color.parseColor(mThumbColors.get(position)));
        StateListDrawable background = new StateListDrawable();
        background.addState(new int[]{android.R.attr.state_activated}, selected);
        imageView.setBackground(background);
        imageView.setImageResource(mThumbIds.get(position));
        return imageView;
    }

    private class SquareImageView extends ImageView {
        public SquareImageView(Context context) {
            super(context);
        }

        public SquareImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
        }
    }
}