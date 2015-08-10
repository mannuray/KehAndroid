package com.dubmania.dubsmania.createdub;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoDownloader;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoDownloaderCallback;
import com.dubmania.dubsmania.misc.AudioRecorder;

import java.io.File;
import java.io.IOException;

public class CreateDubActivity extends AppCompatActivity {

    private VideoView mVideoView;
    private AudioRecorder mAudioRecorder;
    private MediaPlayer mAudioPlayer;
    private MediaPlayer mVideoPlayer;

    private File mVideoFile;
    private File mAudioFile;

    private Button mPlayVideoOringinal;
    private Button mRecord;
    private Button mPlayVideoRecorded;
    private ProgressBar mProgressBar;

    private boolean isRecording = false;
    private boolean isRecordedAudiaAvailable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dub);

        //set up view
        mPlayVideoOringinal = (Button) findViewById(R.id.create_dub_play_original);
        mPlayVideoOringinal.setEnabled(false);
        mRecord = (Button) findViewById(R.id.create_dub_record);
        mRecord.setEnabled(false);
        mPlayVideoRecorded = (Button) findViewById(R.id.create_dub_play_recorded);
        mPlayVideoRecorded.setEnabled(false);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);



        Intent intent = getIntent();
        Long id = intent.getLongExtra("VIDEO_ID", new Long(0));

        try {
            mVideoFile = File.createTempFile(id.toString() + "_video", "mp4", getApplicationContext().getCacheDir());
            mAudioFile = File.createTempFile(id.toString() + "_audio", "mp4", getApplicationContext().getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }

        VideoDownloader mVideoDownloader = new VideoDownloader();
        mVideoDownloader.downloadVideo("searchservice/getvideo", id, mVideoFile, new VideoDownloaderCallback() {
            @Override
            public void onVideosDownloadSuccess(File mFile) {
                //mVideoFile = mFile;
                mPlayVideoOringinal.setEnabled(true);
                mRecord.setEnabled(true);
                //playVideo();
                try {
                    mVideoView.setVideoURI(Uri.parse(mVideoFile.getAbsolutePath()));
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                mVideoView.requestFocus();
            }

            @Override
            public void onVideosDownloadFailure() {

            }

            @Override
            public void onProgress(int mPercentage) {

            }
        });

        //set up video
        mVideoView = (VideoView) findViewById(R.id.create_dub_video_view);
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isRecording) {
                    mAudioRecorder.stopRecording();
                    isRecording = false;
                    mRecord.setEnabled(true);
                    isRecordedAudiaAvailable = true;
                    mPlayVideoRecorded.setEnabled(true);
                }
            }
        });
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                mRecord.setEnabled(true);
                mVideoPlayer = mediaPlayer;
            }
        });

        // set up audio
        mAudioRecorder = new AudioRecorder();
        mAudioPlayer = new MediaPlayer();

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

    public void onPlayVideoOrigClick(View v) throws IOException {
        isRecording = false;
        mVideoView.start();
    }

    public void onRecordVideoClick(View v) {
        isRecording = true;
        mRecord.setEnabled(false);
        try {
            mVideoPlayer.setVolume(0f, 0f);
            mAudioRecorder.startRecording(mAudioFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mVideoView.start();
    }

    public void onPlayVideoRecordedClick(View v) {
        try {
            mAudioPlayer.setDataSource(mAudioFile.getAbsolutePath());
            mAudioPlayer.prepare();
            mVideoPlayer.setVolume(0f, 0f);
            mAudioPlayer.start();
            mVideoView.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
