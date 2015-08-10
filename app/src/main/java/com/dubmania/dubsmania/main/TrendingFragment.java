package com.dubmania.dubsmania.main;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dubmania.dubsmania.Adapters.VideoAndBoardAdapter;
import com.dubmania.dubsmania.Adapters.VideoBoardListItem;
import com.dubmania.dubsmania.Adapters.VideoListItem;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.AddTrendingBoardListEvent;
import com.dubmania.dubsmania.communicator.eventbus.AddTrendingVideoListEvent;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.TrendingViewScrollEndedEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class TrendingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<VideoListItem> mVideoItemList;
    private ArrayList<VideoBoardListItem> mVideoBoardItemList;
    private boolean mVisibleFirstTime = true;

    // TO Do remove it after experimenth
    private TypedArray navMenuIcons;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrendingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trending, container, false);
        final FragmentActivity c = getActivity();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.trending_recycler_view);
        mLayoutManager = new LinearLayoutManager(c);
        mRecyclerView.setLayoutManager(mLayoutManager);

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mVideoItemList = new ArrayList<VideoListItem>();

        mVideoBoardItemList = new ArrayList<VideoBoardListItem>();
        mAdapter = new VideoAndBoardAdapter(mVideoItemList,mVideoBoardItemList);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible && mVisibleFirstTime) {
            BusProvider.getInstance().post(new TrendingViewScrollEndedEvent(0, 0));
            mVisibleFirstTime = false;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onAddTrendingVideoListEvent(AddTrendingVideoListEvent event) {
        mVideoItemList.addAll(event.mVideoItemList);
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onAddTrendingBoardListEvent(AddTrendingBoardListEvent event) {
        mVideoBoardItemList.addAll(event.mVideoBoardItemList);
        mAdapter.notifyDataSetChanged();
    }
}
