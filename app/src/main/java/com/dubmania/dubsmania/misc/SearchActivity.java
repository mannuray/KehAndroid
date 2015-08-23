package com.dubmania.dubsmania.misc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.dubmania.dubsmania.Adapters.VideoAdapter;
import com.dubmania.dubsmania.Adapters.VideoListItem;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.CreateDubEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.VideoItemMenuEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoListDownloader;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoListDownloaderCallback;
import com.dubmania.dubsmania.createdub.CreateDubActivity;
import com.dubmania.dubsmania.dialogs.VideoItemPopupMenu;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.dubmania.dubsmania.utils.SessionManager;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {
    private ArrayList<VideoListItem> mVideoItemList;
    private EditText mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        // add the custom view to the action bar
        assert actionBar != null;
        actionBar.setCustomView(R.layout.search_layout);
        actionBar.setDisplayShowCustomEnabled(true);
        mSearch = (EditText) actionBar.getCustomView().findViewById(
                R.id.searchfield);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mVideoItemList = new ArrayList<>();
        final RecyclerView.Adapter mAdapter = new VideoAdapter(mVideoItemList);
        mRecyclerView.setAdapter(mAdapter);

        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                new VideoListDownloader().searchVideos(mSearch.getText().toString(), new SessionManager(SearchActivity.this).getId(), new VideoListDownloaderCallback() {
                    @Override
                    public void onVideosDownloadSuccess(ArrayList<VideoListItem> videos) {
                        mVideoItemList.addAll(videos);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onVideosDownloadFailure() {

                    }
                });
                return false;
            }
        });
    }

    @Override public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onVideoItemMenuEvent(VideoItemMenuEvent event) {
        new VideoItemPopupMenu(this, event.getId(), event.getView()).show();
    }

    @Subscribe
    public void onCreateDubEvent(CreateDubEvent event) {
        Intent intent = new Intent(this, CreateDubActivity.class);
        intent.putExtra(ConstantsStore.VIDEO_ID, event.getId());
        startActivity(intent);
    }
}
