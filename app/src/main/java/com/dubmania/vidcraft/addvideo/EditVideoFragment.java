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
    private Uri mUri = null;
    private VideoView mVideoView;
    private RangeSeekBar<Integer> mTrimmer;
    private MenuItem next;
    private ProgressBar mProgressBar;

    private static final String VIDEO_URI = "video_uri";
    private static final String MARKER_BAR_START = "marker_bar_start";
    private static final String MARKER_BAR_END = "marker_bar_end";

    /*private int start;
    private int end;
    private boolean fromSavedInstance = false;*/


    public EditVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setHasOptionsMenu(true);
        /*if(savedInstanceState != null && !savedInstanceState.isEmpty()) {

            start = savedInstanceState.getInt(MARKER_BAR_START);
            end = savedInstanceState.getInt(MARKER_BAR_END);
            fromSavedInstance = true;

            mUri = Uri.parse(savedInstanceState.getString(VIDEO_URI));
            Log.i("click", "urin is " + savedInstanceState.getString(VIDEO_URI) + " start is " + start + " end is " + end);
        }*/
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
                //mTrimmer.resetSelectedValues();
                mVideoView.seekTo(Math.max(100, mTrimmer.getSelectedMinValue()));
            }
        });
        mTrimmer = (RangeSeekBar<Integer>) view.findViewById(R.id.trimmer);
        mTrimmer.setNotifyWhileDragging(true);
        mTrimmer.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer touchValue, Integer minValue, Integer maxValue) {
                if(mVideoView != null) {
                    if(touchValue != -1) {
                        if(touchValue >= minValue && touchValue <= maxValue) {
                            mVideoView.seekTo(touchValue);
                            mTrimmer.setCurrentProgressValue(touchValue);
                        }
                    }
                    else {
                        mVideoView.seekTo(minValue);
                    }
                }
            }
        });
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mVideoView.setOnTouchListener(new onVideoViewTouchListner());
        /*if(fromSavedInstance) {

            final int markerStart = start;
            final int markerEnd = end;

            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mTrimmer.setRangeValues(0, mVideoView.getDuration());
                    mTrimmer.resetSelectedValues();
                    mTrimmer.setSelectedMaxValue(markerEnd);
                    mTrimmer.setSelectedMinValue(markerStart);

                }
            });

            mVideoView.setVideoURI(mUri);
            mVideoView.seekTo(markerStart);
        /*if(next != null)
            next.setEnabled(true);//
            isVideoUriSet = true;
        }*/

        if(mUri != null) {
            mVideoView.setVideoURI(mUri);
        }
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

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mUri != null) {
            outState.putString(VIDEO_URI, mUri.toString());
            start = mTrimmer.getSelectedMinValue();
            end = mTrimmer.getSelectedMaxValue();
            outState.putInt(MARKER_BAR_START, start);
            outState.putInt(MARKER_BAR_END, end);
            Log.i("click", "on save state urin is " + mUri.toString() + " start is " + mTrimmer.getSelectedMinValue() + " end is " + mTrimmer.getSelectedMaxValue());
        }
    }*/

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
                //mVideoView.seekTo(mTrimmer.getSelectedMinValue());
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
        //Log.i("click ", "add event recived" + event.getFilePath());
        mUri = Uri.parse(event.getFilePath());
        mVideoView.setVideoURI(mUri);
        //mVideoView.seekTo(100);
        mTrimmer.resetSelectedValues();
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
            //Log.i("click ", "on chaneg fragmetn back " + start + " " + end);
            mProgressBar.setVisibility(View.GONE);
            mProgressBar.setProgress(0);
            mTrimmer.setEnabled(true);
            mVideoView.setEnabled(true);
        }
    }
}
