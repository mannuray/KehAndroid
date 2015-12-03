package com.dubmania.vidcraft.Adapters;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.SearchVideoItemListEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.OnClickListnerEvent;

import java.util.ArrayList;

/**
 * Created by rat on 8/5/2015.
 */
public class ImportVideoAdapter extends RecyclerView.Adapter<ImportVideoAdapter.ViewHolder> {
    private ArrayList<ImportVideoListItem> allObjects;
    private ArrayList<ImportVideoListItem> visibleObjects;

    public ImportVideoAdapter(ArrayList<ImportVideoListItem> myDataset) {
        allObjects = myDataset;
        visibleObjects = new ArrayList<>();
        visibleObjects.addAll(allObjects);
    }

    @Override
    public ImportVideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.import_video_item_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(visibleObjects.get(position).mFilePath, MediaStore.Video.Thumbnails.MICRO_KIND);
        holder.mImageIcon.setImageBitmap(bitmap);
        holder.mVideoName.setText(visibleObjects.get(position).mTitle);
        holder.mArtist.setText(visibleObjects.get(position).mArtist);

        //Log.i("File ",visibleObjects.get(position).mFilePath);
        holder.mCardView.setOnClickListener(new OnClickListnerEvent<>(new SearchVideoItemListEvent(visibleObjects.get(position).mFilePath)));

    }

    @Override
    public int getItemCount() {
        return visibleObjects.size();
    }

    public void flushFilter(){
        visibleObjects = new ArrayList<>();
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
        public CardView mCardView;
        public TextView mVideoName;
        public TextView mArtist;
        public ImageView mImageIcon;

        public ViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mImageIcon = (ImageView) v.findViewById(R.id.import_video_icon);
            mVideoName = (TextView) v.findViewById(R.id.import_video_name);
            mArtist = (TextView) v.findViewById(R.id.import_video_uri);
        }
    }
}
