package com.dubmania.vidcraft.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.addvideo.Tag;

import java.util.ArrayList;

/**
 * Created by rat on 8/14/2015.
 */
public class TagListAdapter extends BaseAdapter {

    protected LayoutInflater layoutInflater;
    private ArrayList<Tag> mDataset;

    public TagListAdapter(Context aContext, ArrayList<Tag> aTable) {
        Context context = aContext;
        mDataset = aTable;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return mDataset.size();
    }

    public Object getItem(int i) {
        return mDataset.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.tag_layout, parent, false);
        }

        TextView lName = (TextView) view.findViewById(R.id.tagName);
        lName.setText(mDataset.get(position).getTag());
        return view;
    }
}