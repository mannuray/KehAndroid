package com.dubmania.vidcraft.Adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dubmania.vidcraft.R;

import java.util.ArrayList;

/**
 * Created by rat on 10/17/2015.
 */

public class EndlessRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected int VIEW_ITEM = 1;
    protected int VIEW_PROG = 0;

    protected ArrayList<T> mDataset;

    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;
    private OnLoadMoreListener onLoadMoreListener;

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        }
    }

    public EndlessRecyclerAdapter(ArrayList<T> myDataSet, RecyclerView recyclerView) {
        mDataset = myDataSet;

        if(recyclerView.getLayoutManager()instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position)!=null? VIEW_ITEM: VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProgressViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_progress_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
    }

    public void setLoaded(){
        loading = false;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void addData(ArrayList<T> myDataset) {
        if(mDataset.size() > 0 && mDataset.get(mDataset.size() - 1) == null) { // remove progress bar if visible
            mDataset.remove(mDataset.size() - 1);
            this.notifyItemRemoved(mDataset.size());
        }
        mDataset.addAll(myDataset);
        this.notifyDataSetChanged();
        this.setLoaded();
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
}
