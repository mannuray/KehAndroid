package com.dubmania.vidcraft.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dubmania.vidcraft.Adapters.ListItem;
import com.dubmania.vidcraft.Adapters.VideoAndBoardAdapter;
import com.dubmania.vidcraft.Adapters.VideoBoardListItem;
import com.dubmania.vidcraft.Adapters.VideoListItem;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.AddDiscoverItemListEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.RecyclerViewScrollEndedEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoBoardDeletedEvent;
import com.dubmania.vidcraft.utils.DiscoverVideoLoader;
import com.dubmania.vidcraft.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
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
    private VideoAndBoardAdapter mAdapter;
    private ArrayList<ListItem> mItemList;
    private boolean mVisibleFirstTime = true;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DiscoverFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String savedValue = prefs.getString("discover_list", "");

        if(savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mItemList = savedInstanceState.getParcelableArrayList("discover_list");
            mVisibleFirstTime = false;
        }
        else if (savedValue != null && savedValue.equals("")) {
            mItemList = new ArrayList<>();
            mItemList.add(null);
        }
        else
        {
            mItemList = DiscoverVideoLoader.getListItem();
            mVisibleFirstTime = false;
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

        mAdapter = new VideoAndBoardAdapter(mItemList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

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
        }
    }

    @Subscribe
    public void onAddDiscoverVideoItemListEvent(AddDiscoverItemListEvent event) {
        new Runnable() {

            @Override
            public void run() {
                Gson gson = new GsonBuilder().create();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                prefs.edit().putString("discover_list", gson.toJson(mItemList)).apply();
            }
        }.run();

        if(mItemList != null) {
            mAdapter.addData(event.mItemList);
        }
    }

    @Subscribe
    public void onVideoBoardDeletedEvent(VideoBoardDeletedEvent event) {
        for(int i = 0; i < mItemList.size(); i++) {
            if((mItemList.get(i).getType() == ListItem.ListType.board) && ((VideoBoardListItem)mItemList.get(i)).getId().equals(event.getBoardId())) {
                mItemList.remove(i);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
