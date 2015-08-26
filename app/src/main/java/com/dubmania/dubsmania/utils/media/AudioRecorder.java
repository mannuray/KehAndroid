package com.dubmania.dubsmania.utils.media;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

/**
 * Created by hardik.parekh on 8/3/2015.
 */
public class AudioRecorder {
    private MediaRecorder mRecorder;
    private boolean recordedFileAvailable = false;

    public AudioRecorder() {
    }

    public void startRecording(File mAudiofile) throws IOException {
        mRecorder = new MediaRecorder();
        mRecorder.reset();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mAudiofile.getAbsolutePath());
        mRecorder.prepare();
        mRecorder.start();
    }

    public void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        recordedFileAvailable = true;
    }

    public long getDuration() {
        if(recordedFileAvailable) {

        }
        return 0; // write code to get filed duraiton
    }
}
