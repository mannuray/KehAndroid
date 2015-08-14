package com.dubmania.dubsmania.createdub;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.createdubevent.SetRecordFilesEvent;
import com.dubmania.dubsmania.utils.AudioRecorder;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;


public class RecordDubFragment extends Fragment {

    private VideoView mVideoView;
    private AudioRecorder mAudioRecorder;
    private MediaPlayer mAudioPlayer;
    private MediaPlayer mVideoPlayer;

    private File mAudioFile;

    private Button mPlayVideoOringinal;
    private Button mRecord;
    private Button mPlayVideoRecorded;
    ProgressBar mProgressBar;

    private boolean isRecording = false;
    private boolean isRecordedAudioAvailable = false;

    public RecordDubFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_dub, container, false);
        mPlayVideoOringinal = (Button) view.findViewById(R.id.playOriginal);
        mPlayVideoOringinal.setEnabled(false);
        mPlayVideoOringinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = false;
                mVideoView.start();
            }
        });

        mRecord = (Button) view.findViewById(R.id.recordDub);
        mRecord.setEnabled(false);
        mRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        mPlayVideoRecorded = (Button) view.findViewById(R.id.playRecorded);
        mPlayVideoRecorded.setEnabled(false);
        mPlayVideoRecorded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isRecordedAudioAvailable) {
                        mAudioPlayer.setDataSource(mAudioFile.getAbsolutePath());
                        mAudioPlayer.prepare();
                        mVideoPlayer.setVolume(0f, 0f);
                        mAudioPlayer.start();
                        mVideoView.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        //set up video
        mVideoView = (VideoView) view.findViewById(R.id.videoView);
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isRecording) {
                    mAudioRecorder.stopRecording();
                    isRecording = false;
                    mRecord.setEnabled(true);
                    isRecordedAudioAvailable = true;
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

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onSetRecordFilesEvent(SetRecordFilesEvent event) {
        mAudioFile = event.getAudioFile();
        File mVideoFile = event.getVideoFile();
        mProgressBar.setVisibility(View.GONE);
        mPlayVideoOringinal.setEnabled(true);
        mRecord.setEnabled(true);

        try {
            mVideoView.setVideoURI(Uri.parse(mVideoFile.getAbsolutePath()));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        mVideoView.requestFocus();
    }
}
