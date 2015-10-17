package com.dubmania.vidcraft.misc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dubmania.vidcraft.Adapters.VideoBoardAdapter;
import com.dubmania.vidcraft.Adapters.VideoBoardListItem;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoBoardClickedEvent;
import com.dubmania.vidcraft.communicator.networkcommunicator.AddVideoToBoard;
import com.dubmania.vidcraft.communicator.networkcommunicator.AddVideoToBoardCallback;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoBoardDownloaderCallback;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoBoardsDownloader;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.SessionManager;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class AddVideoToBoardActivity extends AppCompatActivity {
    Toolbar mToolbar;
    private VideoBoardAdapter mAdapter;
    private Long mVideoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_to_board);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mVideoId =intent.getLongExtra(ConstantsStore.INTENT_VIDEO_ID, (long) 0);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.add_video_to_board_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<VideoBoardListItem> mVideoBoardItemList = new ArrayList<>();
        mVideoBoardItemList.add(null);
        mAdapter = new VideoBoardAdapter(mVideoBoardItemList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        populateData();
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

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onVideoBoardClickedEvent(VideoBoardClickedEvent event) {
        new AddVideoToBoard().addVideoToBoard(event.getId(), mVideoId, new AddVideoToBoardCallback() {
            @Override
            public void onAddVideoToBoardSuccess() {
                finish();
            }

            @Override
            public void onAddVideoToBoardFailure() {
                // show toast
            }
        });
    }

    private void populateData() {
        String user = new SessionManager(this).getUser();
        new VideoBoardsDownloader(getApplicationContext()).getUserBoards(user, new VideoBoardDownloaderCallback() {
            @Override
            public void onVideoBoardsDownloadSuccess(ArrayList<VideoBoardListItem> boards) {
                mAdapter.addData(boards);
            }

            @Override
            public void onVideosBoardsDownloadFailure() {
                // do nothing
            }
        });

    }
}
