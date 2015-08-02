package com.dubmania.dubsmania.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dubmania.dubsmania.Adapters.VideoListItem;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.AddDiscoverVideoItemListEvent;
import com.dubmania.dubsmania.communicator.BusProvider;
import com.dubmania.dubsmania.communicator.RecyclerViewScrollEndedEvent;
import com.dubmania.dubsmania.communicator.VideoItemMenuEvent;
import com.dubmania.dubsmania.dialogs.VideoItemMenuDialog;
import com.dubmania.dubsmania.misc.LanguageActivity;
import com.dubmania.dubsmania.misc.SearchActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, getFragment(position))
                .commit();
    }

    Fragment getFragment(int position) {
        switch (position) {
            case 0:
                return new PagerFragment();
            case 1:
                return new AddVideoFragment();
            case 2:
                return new MyDubsFragment();
            case 3:
                return new SettingFragment();
            default:
                return new PagerFragment();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startBrowser(String uri) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);
    }

    public void ChangeLanguage(View v) {

    }

    public void feedback(View v) {
        startBrowser("http://www.google.com");
    }

    public void libraries(View v) {
        startBrowser("http://www.google.com");
    }

    public void termofuse(View v) {
        startBrowser("http://www.google.com");
    }

    public void privacypolicy(View v) {
        startBrowser("http://www.google.com");
    }

    public void pushNotification(View v) {

    }
    public void language(View v) {
        Intent intent = new Intent(this, LanguageActivity.class);
        startActivity(intent);
    }

    //message  sscribed
    @Subscribe
    public void onVideoItemMenuEvent(VideoItemMenuEvent event) {
        VideoItemMenuDialog dialog = new VideoItemMenuDialog();
        dialog.show(getSupportFragmentManager(), "tag");
    }

    @Subscribe
    public void onRecyclerViewScrollEndedEvent(RecyclerViewScrollEndedEvent event) {
        ArrayList<VideoListItem> mVideoItemList = new ArrayList<VideoListItem>(Arrays.asList(
                new VideoListItem("heros", "mannu", false),
                new VideoListItem("heros1", "mannu", false),
                new VideoListItem("heros2", "mannu", false),
                new VideoListItem("heros3", "prashant", false)
        ));
        BusProvider.getInstance().post(new AddDiscoverVideoItemListEvent(mVideoItemList));
        Toast.makeText(getApplicationContext(), "scroll end message recived " + String.valueOf(event.getmId()), Toast.LENGTH_SHORT).show();

    }
}
