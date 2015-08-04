package com.dubmania.dubsmania.misc;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.dubmania.dubsmania.Adapters.ImageAdapter;
import com.dubmania.dubsmania.R;

import java.util.ArrayList;

public class AddVideoBoardActivity extends ActionBarActivity {
    private EditText mBoardName;
    private GridView mBoardIcon;
    private TypedArray navMenuIcons;
    private ArrayList<Integer> mThumbIds;
    private int mIconId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_board);
        mBoardName = (EditText) findViewById(R.id.add_video_board_name_edit);
        mBoardIcon = (GridView) findViewById(R.id.add_video_board_gridView);
        Button mAddBoardButton = (Button) findViewById(R.id.add_video_board_add_button);

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mThumbIds = new ArrayList<Integer>();
        for(int i = 0; i < 10; i++) {
            mThumbIds.add(navMenuIcons.getResourceId(0, -1));
        }

        mBoardIcon.setAdapter(new ImageAdapter(getApplicationContext(), mThumbIds));
        mBoardIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIconId = position;
            }
        });
        mAddBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
