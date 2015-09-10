package com.dubmania.dubsmania.misc;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.dubmania.dubsmania.Adapters.ImageAdapter;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoBoardCreaterCallback;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoBoardCreator;
import com.dubmania.dubsmania.utils.SessionManager;

import java.util.ArrayList;

public class AddVideoBoardActivity extends AppCompatActivity {
    private EditText mBoardName;
    private int mIconId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_board);
        new SessionManager(this).checkLogin(new SessionManager.LoginListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure() {
                finish();
            }
        });
        mBoardName = (EditText) findViewById(R.id.add_video_board_name_edit);
        GridView mBoardIcon = (GridView) findViewById(R.id.add_video_board_gridView);

        TypedArray mBoardIcons = getResources()
                .obtainTypedArray(R.array.video_board_icons);
        ArrayList<Integer> mThumbIds = new ArrayList<>();
        for(int i = 0; i < mBoardIcons.length(); i++) {
            mThumbIds.add(mBoardIcons.getResourceId(i, -1));
        }
        mBoardIcons.recycle();

        mBoardIcon.setAdapter(new ImageAdapter(getApplicationContext(), mThumbIds));
        mBoardIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIconId = position;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_video_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    public void addVideoBroard(View v) {
        new VideoBoardCreator().addVideoBoard(mBoardName.getText().toString(), mIconId, new VideoBoardCreaterCallback() {
            @Override
            public void onVideoBoardCreateSuccess() {
                finish();
            }

            @Override
            public void onVideoBoardCreateFailure() {
                //
            }
        });
    }
}
