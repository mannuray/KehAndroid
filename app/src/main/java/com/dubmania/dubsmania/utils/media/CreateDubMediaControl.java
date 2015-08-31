package com.dubmania.dubsmania.utils.media;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.dubmania.dubsmania.R;

import java.io.IOException;

/**
 * Created by rat on 8/31/2015.
 */
public class CreateDubMediaControl extends LinearLayout {

    private Context mContext;
    private ViewGroup mAnchor;
    private View mRoot;
    private ProgressBar mProgress;
    private boolean mShowing;

    private ImageButton mPlayOriginal;

    private LinearLayout mRecordView;
    private ImageButton mPlayRecorded;
    private ImageButton mRecord;
    private ImageButton mNext;
    private ImageButton mPrevious;

    private AudioManager mAudioManager;
    private VideoManager mVideoManager;

    enum State {playingOriginal, playingRecorded, recording, pause }
    State mState = State.pause;

    public CreateDubMediaControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public void onFinishInflate() {
        if (mRoot != null)
            initControllerView(mRoot);
    }

    public void setMediaControllers(AudioManager mAudioManager, VideoManager mVideoManager) {
        this.mAudioManager = mAudioManager;
        this.mVideoManager = mVideoManager;
        this.mVideoManager.setOnCompletionListener(mCompletion);
    }

    public void setAnchorView(View view) {
        //mAnchor = view;

        LinearLayout.LayoutParams frameParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.create_dub_media_layout, null);

        initControllerView(mRoot);

        return mRoot;
    }

    private void initControllerView(View v) {
        mPlayOriginal = (ImageButton) v.findViewById(R.id.playOriginal);
        mPlayOriginal.setImageResource(R.drawable.play);
        mPlayOriginal.setOnClickListener(mPlayOriginalListener);

        mRecordView = (LinearLayout) v.findViewById(R.id.record);
        mPrevious = (ImageButton) v.findViewById(R.id.previous);
        mPrevious.setImageResource(R.drawable.previous);
        mPrevious.setOnClickListener(mPreviousListener);

        mPlayRecorded = (ImageButton) v.findViewById(R.id.playRecorded);
        mPlayRecorded.setImageResource(R.drawable.play);
        mPlayRecorded.setOnClickListener(mPlayRecordedListener);

        mRecord = (ImageButton) v.findViewById(R.id.recordDub);
        mRecord.setImageResource(R.drawable.record);
        mRecord.setOnClickListener(mRecordListener);

        mNext = (ImageButton) v.findViewById(R.id.next);
        mNext.setImageResource(R.drawable.next);
        mNext.setOnClickListener(mNextListener);
    }

    private VideoManager.OnCompletionCallback mCompletion = new VideoManager.OnCompletionCallback() {

        @Override
        public void onComplete() {
            if(mState == State.playingOriginal) {
                mPlayOriginal.setImageResource(R.drawable.play);
            }
            else if(mState == State.recording) {
                mRecord.setImageResource(R.drawable.record);
                try {
                    mAudioManager.pause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(mState == State.playingRecorded) {
                mPlayRecorded.setImageResource(R.drawable.play);
            }
            mState = State.pause;
        }
    };

    private View.OnClickListener mPlayOriginalListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(mState == State.pause) {
                mPlayOriginal.setImageResource(R.drawable.pause);
                mRecordView.setEnabled(false);
                mVideoManager.play(false);
                mState = State.playingOriginal;
            }
            else if(mState == State.playingOriginal) {
                mPlayOriginal.setImageResource(R.drawable.play);
                mRecordView.setEnabled(true);
                mVideoManager.pause();
                mState = State.pause;
            }
        }
    };

    private View.OnClickListener mPreviousListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(mState == State.pause) {
                mAudioManager.setPrevPos();
            }
        }
    };

    private View.OnClickListener mPlayRecordedListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(mState == State.pause) {
                mPlayRecorded.setImageResource(R.drawable.pause);
                long position = mAudioManager.getCurrentTime();
                mVideoManager.setPos((int) position);

                mPlayOriginal.setEnabled(false);
                mPrevious.setEnabled(false);
                mRecord.setEnabled(false);
                mNext.setEnabled(false);

                mState = State.playingRecorded;
                mAudioManager.play();
                mVideoManager.play(true);
            }
            else if(mState == State.playingRecorded) {
                mPlayRecorded.setImageResource(R.drawable.play);
                try {
                    mAudioManager.pause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mVideoManager.pause();

                mPlayOriginal.setEnabled(true);
                mPrevious.setEnabled(true);
                mRecord.setEnabled(true);
                mNext.setEnabled(true);

                mState = State.pause;
            }
        }
    };

    private View.OnClickListener mRecordListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(mState == State.pause) {
                mRecord.setImageResource(R.drawable.pause);
                long position = mAudioManager.getCurrentTime();
                mVideoManager.setPos((int) position);

                mPlayOriginal.setEnabled(false);
                mPrevious.setEnabled(false);
                mRecord.setEnabled(false);
                mNext.setEnabled(false);

                mState = State.recording;
                mVideoManager.play(true);
                try {
                    mAudioManager.record();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(mState == State.recording) {
                mRecord.setImageResource(R.drawable.record);
                try {
                    mAudioManager.pause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mVideoManager.pause();

                mPlayOriginal.setEnabled(true);
                mPrevious.setEnabled(true);
                mRecord.setEnabled(true);
                mNext.setEnabled(true);

                mState = State.pause;
            }
        }
    };

    private View.OnClickListener mNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(mState == State.pause) {
                mAudioManager.setNextPos();
            }
        }
    };
}
