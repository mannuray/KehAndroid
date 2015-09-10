package com.dubmania.dubsmania.addvideo;

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
import android.widget.VideoView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.addvideoevent.AddVideoEditEvent;
import com.dubmania.dubsmania.communicator.eventbus.addvideoevent.AddVideoInfoEvent;
import com.dubmania.dubsmania.utils.RangeSeekBar;
import com.squareup.otto.Subscribe;

public class EditVideoFragment extends Fragment {
    private boolean isPlaying = false;
    private boolean isVideoUriSet = false;
    private Uri mUri;
    private VideoView mVideoView;
    private RangeSeekBar<Integer> trimmer;
    private Thread mSeekUpdate;

    public EditVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Log.i("Trimmer1", " duration is prepare " + mVideoView.getDuration());
                trimmer.setSelectedMaxValue(mVideoView.getDuration());
            }
        });
        trimmer = (RangeSeekBar<Integer>) view.findViewById(R.id.trimmer);

        mVideoView.setOnTouchListener(new OnVideoViewTouchListner());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_video, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.action_edit_video) {
            BusProvider.getInstance().post(new AddVideoEditEvent("")); //mUri.getPath())); // this is rendundat now but will be of use later
        }
        return true;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
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

    private class OnVideoViewTouchListner implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("file path ","set");

            if(!isVideoUriSet) {
                mVideoView.setVideoURI(mUri);
                Log.i("Trimmer", " duration is " + mVideoView.getDuration());
                trimmer.setSelectedMaxValue(mVideoView.getDuration());
                isVideoUriSet = true;
            }

            if (!isPlaying) {
                mVideoView.seekTo(trimmer.getSelectedMinValue());
                mVideoView.start();
                Log.i("Trimmer", " duration is play " + mVideoView.getDuration());
                trimmer.setSelectedMaxValue(mVideoView.getDuration());
                new TimeUpdater().start();
                isPlaying = true;
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
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (mVideoView.getCurrentPosition() > trimmer.getAbsoluteMaxValue())
                        mVideoView.pause();
                } catch (InterruptedException e) {
                    return;
                } catch (Exception e) {
                    return;
                }
            }
        }
    }


    @Subscribe
    public void onAddVideoInfoEvent(AddVideoInfoEvent event) {
        mUri = Uri.parse(event.getFilePath());
        isVideoUriSet = false;
    }
}
