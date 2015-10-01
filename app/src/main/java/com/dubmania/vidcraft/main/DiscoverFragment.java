package com.dubmania.vidcraft.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dubmania.vidcraft.Adapters.ListItem;
import com.dubmania.vidcraft.Adapters.VideoAndBoardAdapter;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.AddDiscoverItemListEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.RecyclerViewScrollEndedEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class DiscoverFragment extends Fragment {
    private RecyclerView.Adapter mAdapter;
    private ArrayList<ListItem> mItemList;
    private boolean mVisibleFirstTime = true;
    private ProgressBar spinner;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DiscoverFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mItemList = savedInstanceState.getParcelableArrayList("discover_list");
            mVisibleFirstTime = false;
        }
        else {
            mItemList = new ArrayList<>();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_discover, container, false);
        final FragmentActivity c = getActivity();
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.discover_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        mRecyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)

        mAdapter = new VideoAndBoardAdapter(mItemList);
        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager));
        spinner = (ProgressBar) view.findViewById(R.id.discover_progress_bar);
        spinner.setVisibility(View.VISIBLE);

        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("discover_list", mItemList);
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

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible && mVisibleFirstTime) {
            BusProvider.getInstance().post(new RecyclerViewScrollEndedEvent(0, 0));
            mVisibleFirstTime = false;
        }
    }

    @Subscribe
    public void onAddDiscoverVideoItemListEvent(AddDiscoverItemListEvent event) {
        if(mItemList != null) {
            mItemList.addAll(event.mItemList);
            mAdapter.notifyDataSetChanged();
            spinner.setVisibility(View.GONE);
        }
    }
}