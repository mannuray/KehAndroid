package com.dubmania.dubsmania.utils.media;

import android.content.Context;
import android.media.MediaPlayer;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rat on 8/25/2015.
 */
public class AudioManager {

    private ArrayList<Audio> mAudioFlileList;
    private MediaPlayer mAudioPlayer;
    private int mRecordingPosition = 0;
    private int mPlayingPosition = 0;
    private long mCurrentTime = 0;
    private long mRecordDuration; // check to see if we have complete voice record
    private AudioRecorder mAudioRecorder;
    private Context mContext;
    private OnCompletionCallback mCallback = null;

    enum State {recording, playing, pause}
    State mState = State.pause;

    public AudioManager(Context mContext, OnCompletionCallback mCallback) {
        this.mCallback = mCallback;
        this.mContext = mContext;
        mAudioFlileList = new ArrayList<>();
        mAudioRecorder = new AudioRecorder();
        mAudioPlayer = new MediaPlayer();
        mAudioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                changeTrack();
            }
        });
    }

    public void record() throws IOException {

        if(mRecordingPosition < mAudioFlileList.size()) {
            // remove all previos records
            if(mRecordingPosition == 0) {
                mAudioFlileList.clear();
            }
            else {
                for (int i = mAudioFlileList.size() - 1; i > mRecordingPosition; i--) {
                    mCurrentTime -= mAudioFlileList.get(i).getDuration();
                    mAudioFlileList.remove(i);
                }
            }
        }
        File audioFile = getRandomFileName();
        mAudioRecorder.startRecording(audioFile);
        mState = State.recording;
    }

    public void pause() throws IOException {
        if(mState == State.recording) {
            Audio audio = new Audio(mAudioRecorder.stopRecording(), mCurrentTime);
            mCurrentTime += audio.getDuration();
            mAudioFlileList.add(audio);
            mRecordingPosition++;
        }
        else if (mState == State.playing) {
            mAudioPlayer.pause();
        }
        mState = State.pause;
    }

    public void setPrevPos() {
        if(mRecordingPosition < 0)
            return;
        mRecordingPosition--;
        mCurrentTime = mAudioFlileList.get(mRecordingPosition).getStartTime();
    }

    public void setNextPos() {
        if(mRecordingPosition > mAudioFlileList.size())
            return;
        mRecordingPosition++;
        mCurrentTime = mAudioFlileList.get(mRecordingPosition).getStartTime();
    }

    public void play() {
        if(mState != State.pause)
            return;
        mAudioPlayer.start();
        mState = State.playing;
    }

    public File prepareAudio() {
        if(mAudioFlileList.isEmpty())
            return null;
        List<Track> audioTracks = new LinkedList<>();
        for(Audio audio: mAudioFlileList) {
            Track t = audio.getTrack();
            audioTracks.add(t);
        }

        Movie audio = new Movie();
        try {
            audio.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            File output = getRandomFileName();
            Container mContainer =  new DefaultMp4Builder().build(audio);
            WritableByteChannel wbc = new FileOutputStream(output).getChannel();
            mContainer.writeContainer(wbc);
            return output;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setRecordDuration(long mRecordDuration) {
        this.mRecordDuration = mRecordDuration;
    }

    public long getCurrentTime() {
        return mCurrentTime;
    }

    private void changeTrack() {
        try {
            if(mPlayingPosition >= mAudioFlileList.size()) {
                mPlayingPosition = 0; // code for calling compleation callback if set
                mState = State.pause;
                if(mCallback != null)
                    mCallback.onComplete();
                return;
            }
            mAudioPlayer.reset();
            mAudioPlayer.setDataSource(mAudioFlileList.get(mPlayingPosition).getAudioFile().getAbsolutePath());
            mAudioPlayer.prepare();
            mAudioPlayer.start();
            mPlayingPosition++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getRandomFileName() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_hhmmss_SSS");
        return File.createTempFile(String.format("Audio_File_%s", sdf.format(new Date())), "mp4", mContext.getCacheDir());
    }


    public static class Audio {
        private long mStartTime;
        private File mAudioFile;
        private Track mTrack;

        public Audio(File mAudioFile, long mStartTime) throws IOException {
            this.mAudioFile = mAudioFile;
            this.mStartTime = mStartTime;
            mTrack = MovieCreator.build(mAudioFile.getAbsolutePath()).getTracks().get(0);
        }

        public Track getTrack() {
            return mTrack;
        }

        public File getAudioFile() {
            return mAudioFile;
        }

        public long getStartTime() {
            return mStartTime;
        }

        public long getDuration() {
            return mTrack.getDuration();
        }
    }

    public static abstract class OnCompletionCallback {
        public abstract void onComplete();
    }
}
