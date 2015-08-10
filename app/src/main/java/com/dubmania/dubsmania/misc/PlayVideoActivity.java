package com.dubmania.dubsmania.misc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.dubmania.dubsmania.R;

public class PlayVideoActivity extends AppCompatActivity {

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        Intent intent = getIntent();
        Uri mUri = Uri.parse(intent.getStringExtra(ConstantsStore.SHARE_FILE_PATH));
        final VideoView mVideoView = (VideoView) findViewById(R.id.play_video_view);
        mVideoView.setVideoURI(mUri);
        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isPlaying) {
                    mVideoView.start();
                    isPlaying = true;
                    Toast.makeText(getApplicationContext(),"stng llabbae ", Toast.LENGTH_SHORT).show();
                }
                else {
                    mVideoView.pause();
                    isPlaying = false;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
