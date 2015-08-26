package com.dubmania.dubsmania.createdub;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.createdubevent.RecordingDoneEvent;
import com.dubmania.dubsmania.communicator.eventbus.createdubevent.SetRecordFilesEvent;
import com.dubmania.dubsmania.utils.media.AudioManager;
import com.dubmania.dubsmania.utils.media.VideoManager;
import com.squareup.otto.Subscribe;

import java.io.IOException;


public class RecordDubFragment extends Fragment {

    private Button mPlayVideoOringinal;
    private Button mRecord;
    private Button mPlayVideoRecorded;
    private ProgressBar mProgressBar;

    private AudioManager mAudioManager;
    private VideoManager mVideoManager;

    enum State {playingOriginal, playingRecorded, recording, pause }
    State mState = State.pause;

    public RecordDubFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_dub, container, false);
        mAudioManager = new AudioManager(getActivity().getApplicationContext());
        mVideoManager = new VideoManager((VideoView) view.findViewById(R.id.videoView), new VideoManager.OnCompletionCallback(){

            @Override
            public void onComplete() {
                if(mState == State.recording) {
                    mPlayVideoRecorded.setEnabled(true);
                    mAudioManager.pause();
                }
                mState = State.pause;
            }
        });
        mPlayVideoOringinal = (Button) view.findViewById(R.id.playOriginal);
        mPlayVideoOringinal.setEnabled(false);
        mPlayVideoOringinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mState == State.recording) {
                    mAudioManager.pause();
                }
                mVideoManager.start(false);
                mState = State.playingOriginal;
            }
        });

        mRecord = (Button) view.findViewById(R.id.recordDub);
        mRecord.setEnabled(false);
        mRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mState == State.playingOriginal) {
                    mVideoManager.setPos(mAudioManager.getCurrentTime());
                }

                mVideoManager.start(true);
                try {
                    mAudioManager.record();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mState = State.recording;

            }
        });
        mPlayVideoRecorded = (Button) view.findViewById(R.id.playRecorded);
        mPlayVideoRecorded.setEnabled(false);
        mPlayVideoRecorded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mState == State.recording) {
                    mAudioManager.pause();
                }

                if(mState == State.playingOriginal) {
                    mVideoManager.pause();
                }
                mVideoManager.setPos(mAudioManager.getCurrentTime());
                mVideoManager.start(true);
                mAudioManager.play();
            }
        });

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_dub, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_dub) {
            BusProvider.getInstance().post(new RecordingDoneEvent(mAudioManager.prepareAudio()));
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        mVideoManager.setVideoFilePath(event.getVideoFile());
        mAudioManager.setRecordDuration(mVideoManager.getDuration());
        mProgressBar.setVisibility(View.GONE);
        mPlayVideoOringinal.setEnabled(true);
        mRecord.setEnabled(true);
    }
}
