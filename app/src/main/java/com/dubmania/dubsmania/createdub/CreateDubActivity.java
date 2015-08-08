package com.dubmania.dubsmania.createdub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoDownloader;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoDownloaderCallback;

import java.io.File;
import java.io.IOException;

public class CreateDubActivity extends AppCompatActivity {

    private VideoView myVideoView;
    private VideoDownloader mVideoDownloader;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private Uri mUri;
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dub);

        Intent intent = getIntent();
        id = intent.getLongExtra("VIDEO_ID", new Long(0));

        mVideoDownloader = new VideoDownloader(getApplicationContext());
        try {
            mVideoDownloader.downloadVideo("searchservice/getvideo", id, new VideoDownloaderCallback() {
                @Override
                public void onVideosDownloadSuccess(File mFile) {
                    mUri = Uri.fromFile(mFile);
                    playVideo();
                }

                @Override
                public void onVideosDownloadFailure() {

                }

                @Override
                public void onProgress(int mPercentage) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }

        myVideoView = (VideoView) findViewById(R.id.create_dub_video_view);

        // create a progress bar while the video file is loading
        progressDialog = new ProgressDialog(this);
        // set a title for the progress bar
        progressDialog.setTitle("JavaCodeGeeks Android Video View Example");
        // set a message for the progress bar
        progressDialog.setMessage("Loading...");
        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(false);
        // show the progress bar
        progressDialog.show();
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
        getMenuInflater().inflate(R.menu.menu_create_dub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void playVideo() {
        try {
            //set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);

            //set the uri of the video to be played
            myVideoView.setVideoURI(mUri);
            Log.e("Error", "Startingc video " + mUri.toString());

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // close the progress bar and play the video
                progressDialog.dismiss();
                //if we have a position on savedInstanceState, the video playback should start from here
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    myVideoView.pause();
                }
            }
        });
    }
}
