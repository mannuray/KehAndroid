package com.dubmania.dubsmania.misc;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dubmania.dubsmania.Adapters.VideoAdapter;
import com.dubmania.dubsmania.Adapters.VideoListItem;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.VideoItemMenuEvent;
import com.dubmania.dubsmania.dialogs.VideoItemMenuDialog;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;


public class SearchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<VideoListItem> mVideoItemList;

    // TO Do remove it after experimenth
    private TypedArray navMenuIcons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.search_layout);
        EditText search = (EditText) actionBar.getCustomView().findViewById(
                R.id.searchfield);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                Toast.makeText(SearchActivity.this, "Search triggered",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mVideoItemList = new ArrayList<VideoListItem>(Arrays.asList(
                new VideoListItem("heros", "mannu", false),
                new VideoListItem("heros1", "mannu", false),
                new VideoListItem("heros2", "mannu", false),
                new VideoListItem("heros3", "prashant", false)
        ));
        mAdapter = new VideoAdapter(mVideoItemList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Subscribe
    public void onVideoItemMenuEvent(VideoItemMenuEvent event) {
        VideoItemMenuDialog dialog = new VideoItemMenuDialog();
        dialog.show(getSupportFragmentManager(), "tag");
    }
}
