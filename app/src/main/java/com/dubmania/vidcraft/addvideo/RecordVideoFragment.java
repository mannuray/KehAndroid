package com.dubmania.vidcraft.addvideo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.AddVideoRecordDoneEvent;
import com.dubmania.vidcraft.utils.MiscFunction;
import com.dubmania.vidcraft.utils.media.VideoRecorder;

import java.io.IOException;

public class RecordVideoFragment extends Fragment {

    private String mVideoFilePath;
    private VideoRecorder mVideoRecorder;

    public RecordVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_recond_video, container, false);
        Button startRecording = (Button) view.findViewById(R.id.buttonstart);
        startRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mVideoRecorder.isRecording()) {
                    mVideoRecorder.stopRecording();
                    BusProvider.getInstance().post(new AddVideoRecordDoneEvent(mVideoFilePath));
                }
                else {
                    try {
                        mVideoRecorder.startRecording();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mVideoFilePath = getActivity().getApplicationContext().getCacheDir().getAbsolutePath() + "/" + MiscFunction.getRandomFileName("Record") + ".mp4";
        SurfaceView surfaceView = (SurfaceView) view.findViewById(R.id.surface_camera);
        mVideoRecorder = new VideoRecorder(surfaceView.getHolder(), mVideoFilePath);

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
