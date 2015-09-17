package com.dubmania.vidcraft.utils.media;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by rat on 9/17/2015.
 */
public class VideoRecorder implements SurfaceHolder.Callback{
    private SurfaceHolder mSurfaceHolder;
    public MediaRecorder mRecorder = new MediaRecorder();
    private boolean recordAvailable = false;
    private boolean mRecording = false;

    private String mVideoFilePath;
    private Camera mCamera;

    public VideoRecorder(SurfaceHolder surfaceHolder, String video) {
        this.mSurfaceHolder = surfaceHolder;
        this.mVideoFilePath = video;
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCamera = Camera.open();
    }

    public void startRecording() throws IOException
    {
        mRecorder = new MediaRecorder();  // Works well
        mCamera.unlock();

        mRecorder.setCamera(mCamera);

        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        CamcorderProfile profile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT,CamcorderProfile.QUALITY_HIGH);
        profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
        profile.videoCodec = MediaRecorder.VideoEncoder.MPEG_4_SP;
        profile.videoFrameHeight = 240;
        profile.videoFrameWidth = 320;
        //profile.videoBitRate = 15;

        mRecorder.setProfile(profile);
        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mRecorder.setOutputFile(mVideoFilePath);

        mRecorder.prepare();
        mRecorder.start();
        mRecording = true;
    }

    public void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        //mCamera.release();
        mRecording = false;
    }

    public boolean isRecording() {
        return mRecording;
    }

    private void releaseMediaRecorder(){
        if (mRecorder != null) {
            mRecorder.reset();   // clear recorder configuration
            mRecorder.release(); // release the recorder object
            mRecorder = null;
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
            mCamera.startPreview();
        }
        else {
            //Toast.makeText(getActivity().getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
    }
}
