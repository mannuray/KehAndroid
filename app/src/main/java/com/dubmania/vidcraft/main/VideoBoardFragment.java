package com.dubmania.vidcraft.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dubmania.vidcraft.Adapters.VideoBoardAdapter;
import com.dubmania.vidcraft.Adapters.VideoBoardListItem;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.AddVideoBoardListEvent;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.VideoBoardScrollEndedEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoBoardDeletedEvent;
import com.dubmania.vidcraft.misc.AddVideoBoardActivity;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.EmptyRecyclerView;
import com.dubmania.vidcraft.utils.SessionManager;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Arrays;


public class VideoBoardFragment extends Fragment {
    private VideoBoardAdapter mAdapter;
    private ArrayList<VideoBoardListItem> mVideoBoardItemList;
    private boolean mVisibleFirstTime = true;
    private FloatingActionButton fab_btn1,fab_btn2;

    public VideoBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mVideoBoardItemList = savedInstanceState.getParcelableArrayList("board_list");
            mVisibleFirstTime = false;

        }
        else {
            mVideoBoardItemList = new ArrayList<>();
            TypedArray mBoardIcons = getResources()
                    .obtainTypedArray(R.array.video_board_icons);


            mVideoBoardItemList = new ArrayList<>();
            mVideoBoardItemList.add(null);
            mBoardIcons.recycle();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_video_board, container, false);
        CoordinatorLayout rootLayout=(CoordinatorLayout)view.findViewById(R.id.rootLayout);
        FloatingActionButton mAddBoardButton = (FloatingActionButton) view.findViewById(R.id.fabBtn);
        fab_btn1= (FloatingActionButton) view.findViewById(R.id.fabBtn1);
        fab_btn2= (FloatingActionButton) view.findViewById(R.id.fabBtn2);

        final Fragment f = this;
        mAddBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddVideoBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                f.startActivityForResult(intent, 2);
            }
        });

        EmptyRecyclerView mRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.video_board_recycler_view);
        mRecyclerView.setEmptyView(view.findViewById(R.id.list_empty));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new VideoBoardAdapter(mVideoBoardItemList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        initData();
        return view;
    }

    private void initData(){
        SessionManager manager = new SessionManager(getActivity());
        if(manager.isLoggedIn()) {
            fab_btn1.setVisibility(View.VISIBLE);
            fab_btn2.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Board", "Add video booard result recived ouoeu ");
        if (resultCode == Activity.RESULT_OK) {
            String boardName = data.getStringExtra(ConstantsStore.INTENT_BOARD_NAME);
            Long id = data.getLongExtra(ConstantsStore.INTENT_BOARD_ID, 0);
            int iconId = data.getIntExtra(ConstantsStore.INTENT_BOARD_ICON, 0);
            mVideoBoardItemList.add(new VideoBoardListItem(id, boardName, new SessionManager(this.getActivity()).getUser(), iconId));
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("board_list", mVideoBoardItemList);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mVisibleFirstTime) {
            BusProvider.getInstance().post(new VideoBoardScrollEndedEvent(0, 0));
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
    public void onAddVideoBoardListEvent(AddVideoBoardListEvent event) {
        mAdapter.addData(event.getVideoBoard());
    }

    @Subscribe
    public void onVideoBoardDeletedEvent(VideoBoardDeletedEvent event) {
        for(int i = 0; i < mVideoBoardItemList.size(); i++) {
            if(mVideoBoardItemList.get(i).getId().equals(event.getBoardId())) {
                mVideoBoardItemList.remove(i);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
