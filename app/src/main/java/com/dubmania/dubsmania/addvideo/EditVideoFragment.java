package com.dubmania.dubsmania.addvideo;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;

public class EditVideoFragment extends Fragment {
    private boolean isPlaying = false;

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
        // Inflate the layout for this fragment
        Uri mUri = null;
        View view = inflater.inflate(R.layout.fragment_edit_video, container, false);
        final VideoView mVideoView = (VideoView) view.findViewById(R.id.videoView);
        mVideoView.setVideoURI(mUri);
        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isPlaying) {
                    mVideoView.start();
                    isPlaying = true;
                }
                else {
                    mVideoView.pause();
                    isPlaying = false;
                }
                return false;
            }
        });
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
}
