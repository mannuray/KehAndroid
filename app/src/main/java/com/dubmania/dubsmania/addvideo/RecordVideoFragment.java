package com.dubmania.dubsmania.addvideo;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.addvideoevent.AddVideoRecordDoneEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.OnClickListnerEvent;
import com.dubmania.dubsmania.utils.MiscFunction;

import java.io.File;
import java.io.IOException;

public class RecordVideoFragment extends Fragment implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    public MediaRecorder mrec = new MediaRecorder();
    private Button startRecording = null;
    private boolean recordAvailable = false;
    private boolean recording = false;

    private String video;
    private Camera mCamera;

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
        startRecording = (Button) view.findViewById(R.id.buttonstart);
        startRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!recording) {
                    try {
                        startRecording();
                        Log.i("Change", "Recording Start" );
                        recording = true;
                    } catch (Exception e) {
                        String message = e.getMessage();
                        Log.i(null, "Problem Start" + message);
                        mrec.release();
                    }
                }
                else {
                    mrec.stop();
                    mrec.release();
                    mrec = null;
                    Log.i("Change", "Recording Stop" );
                    BusProvider.getInstance().post(new AddVideoRecordDoneEvent(video));
                }
            }
        });
        mCamera = Camera.open();
        surfaceView = (SurfaceView) view.findViewById(R.id.surface_camera);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

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

    protected void startRecording() throws IOException
    {
        mrec = new MediaRecorder();  // Works well
        mCamera.unlock();

        mrec.setCamera(mCamera);

        mrec.setPreviewDisplay(surfaceHolder.getSurface());
        mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mrec.setAudioSource(MediaRecorder.AudioSource.MIC);

        CamcorderProfile profile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT,CamcorderProfile.QUALITY_HIGH);
        profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
        profile.videoCodec = MediaRecorder.VideoEncoder.MPEG_4_SP;
        profile.videoFrameHeight = 240;
        profile.videoFrameWidth = 320;
        profile.videoBitRate = 15;

        mrec.setProfile(profile);
        mrec.setPreviewDisplay(surfaceHolder.getSurface());
        video = getActivity().getApplicationContext().getCacheDir().getAbsolutePath() + "/" + MiscFunction.getRandomFileName("Record") + ".mp4";
        mrec.setOutputFile(video);

        mrec.prepare();
        mrec.start();
    }

    protected void stopRecording() {
        mrec.stop();
        mrec.release();
        mCamera.release();
    }

    private void releaseMediaRecorder(){
        if (mrec != null) {
            mrec.reset();   // clear recorder configuration
            mrec.release(); // release the recorder object
            mrec = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera != null){
            Camera.Parameters params = mCamera.getParameters();
            mCamera.setParameters(params);
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
    }
}
