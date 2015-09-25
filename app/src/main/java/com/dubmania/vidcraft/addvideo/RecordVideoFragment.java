package com.dubmania.vidcraft.addvideo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.AddVideoRecordDoneEvent;
import com.dubmania.vidcraft.utils.MiscFunction;
import com.dubmania.vidcraft.utils.UtilsDevice;

import java.io.File;
import java.io.IOException;

public class RecordVideoFragment extends Fragment  {

    private ImageView startRecording = null;
    private boolean recording = false;
    private String video;
    private Camera mCamera;
    private CameraPreview cameraPreview;
    RelativeLayout camera_preview;
    private MediaRecorder mediaRecorder;
    ImageView switch_camera,auto_flash;
    private static int cameraId = -1;
    private boolean cameraFront = false;


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
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        startRecording = (ImageView) view.findViewById(R.id.buttonstart);
        camera_preview=(RelativeLayout)view.findViewById(R.id.camera_preview);
        switch_camera=(ImageView)view.findViewById(R.id.switch_camera);
        auto_flash=(ImageView) view.findViewById(R.id.auto_flash);

        cameraPreview = new CameraPreview(getActivity(), mCamera);
        camera_preview.addView(cameraPreview);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        setListener();
    }

    public void initData(){
        if (!hasCamera(getActivity())) {
            Toast.makeText(getActivity(),
                    "Sorry, your phone does not have a camera!",
                    Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
        if (mCamera == null) {
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(getActivity(),
                        "No front facing camera found.", Toast.LENGTH_LONG)
                        .show();
                switch_camera.setVisibility(View.GONE);
            }
            mCamera = Camera.open(findBackFacingCamera());
            cameraPreview.refreshCamera(mCamera);
        }

    }

    public void setListener(){
        startRecording.setOnClickListener(m_click);
        switch_camera.setOnClickListener(m_click);
    }

    View.OnClickListener m_click=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(view==startRecording) {
                if (recording) {
                    mediaRecorder.stop();
                    releaseMediaRecorder();
                    recording = false;
                    startRecording.setImageResource(R.drawable.before_record);
                    BusProvider.getInstance().post(new AddVideoRecordDoneEvent(video));
                   // getActivity().finish();
                } else {
                    startRecording.setImageResource(R.drawable.record_video);
                    if (!prepareMediaRecorder()) {
                        Log.d("Record Video", "Fail in prepare media recorder");
                        getActivity().finish();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaRecorder.start();
                        }
                    });
                    recording = true;
                }
            } else if(view==switch_camera){
                if (!recording) {
                    int camerasNumber = Camera.getNumberOfCameras();
                    if (camerasNumber > 1) {
                        releaseCamera();
                        chooseCamera();
                    } else {
                        Toast.makeText(getActivity(),
                                "Sorry, your phone has only one camera!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };


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

    protected boolean prepareMediaRecorder()
    {
         // Works well
        mCamera.unlock();

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setPreviewDisplay(cameraPreview.getHolder().getSurface());
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        CamcorderProfile profile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT,CamcorderProfile.QUALITY_HIGH);
        profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
        profile.videoCodec = MediaRecorder.VideoEncoder.MPEG_4_SP;
        profile.videoFrameHeight = 240;
        profile.videoFrameWidth = 320;
        profile.videoBitRate = 15;

        mediaRecorder.setProfile(profile);
        mediaRecorder.setPreviewDisplay(cameraPreview.getHolder().getSurface());
        CameraPreview.setMediaRecorderOrientation(getActivity(), mediaRecorder);
        video = getActivity().getApplicationContext().getCacheDir().getAbsolutePath() + "/" + MiscFunction.getRandomFileName("Record") + ".mp4";
        mediaRecorder.setOutputFile(video);

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }

        return true;
    }


    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
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
    public void onPause() {
        super.onPause();
        releaseMediaRecorder();
        releaseCamera();
    }


    private boolean hasCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }


    public void chooseCamera() {
        if (cameraFront) {
            cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId);
                cameraPreview.refreshCamera(mCamera);
            }
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId);
                cameraPreview.refreshCamera(mCamera);
            }
        }
    }


    private int findFrontFacingCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    public int findBackFacingCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

}
