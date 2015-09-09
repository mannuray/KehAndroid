package com.dubmania.dubsmania.createdub;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.createdubevent.CreateDubShareEvent;
import com.dubmania.dubsmania.communicator.eventbus.createdubevent.RecordingDoneEvent;
import com.dubmania.dubsmania.communicator.eventbus.createdubevent.SetRecordFilesEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoDownloader;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoDownloaderCallback;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.dubmania.dubsmania.utils.DubsApplication;
import com.dubmania.dubsmania.utils.SavedDubsData;
import com.dubmania.dubsmania.utils.VideoSharer;
import com.dubmania.dubsmania.utils.media.VideoPreparer;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import io.realm.Realm;

public class CreateDubActivity extends AppCompatActivity {

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

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPager.setAdapter(mPagerAdapter);

        Intent intent = getIntent();
        id = intent.getLongExtra(ConstantsStore.VIDEO_ID, (long) 0);
        mTitle = intent.getStringExtra(ConstantsStore.INTENT_VIDEO_TITLE);

        try {
            mVideoFile = File.createTempFile(id.toString() + "_video", ".mp4", getApplicationContext().getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }

        VideoDownloader mVideoDownloader = new VideoDownloader();
        mVideoDownloader.downloadVideo(ConstantsStore.URL_DOWNLOAD_VIDEO, id, mVideoFile, new VideoDownloaderCallback() {
            @Override
            public void onVideosDownloadSuccess(File mFile) {
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
        // Get the directory for the user's public pictures directory.
        if(!Environment.getExternalStorageDirectory().exists()) {
            Environment.getExternalStorageDirectory().mkdir();
        }
        /*File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), mTitle + ".mp4");*/

        File file = new File("/storage/sdcard0/dub/Movies", mTitle + ".mp4");
        int i = 1;
        while (file.exists()) {
            /*file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES), mTitle + "(" + String.valueOf(i) + ").mp4");*/
            file = new File("/storage/sdcard0/dub/Movies", mTitle + "(" + String.valueOf(i) + ").mp4");
            i++;
        }
        return file;
    }

    @Subscribe
    public void onCreateDubShareEvent(CreateDubShareEvent event) {
        switch (event.getAppId()) {
            case ConstantsStore.SHARE_APP_ID_MESSENGER:
                break;
            case ConstantsStore.SHARE_APP_ID_WHATSAPP:
                break;
            case ConstantsStore.SHARE_APP_ID_SAVE_GALLERY:
                new VideoSharer(this).saveInGallery(mOutputFile);
        }
        finish();
    }

    @Subscribe
    public void onRecordingDoneEvent(RecordingDoneEvent event) {
        //prepare viedo
        VideoPreparer mPreparer = new VideoPreparer();
        mOutputFile = getOutputFile(mTitle);
        // prepare the audio file
        mPreparer.prepareVideo(event.getAudioFile(), mVideoFile, mOutputFile);

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

        DubsApplication application = new DubsApplication();
        Tracker mTracker = DubsApplication.tracker();
        mTracker.send(builder.build());

        mPager.setCurrentItem(1);
    }
}
