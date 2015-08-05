package com.dubmania.dubsmania.misc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.dubmania.dubsmania.Adapters.ImportVideoAdapter;
import com.dubmania.dubsmania.Adapters.ImportVideoListItem;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.BusProvider;
import com.dubmania.dubsmania.communicator.ImportVideoItemListEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class ImportVideoActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImportVideoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ImportVideoListItem> mVideoItemList;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_video);

        mRecyclerView = (RecyclerView) findViewById(R.id.import_video_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mVideoItemList = new ArrayList<ImportVideoListItem>();
        populateVideo();

        mAdapter = new ImportVideoAdapter(mVideoItemList);
        mRecyclerView.setAdapter(mAdapter);
        EditText search = (EditText) findViewById(R.id.import_video_seach_edit);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.setFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_import_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
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

    private void populateVideo() {
        String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID };

        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE };

        String thumbnail;
        cursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                mVideoItemList.add(new ImportVideoListItem(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE))));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    @Subscribe
    public void onImportVideoItemListEvent(ImportVideoItemListEvent event) {
        Intent intent = new Intent(this, PlayVideoActivity.class);
        intent.putExtra("URI", event.getUri());
        startActivity(intent);
    }
}
