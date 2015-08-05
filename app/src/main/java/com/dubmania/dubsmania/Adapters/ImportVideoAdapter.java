package com.dubmania.dubsmania.Adapters;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.ImportVideoItemListEvent;

import java.util.ArrayList;

/**
 * Created by rat on 8/5/2015.
 */
public class ImportVideoAdapter extends RecyclerView.Adapter<ImportVideoAdapter.ViewHolder> {
    private ArrayList<ImportVideoListItem> allObjects;
    private ArrayList<ImportVideoListItem> visibleObjects;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ImportVideoAdapter(ArrayList<ImportVideoListItem> myDataset) {
        allObjects = myDataset;
        visibleObjects = new ArrayList<ImportVideoListItem>();
        visibleObjects.addAll(allObjects);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ImportVideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.import_video_item_list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(visibleObjects.get(position).mFilePath, MediaStore.Video.Thumbnails.MICRO_KIND);
        holder.mImageIcon.setImageBitmap(bitmap);
        holder.mVideoName.setText(visibleObjects.get(position).mTitle);
        holder.mPath.setText(visibleObjects.get(position).mFilePath);

        holder.mVideoName.setOnClickListener(new ViewItemOnClickListner<ImportVideoItemListEvent>(new ImportVideoItemListEvent(visibleObjects.get(position).mFilePath)));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return visibleObjects.size();
    }

    public void flushFilter(){
        visibleObjects = new ArrayList<ImportVideoListItem>();
        visibleObjects.addAll(allObjects);
        notifyDataSetChanged();
    }

    public void setFilter(String queryText) {

        visibleObjects = new ArrayList<>();
        //constraint = constraint.toString().toLowerCase();
        for (ImportVideoListItem item: allObjects) {
            if (item.mTitle.toLowerCase().contains(queryText.toLowerCase()))
                visibleObjects.add(item);
        }
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mVideoName;
        public TextView mPath;
        public ImageView mImageIcon;

        public ViewHolder(View v) {
            super(v);
            mImageIcon = (ImageView) v.findViewById(R.id.import_video_icon);
            mVideoName = (TextView) v.findViewById(R.id.import_video_name);
            mPath = (TextView) v.findViewById(R.id.import_video_uri);
        }
    }
}
