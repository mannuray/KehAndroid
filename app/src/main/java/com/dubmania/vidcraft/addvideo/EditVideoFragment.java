package com.dubmania.vidcraft.addvideo;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.AddVideoChangeFragmentEvent;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.AddVideoEditEvent;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.AddVideoInfoEvent;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.SetProgressBarValue;
import com.dubmania.vidcraft.utils.RangeSeekBar;
import com.squareup.otto.Subscribe;

public class EditVideoFragment extends Fragment {
    private boolean isPlaying = false;
    private boolean isVideoUriSet = false;
    private Uri mUri;
    private VideoView mVideoView;
    private RangeSeekBar<Integer> mTrimmer;
    private MenuItem next;
    private ProgressBar mProgressBar;

    public EditVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_video, container, false);
        mVideoView = (VideoView) view.findViewById(R.id.videoView);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mTrimmer.setRangeValues(0, mVideoView.getDuration());
                mTrimmer.resetSelectedValues();
            }
        });
        mTrimmer = (RangeSeekBar<Integer>) view.findViewById(R.id.trimmer);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mVideoView.setOnTouchListener(new onVideoViewTouchListner());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_video, menu);
        super.onCreateOptionsMenu(menu, inflater);
        next = menu.findItem(R.id.action_edit_video);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.action_edit_video) {
            item.setEnabled(false);
            item.setVisible(false);
            mProgressBar.setVisibility(View.VISIBLE);
            mTrimmer.setEnabled(false);
            mVideoView.setEnabled(false);
            BusProvider.getInstance().post(new AddVideoEditEvent(mTrimmer.getSelectedMinValue(), mTrimmer.getSelectedMaxValue())); //mUri.getPath())); // this is rendundat now but will be of use late
        }
        return true;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if(next != null) {
            next.setEnabled(true);
            next.setVisible(true);
        }
        if(mVideoView != null) {
            mVideoView.pause();
            isPlaying = false;
        }
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

    private class onVideoViewTouchListner implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(!isVideoUriSet) {
                mVideoView.setVideoURI(mUri);
                mTrimmer.setSelectedMaxValue(mVideoView.getDuration());
                isVideoUriSet = true;
            }

            if (!isPlaying) {
                mVideoView.seekTo(mTrimmer.getSelectedMinValue());
                mVideoView.start();
                isPlaying = true;
                new TimeUpdater().start();
            } else {
                mVideoView.pause();
                isPlaying = false;
            }
            return false;
        }
    }

    private class TimeUpdater extends Thread {
        @Override
        public void run() {
            while (isPlaying) {
                try {
                    Thread.sleep(100);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTrimmer.setCurrentProgressValue(mVideoView.getCurrentPosition());
                        }
                    });
                    if (mVideoView.getCurrentPosition() > mTrimmer.getSelectedMaxValue()) {
                        isPlaying = false;
                        mVideoView.pause();
                    }
                } catch (Exception e) {
                    return;
                }
            }
        }
    }


    @Subscribe
    public void onAddVideoInfoEvent(AddVideoInfoEvent event) {
        mUri = Uri.parse(event.getFilePath());
        mVideoView.setVideoURI(mUri);
        mVideoView.seekTo(100);
        /*if(next != null)
            next.setEnabled(true);*/
        isVideoUriSet = true;
    }

    @Subscribe
    public void onSetProgressBarValue(SetProgressBarValue event) {
        //Log.i("Progress", "updating progress value  " + event.getPercentage());
        mProgressBar.setProgress(event.getPercentage());
    }

    @Subscribe
    public void onAddVideoChangeFragmentEvent(AddVideoChangeFragmentEvent event) {
        if(event.getPosition() == 2) {
            // we got changed from here
            mProgressBar.setVisibility(View.GONE);
            mProgressBar.setProgress(0);
            mTrimmer.setEnabled(true);
            mVideoView.setEnabled(true);
        }
    }
}
