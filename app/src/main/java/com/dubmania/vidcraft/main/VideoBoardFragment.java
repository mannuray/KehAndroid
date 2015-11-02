package com.dubmania.vidcraft.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dubmania.vidcraft.Adapters.VideoBoardAdapter;
import com.dubmania.vidcraft.Adapters.VideoBoardListItem;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.AddVideoBoardListEvent;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.VideoBoardScrollEndedEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.OnClickListnerEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoBoardClickedEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoBoardDeletedEvent;
import com.dubmania.vidcraft.misc.AddVideoBoardActivity;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.EmptyRecyclerView;
import com.dubmania.vidcraft.utils.SessionManager;
import com.dubmania.vidcraft.utils.SnackFactory;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;


public class VideoBoardFragment extends Fragment {
    private VideoBoardAdapter mAdapter;
    private ArrayList<VideoBoardListItem> mVideoBoardItemList;
    private boolean mVisibleFirstTime = true;
    CoordinatorLayout mLayoutRoot;
    public static FloatingActionButton actionButton;

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
        mLayoutRoot= (CoordinatorLayout) view.findViewById(R.id.rootLayout);

        TypedArray mBoardIcons = getActivity().getResources()
                .obtainTypedArray(R.array.video_board_fav_icons);

        ImageView imageView=new ImageView(getActivity());
        imageView.setImageResource(R.drawable.ic_plus);

         actionButton = new FloatingActionButton.Builder(getActivity())
                .setContentView(imageView)
                .setBackgroundDrawable(R.drawable.play)
                .build();

        ImageView imageView1=new ImageView(getActivity());
        imageView1.setImageResource(R.drawable.user_icon);

        ImageView imageView2=new ImageView(getActivity());
        imageView2.setImageResource(R.drawable.user_icon);

        ImageView imageView3=new ImageView(getActivity());
        imageView3.setImageResource(R.drawable.user_icon);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());

        SubActionButton button1 = itemBuilder.setContentView(imageView1).build();
        SubActionButton button2 = itemBuilder.setContentView(imageView2).build();
        SubActionButton button3 = itemBuilder.setContentView(imageView3).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .build();


        mBoardIcons.recycle();

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
           // mMyUploads.setVisibility(View.VISIBLE);
           // mMyFavrioutes.setVisibility(View.VISIBLE);
        }

    }

    private void presentShowcaseView(int withDelay) {
        new MaterialShowcaseView.Builder(getActivity())
                .setTarget(actionButton)
                .setContentText("Click this button to get extra menu")
                .setDismissText("GOT IT")
                .setContentTextColor(getResources().getColor(R.color.green_500))
                .setMaskColour(getResources().getColor(R.color.blue_500))
                .setDelay(withDelay) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("12345") // provide a unique ID used to ensure it is only shown once
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String boardName = data.getStringExtra(ConstantsStore.INTENT_BOARD_NAME);
            Long id = data.getLongExtra(ConstantsStore.INTENT_BOARD_ID, 0);
            int iconId = data.getIntExtra(ConstantsStore.INTENT_BOARD_ICON, 0);
            mVideoBoardItemList.add(new VideoBoardListItem(id, boardName, new SessionManager(this.getActivity()).getUser(), iconId));
            mAdapter.notifyDataSetChanged();
            SnackFactory.createSnackbar(
                    getActivity(),
                    mLayoutRoot,
                    "Unable to add videoboard due to unknown error"
            ).show();

            //SnackFactory.getSnack(getActivity().findViewById(android.R.id.content), "Unable to add videoboard due to unknown error").show();
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
            presentShowcaseView(1000);
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
                SnackFactory.createSnackbar(
                        getActivity(),
                        mLayoutRoot,
                        "Videoboard: " + mVideoBoardItemList.get(i).getName() + " deleted"
                ).show();
               // SnackFactory.getSnack(getActivity().findViewById(android.R.id.content), "Videoboard: " + mVideoBoardItemList.get(i).getName() + " deleted").show();
                mVideoBoardItemList.remove(i);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
