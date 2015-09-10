package com.dubmania.dubsmania.report;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.feedbackevent.FragmentFeedbackCreateEvent;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.squareup.otto.Produce;

public class FeedbackActivity extends AppCompatActivity {

    private Long mVideoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Intent intent = getIntent();
        mVideoId = intent.getLongExtra(ConstantsStore.VIDEO_ID, 0);
        changeFragment(intent.getIntExtra(ConstantsStore.INTENT_REPORT_ACTION, ConstantsStore.INTENT_REPORT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
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

    private Fragment getFragment(int position) {

        switch (position) {
            case 0:
                return new ReportFragment();
            case 1:
                return new ImproveFragment();
        }
        return new ReportFragment();
    }

    @Produce
    public FragmentFeedbackCreateEvent onFragmentFeedbackCreateEvent() {
        return new FragmentFeedbackCreateEvent(this.mVideoId);
    }
}
