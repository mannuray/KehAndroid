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
import java.util.Locale;

/**
 * Created by rat on 8/25/2015.
 */
public class AudioManager {

    private ArrayList<Audio> mAudioFlileList;
    private MediaPlayer mAudioPlayer;
    private int mRecordingPosition = 0;
    private int mPlayingPosition = 0;
    private boolean isRecordingAvailable = false;
    private boolean isPlayRecordingIntilized = false;
    //private long mCurrentTime = 0;
    private AudioRecorder mAudioRecorder;
    private Context mContext;
    private OnCompletionCallback mCallback = null;

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

    public void setOnCompletionListener(OnCompletionCallback mCallback) {
        this.mCallback = mCallback;
    }

    public void record() throws IOException {

        if(mRecordingPosition < mAudioFlileList.size()) {
            // remove all previos records
            if(mRecordingPosition == 0) {
                mAudioFlileList.clear();
                isRecordingAvailable = false;
                isPlayRecordingIntilized = false;
            }
            else {
                for (int i = mAudioFlileList.size() - 1; i > mRecordingPosition; i--) {
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
            Audio audio;
            if(mAudioFlileList.size() <= 0 )
                audio = new Audio(mAudioRecorder.stopRecording(), 0);
            else
                audio = new Audio(mAudioRecorder.stopRecording(), mAudioFlileList.get(mRecordingPosition).getStartTime() + mAudioFlileList.get(mRecordingPosition).getDuration());
            mAudioFlileList.add(audio);
            mRecordingPosition++;
        }
        else if (mState == State.playing) {
            mAudioPlayer.pause();
        }
        isRecordingAvailable = true;
        mState = State.pause;
    }

    public void setPlayingPos(int pos) {
        if(!(pos < 0 || pos >= mAudioFlileList.size())) {
            mPlayingPosition = pos;
            isPlayRecordingIntilized = false;
        }
    }

    public void setPrevPos() {
        if(mRecordingPosition < 0)
            return;
        mRecordingPosition--;
        setPlayingPos(--mPlayingPosition);
    }

    public void setNextPos() {
        if(mRecordingPosition > mAudioFlileList.size())
            return;
        mRecordingPosition++;
        setPlayingPos(++mPlayingPosition);

    }

    public void play() {
        if(mState != State.pause)
            return;
        try {
            if(initializePlayRecording()) {
                mAudioPlayer.start();
                mState = State.playing;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean initializePlayRecording() throws IOException {
        if(!isRecordingAvailable)
            return false;
        if(isPlayRecordingIntilized)
            return true;
        mAudioPlayer.reset();
        mAudioPlayer.setDataSource(mAudioFlileList.get(mPlayingPosition).getAudioFile().getAbsolutePath());
        mAudioPlayer.prepare();
        isPlayRecordingIntilized = true;
        return true;
    }

    private void changeTrack() {
        try {
            if(mPlayingPosition >= mAudioFlileList.size()) {
                mPlayingPosition = 0; // code for calling compleation callback if set
                isPlayRecordingIntilized = false;
                mState = State.pause;
                if(mCallback != null)
                    mCallback.onComplete();
                return;
            }
            initializePlayRecording();
            mAudioPlayer.start();
            mPlayingPosition++;
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public long getCurrentTime() {
        if(mAudioFlileList.size() <= 0)
            return 0;
        return mAudioFlileList.get(mRecordingPosition - 1).getStartTime();
    }

    private File getRandomFileName() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_hhmmss_SSS", Locale.getDefault());
        return File.createTempFile(String.format("Audio_File_%s", sdf.format(new Date())), ".mp4", mContext.getCacheDir());
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
