package com.dubmania.vidcraft.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dubmania.vidcraft.Adapters.ListItem;
import com.dubmania.vidcraft.Adapters.VideoAndBoardAdapter;
import com.dubmania.vidcraft.Adapters.VideoBoardListItem;
import com.dubmania.vidcraft.Adapters.VideoListItem;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.AddDiscoverItemListEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.RecyclerViewScrollEndedEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoBoardDeletedEvent;
import com.dubmania.vidcraft.utils.VidCraftApplication;
import com.dubmania.vidcraft.utils.database.VideoBoardRealmObject;
import com.dubmania.vidcraft.utils.database.VideoRealmObject;
import com.loopj.android.http.Base64;
import com.squareup.otto.Subscribe;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

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
        Boolean discoverListAvailable = prefs.getBoolean("discover_list_available", false);

        if(savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mItemList = savedInstanceState.getParcelableArrayList("discover_list");
            mVisibleFirstTime = false;
        }
        else if (!discoverListAvailable) {
            mItemList = new ArrayList<>();
            mItemList.add(null);
        }
        else
        {
            mItemList = new ArrayList<>();
            Realm realm = Realm.getInstance(VidCraftApplication.getContext());
            RealmResults<VideoRealmObject> videos = realm.allObjects(VideoRealmObject.class).where().findAll();
            for(VideoRealmObject video: videos) {
                String thumbnailString = video.getThumbnail();
                byte[] thumbnailByte = Base64.decode(thumbnailString, 0);
                Bitmap thumbnail = BitmapFactory.decodeByteArray(thumbnailByte, 0, thumbnailByte.length);
                mItemList.add(new VideoListItem(video.getId(), video.getName(), video.getUser(), video.isFavourite(), thumbnail));
                mVisibleFirstTime = false;
            }

            RealmResults<VideoBoardRealmObject> boards = realm.allObjects(VideoBoardRealmObject.class).where().findAll();
            for(VideoBoardRealmObject board: boards) {
                mItemList.add(new VideoBoardListItem(board.getId(), board.getName(), board.getUser(), board.getIcon()));
                mVisibleFirstTime = false;
            }

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

        if(mVisibleFirstTime)
            BusProvider.getInstance().post(new RecyclerViewScrollEndedEvent(0, 0));

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

    @Subscribe
    public void onAddDiscoverVideoItemListEvent(final AddDiscoverItemListEvent event) {
        new Thread() {
            @Override
            public void run() {
                Realm realm = Realm.getInstance(VidCraftApplication.getContext());
                realm.beginTransaction();
                for(int i = 0; i < event.mItemList.size(); i++) {
                    ListItem item = event.mItemList.get(i);
                    if(item.getType() == ListItem.ListType.video) {
                        VideoRealmObject video = realm.createObject( VideoRealmObject.class );
                        VideoListItem vitem = (VideoListItem) item;
                        video.setId(vitem.getId());
                        video.setName(vitem.getName());
                        video.setUser(vitem.getUser());
                        video.setFavourite(vitem.isFavourite());

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        vitem.getThumbnail().compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] thumbnail = stream.toByteArray();
                        String imageEncoded = Base64.encodeToString(thumbnail, Base64.DEFAULT);
                        video.setThumbnail(imageEncoded);
                    }
                    else if(item.getType() == ListItem.ListType.board) {
                        VideoBoardRealmObject board = realm.createObject( VideoBoardRealmObject.class );
                        VideoBoardListItem vBoard = (VideoBoardListItem) item;
                        board.setId(vBoard.getId());
                        board.setName(vBoard.getName());
                        board.setUser(vBoard.getUser());
                        board.setIcon(vBoard.getIcon());
                    }

                }
                realm.commitTransaction();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                prefs.edit().putBoolean("discover_list_available", true).apply();

            }
        }.start();

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
