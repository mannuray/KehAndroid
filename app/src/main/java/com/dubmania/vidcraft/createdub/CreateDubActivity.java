package com.dubmania.vidcraft.createdub;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.createdubevent.CreateDubShareEvent;
import com.dubmania.vidcraft.communicator.eventbus.createdubevent.RecordingDoneEvent;
import com.dubmania.vidcraft.communicator.eventbus.createdubevent.SetRecordFilesEvent;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoDownloader;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoDownloaderCallback;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.VidCraftApplication;
import com.dubmania.vidcraft.utils.SavedDubsData;
import com.dubmania.vidcraft.utils.VideoSharer;
import com.dubmania.vidcraft.utils.media.VideoPreparer;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import io.realm.Realm;

public class CreateDubActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mPager;
    PagerAdapter mPagerAdapter;
    Long id;

    private File mVideoFile;
    private File mOutputFile;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dub);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPager.setAdapter(mPagerAdapter);

        Intent intent = getIntent();
        id = intent.getLongExtra(ConstantsStore.VIDEO_ID, (long) 0);
        mTitle = intent.getStringExtra(ConstantsStore.INTENT_VIDEO_TITLE);

        VideoDownloader mVideoDownloader = new VideoDownloader();
        mVideoDownloader.downloadVideo(ConstantsStore.URL_DOWNLOAD_VIDEO, id, new VideoDownloaderCallback() {
            @Override
            public void onVideosDownloadSuccess(File mFile) {
                mVideoFile = mFile;
                BusProvider.getInstance().post(new SetRecordFilesEvent(mVideoFile));
            }

            @Override
            public void onVideosDownloadFailure() {

            }

            @Override
            public void onProgress(int mPercentage) {

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

    @Override
    public void onBackPressed() {
        int position = mPager.getCurrentItem();
        if (position == 0 || (position == mPagerAdapter.getCount() - 1)) {
            finish();
        }
        mPager.setCurrentItem(position - 1);
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
        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_dub) {
            done();
            return true;
        } */

        return super.onOptionsItemSelected(item);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        TypedArray title = getResources()
                .obtainTypedArray(R.array.pager_signup_titles);
        String titles[] = {title.getString(0), title.getString(1), title.getString(2), title.getString(3)};
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {

            switch (i) {
                case 0:
                    return new RecordDubFragment();
                case 1:
                    return new FinishDubFragment();
            }
            return new RecordDubFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private File getOutputFile(String mTitle) {

        String mDatadir = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();

        File file = new File(mDatadir, mTitle + ".mp4");
        int i = 1;
        while (file.exists()) {
            file = new File(mDatadir, mTitle + "(" + String.valueOf(i) + ").mp4");
            i++;
        }
        return file;
    }

    @Subscribe
    public void onCreateDubShareEvent(CreateDubShareEvent event) {
        VideoSharer sharer = new VideoSharer(this, mOutputFile.getAbsolutePath());
        switch (event.getAppId()) {
            case ConstantsStore.SHARE_APP_ID_MESSENGER:
                sharer.shareViaWhatsApp();
                break;
            case ConstantsStore.SHARE_APP_ID_WHATSAPP:
                sharer.shareViaFacebookMessenger();
                break;
            case ConstantsStore.SHARE_APP_ID_SAVE_GALLERY:
                sharer.saveInGallery();
        }
        finish();
    }

    @Subscribe
    public void onRecordingDoneEvent(RecordingDoneEvent event) {
        //prepare viedo
        VideoPreparer mPreparer = new VideoPreparer();
        mOutputFile = getOutputFile(mTitle);
        // prepare the audio file
        try {
            mPreparer.prepareVideo(event.getAudioFile(), mVideoFile, mOutputFile);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "unable to prepare video", Toast.LENGTH_LONG).show();
            return;
        }

        Realm realm = Realm.getInstance(getApplicationContext());
        realm.beginTransaction();
        SavedDubsData dubsData = realm.createObject( SavedDubsData.class );
        dubsData.setFilePath(mOutputFile.getAbsolutePath());
        dubsData.setTitle(mTitle);
        dubsData.setCreationDate(DateFormat.getDateTimeInstance().format(new Date()));
        realm.commitTransaction();

        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory("Video")
                .setAction("CreateDub")
                .setLabel(String.valueOf(id));

        VidCraftApplication application = new VidCraftApplication();
        Tracker mTracker = VidCraftApplication.tracker();
        mTracker.send(builder.build());

        mPager.setCurrentItem(1);
    }
}
