package com.dubmania.vidcraft.misc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.dubmania.vidcraft.Adapters.ListItem;
import com.dubmania.vidcraft.Adapters.VideoAndBoardAdapter;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.CreateDubEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoBoardClickedEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoItemMenuEvent;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoAndBoardDownloader;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoAndBoardDownloaderCallback;
import com.dubmania.vidcraft.createdub.CreateDubActivity;
import com.dubmania.vidcraft.dialogs.VideoItemPopupMenu;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.SessionManager;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {
    private ArrayList<ListItem> mItemList;
    private EditText mSearch;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setCustomView(R.layout.search_layout);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        mSearch = (EditText) actionBar.getCustomView().findViewById(
                R.id.searchfield);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mItemList = new ArrayList<>();
        final RecyclerView.Adapter mAdapter = new VideoAndBoardAdapter(mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                new VideoAndBoardDownloader(getApplicationContext()).search(mSearch.getText().toString(), new SessionManager(SearchActivity.this).getId(), new VideoAndBoardDownloaderCallback() {
                    @Override
                    public void onVideoAndBoardDownloaderSuccess(ArrayList<ListItem> videos) {
                        mItemList.addAll(videos);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onVideoAndBoardDownloaderFailure() {

                    }
                });
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empty, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        new VideoItemPopupMenu(new Long(0), this, event.getId(), event.getTitle(), event.getView(), false).show();
    }

    @Subscribe
    public void onCreateDubEvent(CreateDubEvent event) {
        Intent intent = new Intent(this, CreateDubActivity.class);
        intent.putExtra(ConstantsStore.VIDEO_ID, event.getId());
        startActivity(intent);
    }

    @Subscribe
    public void onVideoBoardClickedEvent(VideoBoardClickedEvent event) {
        Intent intent = new Intent(this, VideoBoardActivity.class);
        intent.putExtra(ConstantsStore.INTENT_BOARD_ID, event.getId());
        intent.putExtra(ConstantsStore.INTENT_BOARD_NAME, event.getBoardName());
        intent.putExtra(ConstantsStore.INTENT_BOARD_USER_NAME, event.getBoardUsername());
        intent.putExtra(ConstantsStore.INTENT_BOARD_ICON, event.getIcon());
        startActivity(intent);
    }
}
