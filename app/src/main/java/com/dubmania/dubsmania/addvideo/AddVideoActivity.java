package com.dubmania.dubsmania.addvideo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.addvideoevent.AddTagsEvent;
import com.dubmania.dubsmania.communicator.eventbus.addvideoevent.AddVideoChangeFragmentEvent;
import com.dubmania.dubsmania.communicator.eventbus.addvideoevent.AddVideoEditEvent;
import com.dubmania.dubsmania.communicator.eventbus.addvideoevent.AddVideoFinishEvent;
import com.dubmania.dubsmania.communicator.eventbus.addvideoevent.AddVideoInfoEvent;
import com.dubmania.dubsmania.communicator.eventbus.addvideoevent.SearchVideoItemListEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoUploader;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoUploaderCallback;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.dubmania.dubsmania.utils.SessionManager;
import com.dubmania.dubsmania.utils.media.VideoTrimmer;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddVideoActivity extends AppCompatActivity {

    private VideoInfo mVideoInfo;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        new SessionManager(this).checkLogin(new SessionManager.LoginListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure() {
                finish();
            }
        });

        Intent intent = getIntent();
        changeFragment(intent.getIntExtra(ConstantsStore.INTENT_ADD_VIDEO_ACTION, ConstantsStore.INTENT_ADD_VIDEO_RECORD));
        mVideoInfo = new VideoInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_video) {

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

    @Override
    public void onBackPressed() {
        BusProvider.getInstance().post(new AddVideoChangeFragmentEvent(-1));
    }

    private void changeFragment(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, getFragment(position))
                .commit();
    }

    private Fragment getFragment(int position) {

        switch (position) {
            case 0:
                return new RecordVideoPagerFragment();
            case 1:
                return new ImportVideoPagerFragment();
        }
        return new RecordVideoPagerFragment();
    }

    private class VideoInfo {
        private String mSrcFilePath;
        private String mDstFilePath;
        private ArrayList<Tag> mTags;
        private String mTitle;
        private String mLanguage;

        public String getSrcFilePath() {
            return mSrcFilePath;
        }

        public void setFilePath(String mFilePath) {
            this.mSrcFilePath = mFilePath;
            this.mDstFilePath = "Temp_" + mFilePath;
        }

        public ArrayList<Tag> getTags() {
            return mTags;
        }

        public void setTags(ArrayList<Tag> mTags) {
            this.mTags = mTags;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public String getLanguage() {
            return mLanguage;
        }

        public void setLanguage(String mLanguage) {
            this.mLanguage = mLanguage;
        }

        public String getDstFilePath() {
            return mDstFilePath;
        }
    }

    /*
    @Produce
    public AddVideoInfoEvent produceAddVideoInfo() {
        return new AddVideoInfoEvent(mVideoInfo.getSrcFilePath(), mVideoInfo.getTags(), mVideoInfo.getTitle(), mVideoInfo.getLanguage());
    }*/

    @Subscribe
    public void onSearchVideoItemListEvent(SearchVideoItemListEvent event) {
        mVideoInfo.setFilePath(event.getFilePath());
        BusProvider.getInstance().post(new AddVideoInfoEvent(mVideoInfo.getSrcFilePath(), mVideoInfo.getTags(), mVideoInfo.getTitle(), mVideoInfo.getLanguage()));
        BusProvider.getInstance().post(new AddVideoChangeFragmentEvent(1));
    }

    @Subscribe
    public void onAddVideoEditEvent(AddVideoEditEvent event) {
        try {
            Log.i("Video Trimmer", "Staring trimmer");
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_hhmmss_SSS", Locale.getDefault());
            File dst = File.createTempFile(String.format("Video_File_%s", sdf.format(new Date())), ".mp4", getApplicationContext().getCacheDir());
            mVideoInfo.mDstFilePath = dst.getAbsolutePath();
            VideoTrimmer.startTrim(new File(mVideoInfo.getSrcFilePath()), new File(mVideoInfo.getDstFilePath()), event.getStartPos(), event.getEndPos());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BusProvider.getInstance().post(new AddVideoChangeFragmentEvent(2));
    }

    @Subscribe
    public void onAddTagsEvent(AddTagsEvent event) {
        mVideoInfo.setTags(event.getTags());
        BusProvider.getInstance().post(new AddVideoInfoEvent(mVideoInfo.getSrcFilePath(), mVideoInfo.getTags(), mVideoInfo.getTitle(), mVideoInfo.getLanguage()));
        BusProvider.getInstance().post(new AddVideoChangeFragmentEvent(3));
    }

    @Subscribe
    public void onAddVideoFinishEvent(AddVideoFinishEvent event) {
        // change it to dst file path once file is modified
        new VideoUploader().addVideo(mVideoInfo.getDstFilePath(), event.getTitle(), mVideoInfo.getTags(), event.getLanguage(), new VideoUploaderCallback() {
            @Override
            public void onVideosUploadSuccess() {
                finish();
            }

            @Override
            public void onVideosUploadFailure() {
                Toast.makeText(getApplicationContext(), " unable to add video", Toast.LENGTH_LONG).show();
            }
        });
    }
}
