package com.dubmania.dubsmania.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dubmania.dubsmania.Adapters.VideoAdapter;
import com.dubmania.dubsmania.Adapters.VideoListItem;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.AddTrendingVideoListEvent;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.TrendingViewScrollEndedEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class TrendingFragment extends Fragment {

    private RecyclerView.Adapter mAdapter;
    private ArrayList<VideoListItem> mVideoItemList;
    private boolean mVisibleFirstTime = true;

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
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.trending_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(c));

        mVideoItemList = new ArrayList<>();

        mAdapter = new VideoAdapter(mVideoItemList);
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
}
