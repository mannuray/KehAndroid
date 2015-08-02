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

import com.dubmania.dubsmania.Adapters.VideoAdapter;
import com.dubmania.dubsmania.Adapters.VideoListItem;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.AddDiscoverVideoItemListEvent;
import com.dubmania.dubsmania.communicator.BusProvider;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class DiscoverFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<VideoListItem> mVideoItemList;

    // TO Do remove it after experimenth
    private TypedArray navMenuIcons;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DiscoverFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_discover, container, false);
        final FragmentActivity c = getActivity();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.discover_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        mRecyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mVideoItemList = new ArrayList<VideoListItem>(Arrays.asList(
                new VideoListItem("heros", "mannu", false),
                new VideoListItem("heros1", "mannu", false),
                new VideoListItem("heros2", "mannu", false),
                new VideoListItem("heros3", "prashant", false)
        ));


        mAdapter = new VideoAdapter(mVideoItemList);
        mRecyclerView.setAdapter(mAdapter);
        return view;

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
    public void onAddDiscoverVideoItemListEvent(AddDiscoverVideoItemListEvent event) {
        mVideoItemList.addAll(event.mVideoItemList);
        mAdapter.notifyDataSetChanged();
    }
}
