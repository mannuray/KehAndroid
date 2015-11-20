package com.dubmania.vidcraft.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dubmania.vidcraft.Adapters.EndlessRecyclerAdapter;
import com.dubmania.vidcraft.Adapters.VideoAdapter;
import com.dubmania.vidcraft.Adapters.VideoListItem;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.AddTrendingVideoListEvent;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.TrendingViewScrollEndedEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class TrendingFragment extends Fragment {

    private VideoAdapter mAdapter;
    private ArrayList<VideoListItem> mVideoItemList;
    private boolean mVisibleFirstTime = true;
    private String mCursor = "";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrendingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mVideoItemList = savedInstanceState.getParcelableArrayList("trending_list");
            mCursor = savedInstanceState.getString("trending_cursor");
            mVisibleFirstTime = false;
        }
        else {
            mVideoItemList = new ArrayList<>();
            mVideoItemList.add(null); // show the progress bar
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trending, container, false);
        final FragmentActivity c = getActivity();
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.trending_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(c)); // probably we dont need this

        mAdapter = new VideoAdapter(mVideoItemList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new EndlessRecyclerAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add progress item
                if(mCursor.equals("end"))
                    return;

                ArrayList<VideoListItem> a = new ArrayList<>();
                a.add(null);
                mAdapter.addData(a);
                BusProvider.getInstance().post(new TrendingViewScrollEndedEvent(0, mCursor));
            }
        });

        if(mVisibleFirstTime)
            BusProvider.getInstance().post(new TrendingViewScrollEndedEvent(0, mCursor));

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("trending_list", mVideoItemList);
        outState.putString("trending_cursor", mCursor);
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
        mAdapter.addData(event.mVideoItemList);
        mCursor = event.getCursor();
        mVisibleFirstTime = false;
    }
}
