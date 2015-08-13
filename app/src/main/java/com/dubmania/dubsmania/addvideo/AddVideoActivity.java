package com.dubmania.dubsmania.addvideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dubmania.dubsmania.Adapters.VideoPlayEvent;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.misc.PlayVideoActivity;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.squareup.otto.Subscribe;

public class AddVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        Intent intent = getIntent();
        changeFragment(intent.getIntExtra(ConstantsStore.INTENT_ADD_VIDEO_ACTION, ConstantsStore.INTENT_ADD_VIDEO_RECORD));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_video) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    private void changeFragment(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, getFragment(position))
                .commit();
    }

    Fragment getFragment(int position) {

        switch (position) {
            case 0:
                return new RecordVideoPagerFragment();
            case 1:
                return new ImportVideoPagerFragment();
        }
        return new RecordVideoPagerFragment();
    }

    @Subscribe
    public void onVideoPlayEvent(VideoPlayEvent event) {
        Intent intent = new Intent(this, PlayVideoActivity.class);
        intent.putExtra(ConstantsStore.INTENT_FILE_PATH, event.getFilePath());
        startActivity(intent);
    }
}
