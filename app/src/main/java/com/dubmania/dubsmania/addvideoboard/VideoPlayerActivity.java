package com.dubmania.dubsmania.addvideoboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.OnVideoDownloadEvent;
import com.dubmania.dubsmania.misc.AudioRecorder;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoDownloadClient;
import com.squareup.otto.Subscribe;

/**
 * Created by hardik.parekh on 8/3/2015.
 */
public class VideoPlayerActivity extends Activity implements MediaPlayer.OnCompletionListener {

    private VideoView _UIVideo;
    private AudioRecorder recorder;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.video_player_view);
        _UIVideo = (VideoView) findViewById(R.id.video);
        recorder = new AudioRecorder(this);
        new VideoDownloadClient().downloadVideo("big_buck_bunny.mp4");
        BusProvider.getInstance().register(this);
    }

    public void mute() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }

    public void unmute() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //Add Video and audio save logic here.
//        recorder.stopRecording();
        unmute();
        finish();
    }

    @Subscribe
    public void getMessage(OnVideoDownloadEvent event) {
        mute();
        /*try {
            recorder.startRecording();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
        _UIVideo.setVideoPath(event.getSavedLocation());
        android.widget.MediaController mediaController = new
                android.widget.MediaController(this);
        mediaController.setAnchorView(_UIVideo);
        _UIVideo.setMediaController(mediaController);
        _UIVideo.setOnCompletionListener(this);
        _UIVideo.requestFocus();
        _UIVideo.start();
    }
}
