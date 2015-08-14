package com.dubmania.dubsmania.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dubmania.dubsmania.Adapters.VideoBoardAdapter;
import com.dubmania.dubsmania.Adapters.VideoBoardListItem;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.AddVideoBoardListEvent;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.misc.AddVideoBoardActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

public class VideoBoardFragment extends Fragment {
    private RecyclerView.Adapter mAdapter;
    private ArrayList<VideoBoardListItem> mVideoBoardItemList;

    public VideoBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_video_board, container, false);
        ImageButton mAddBoardButton = (ImageButton) view.findViewById(R.id.add_video_board_button);
        mAddBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddVideoBoardActivity.class);
                startActivity(intent);
            }
        });
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.video_board_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // specify an adapter (see also next example)
        TypedArray mBoardIcons = getResources()
                .obtainTypedArray(R.array.video_board_icons);

        mVideoBoardItemList = new ArrayList<>((Arrays.asList(
                new VideoBoardListItem((long) 1, "My Sounds", "me", mBoardIcons.getResourceId(0, -1)),
                new VideoBoardListItem((long) 2, "My Favorites", "me", mBoardIcons.getResourceId(1, -1))
        )));
        mBoardIcons.recycle();
        mAdapter = new VideoBoardAdapter(mVideoBoardItemList);
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
    public void onAddVideoBoardListEvent(AddVideoBoardListEvent event) {
        mVideoBoardItemList.add(event.getVideoBoard());
        mAdapter.notifyDataSetChanged();
    }
}
