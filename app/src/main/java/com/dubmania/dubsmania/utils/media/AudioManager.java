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
    private int mCurrentPos = 0;
    private long mCurrentTime = 0;
    private long mRecordDuration; // chech to see if we have complete voice record
    private AudioRecorder mAudioRecorder;
    private Context mContext;

    enum State {recording, playing, pause}
    State mState = State.pause;

    public AudioManager(Context mContext) {
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

        if(mCurrentPos < mAudioFlileList.size()) {
            // remove all previos records
            for(int i = mAudioFlileList.size() - 1; i > mCurrentPos; i--) {
                mCurrentTime -= mAudioFlileList.get(i).getAudioLength();
                mAudioFlileList.remove(i);
            }
        }
        File audioFile = getRandomFileName();
        mAudioFlileList.add(new Audio(audioFile, mCurrentTime));
        mAudioRecorder.startRecording(audioFile);
        mState = State.recording;
    }

    public void pause(){
        if(mState == State.recording) {
            mAudioRecorder.stopRecording();
            long duration = mAudioRecorder.getDuration();
            mAudioFlileList.get(mCurrentPos).setAudioLength(duration);
            mCurrentTime += duration;
            mCurrentPos++;
        }
        else if (mState == State.playing) {
            mAudioPlayer.pause();
        }
        mState = State.pause;
    }

    public void setPrevPos() {
        if(mCurrentPos < 0)
            return;
        mCurrentPos--;
        mCurrentTime = mAudioFlileList.get(mCurrentPos).getStartTime();
    }

    public void setNextPos() {
        if(mCurrentPos > mAudioFlileList.size())
            return;
        mCurrentPos++;
        mCurrentTime = mAudioFlileList.get(mCurrentPos).getStartTime();
    }

    public void play() {
        mAudioPlayer.start();
        mState = State.playing;
    }

    public File prepareAudio() {
        if(mAudioFlileList.isEmpty())
            return null;

        List<Track> audioTracks = new LinkedList<Track>();
        for(Audio audio: mAudioFlileList) {
            try {
                Movie clip = MovieCreator.build(audio.getAudioFile().getAbsolutePath());
                Track t = clip.getTracks().get(0);
                audioTracks.add(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            if(mCurrentPos > mAudioFlileList.size()) {
                mCurrentPos = 0; // code for calling compleation callback if set
                mState = State.pause;
                return;
            }
            mAudioPlayer.setDataSource(mAudioFlileList.get(mCurrentPos).getAudioFile().getAbsolutePath());
            mAudioPlayer.prepare();
            mAudioPlayer.start();
            mCurrentPos++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getRandomFileName() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_hhmmss_SSS");
        return File.createTempFile(String.format("Audio_File_%s", sdf.format(new Date())), "mp4", mContext.getCacheDir());
    }


    public static class Audio {
        private File mAudioFile;
        private long mAudioLength;
        private long mStartTime;

        public Audio(File mAudioFile, long mStartTime) {
            this.mAudioFile = mAudioFile;
            this.mStartTime = mStartTime;
        }

        public File getAudioFile() {
            return mAudioFile;
        }

        public long getAudioLength() {
            return mAudioLength;
        }

        public void setAudioLength(long mAudioLength) {
            this.mAudioLength = mAudioLength;
        }

        public long getStartTime() {
            return mStartTime;
        }
    }
}
