package com.dubmania.vidcraft.utils.media;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.VideoView;

import java.io.File;

/**
 * Created by rat on 8/25/2015.
 */
public class VideoManager {
    private File mVideoFile = null;
    private VideoView mVideoView;
    private MediaPlayer mVideoPlayer;

    public VideoManager(final VideoView mVideoView) {
        this.mVideoView = mVideoView;
    }

    public void setOnCompletionListener(final OnCompletionCallback mCallback) {
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mCallback.onComplete();
            }
        });
    }

    public void setOnPrepareListener(final OnPrepareCallback mCallback) {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                mVideoPlayer = mediaPlayer;
                mCallback.onPrepare();
            }
        });
    }

    public void setVideoFilePath(File mVideoFilePath) {
        mVideoFile = mVideoFilePath;
        try {
            mVideoView.setVideoURI(Uri.parse(mVideoFile.getAbsolutePath()));
        } catch (Exception e) {
            Log.e("Video File Error", e.getMessage());
            e.printStackTrace();
        }
        mVideoView.requestFocus();
    }

    public void play(boolean mute) {
        if(mVideoFile == null)
            return;
        mute(mute);
        mVideoView.start();
    }

    public void pause() {
        if(mVideoFile == null)
            return;
        mVideoView.pause();
    }

    // for test see if we may neeed it further on too
    public int getPos() {
        return  mVideoPlayer.getCurrentPosition();
    }

    public void start(boolean mute) {
        if(mVideoFile == null)
            return;
        mute(mute);
        mVideoView.seekTo(0);
        mVideoView.start();
    }

    public void setPos(int pos) {
        if(mVideoFile == null)
            return;
        mVideoView.seekTo(pos);
    }

    public int getDuration() {
        if(mVideoFile == null)
            return 0;
        return mVideoView.getDuration();
    }

    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    private void mute(boolean mute) {
        if(mVideoFile == null)
            return;
        if(mute)
            mVideoPlayer.setVolume(0f, 0f);
        else
            mVideoPlayer.setVolume(1.0f, 1.0f);
    }

    public static abstract class OnCompletionCallback {
        public abstract void onComplete();
    }

    public static abstract class OnPrepareCallback {
        public abstract void onPrepare();
    }
}
