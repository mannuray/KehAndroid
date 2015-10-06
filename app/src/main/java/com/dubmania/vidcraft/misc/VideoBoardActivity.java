package com.dubmania.vidcraft.misc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.dubmania.vidcraft.Adapters.VideoAdapter;
import com.dubmania.vidcraft.Adapters.VideoListItem;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.CreateDubEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoFavriouteChangedEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoItemMenuEvent;
import com.dubmania.vidcraft.communicator.networkcommunicator.DeleteVideoBoard;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoFavoriteMarker;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoListDownloader;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoListDownloaderCallback;
import com.dubmania.vidcraft.createdub.CreateDubActivity;
import com.dubmania.vidcraft.dialogs.VideoItemPopupMenu;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.SessionManager;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class VideoBoardActivity extends AppCompatActivity {
    private ArrayList<VideoListItem> mVideoItemList;
    private RecyclerView.Adapter mAdapter;

    private Long mBoardId;
    private boolean mUserBoard;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_board);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mBoardId = intent.getLongExtra(ConstantsStore.INTENT_BOARD_ID, (long) 0);
        String mBoardName = intent.getStringExtra(ConstantsStore.INTENT_BOARD_NAME); // set it in action bar
        String mUserName = intent.getStringExtra(ConstantsStore.INTENT_BOARD_USER_NAME); // set it in action bar
        int icon = intent.getIntExtra(ConstantsStore.INTENT_BOARD_ICON, 0); // set it in action bar
        mUserBoard = intent.getBooleanExtra(ConstantsStore.INTENT_BOARD_USER, false);

        getSupportActionBar().setTitle(mBoardName);
        mToolbar.setSubtitle("Uploaded by " + mUserName);
        mToolbar.setLogo(icon);



        spinner = (ProgressBar) findViewById(R.id.BoardProgressBar);
        spinner.setVisibility(View.VISIBLE);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.boardRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mVideoItemList = new ArrayList<>();
        populateData();
        mAdapter = new VideoAdapter(mVideoItemList);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(mUserBoard)
            getMenuInflater().inflate(R.menu.menu_video_board, menu);
        else
            getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
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

        if(id == R.id.action_delete_board) {
            new DeleteVideoBoard().deleteVideoBoard(mBoardId, new DeleteVideoBoard.DeleteVideoBoardCallback() {
                @Override
                public void onDeleteVideoBoardSuccess() {

                }

                @Override
                public void onDeleteVideoBoardFailure() {

                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateData() {

        new VideoListDownloader().downloadBoardVideo(mBoardId, new SessionManager(this).getId(), new VideoListDownloaderCallback() {

            @Override
            public void onVideosDownloadSuccess(ArrayList<VideoListItem> videos) {
                mVideoItemList.addAll(videos);
                mAdapter.notifyDataSetChanged();
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onVideosDownloadFailure() {

            }
        });
    }

    @Subscribe
    public void onCreateDubEvent(CreateDubEvent event) {
        Intent intent = new Intent(this, CreateDubActivity.class);
        intent.putExtra(ConstantsStore.VIDEO_ID, event.getId());
        intent.putExtra(ConstantsStore.INTENT_VIDEO_TITLE, event.getTitle());
        startActivity(intent);
    }

    @Subscribe
    public void onnVideoFavriouteChangedEvent(VideoFavriouteChangedEvent event) {
        new VideoFavoriteMarker().markFavorite(event.getId(), event.ismFavrioute());
    }

    @Subscribe
    public void onVideoItemMenuEvent(VideoItemMenuEvent event) {
        new VideoItemPopupMenu(mBoardId, this, event.getId(), event.getTitle(), event.getView(), mUserBoard).show();
    }
}
