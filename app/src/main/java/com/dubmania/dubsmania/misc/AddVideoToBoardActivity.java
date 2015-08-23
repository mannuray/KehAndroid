package com.dubmania.dubsmania.misc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.dubmania.dubsmania.Adapters.VideoBoardAdapter;
import com.dubmania.dubsmania.Adapters.VideoBoardListItem;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.VideoBoardClickedEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.AddVideoToBoard;
import com.dubmania.dubsmania.communicator.networkcommunicator.AddVideoToBoardCallback;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoBoardDownloaderCallback;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoBoardsDownloader;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.dubmania.dubsmania.utils.SessionManager;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class AddVideoToBoardActivity extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;
    private ArrayList<VideoBoardListItem> mVideoBoardItemList;
    private Long mVideoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_to_board);
        Intent intent = getIntent();
        mVideoId =intent.getLongExtra(ConstantsStore.INTENT_VIDEO_ID, (long) 0);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.add_video_to_board_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mVideoBoardItemList = new ArrayList<>();
        mAdapter = new VideoBoardAdapter(mVideoBoardItemList);
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
        getMenuInflater().inflate(R.menu.menu_add_video_to_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

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
                mVideoBoardItemList.addAll(boards);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onVideosBoardsDownloadFailure() {
                // do nothing
            }
        });

    }
}
