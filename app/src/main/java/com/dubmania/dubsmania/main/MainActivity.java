package com.dubmania.dubsmania.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dubmania.dubsmania.Adapters.VideoListItem;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.AddDiscoverVideoItemListEvent;
import com.dubmania.dubsmania.communicator.eventbus.AddTrendingVideoListEvent;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.CreateDubEvent;
import com.dubmania.dubsmania.communicator.eventbus.MyVideoItemShareEvent;
import com.dubmania.dubsmania.communicator.eventbus.RecyclerViewScrollEndedEvent;
import com.dubmania.dubsmania.communicator.eventbus.TrendingViewScrollEndedEvent;
import com.dubmania.dubsmania.communicator.eventbus.VideoFavriouteChangedEvent;
import com.dubmania.dubsmania.communicator.eventbus.VideoItemMenuEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoFavoriteMarker;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoListDownloader;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoListDownloaderCallback;
import com.dubmania.dubsmania.createdub.CreateDubActivity;
import com.dubmania.dubsmania.dialogs.VideoItemMenuDialog;
import com.dubmania.dubsmania.misc.AddLanguageActivity;
import com.dubmania.dubsmania.misc.SearchActivity;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

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
    private AlertDialog shareDialog;
    private VideoListDownloader mTrendingVideosDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        DubsmaniaHttpClient.setCookieStore(myCookieStore);
        mTrendingVideosDownloader = new VideoListDownloader();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        String[] mMessengerList = getResources()
                .getStringArray(R.array.messenger_list);
        shareDialog = getShareAlertDialog(mMessengerList);

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
            case 4:
                return new LanguageFragment();
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        switch(id) {
            case R.id.action_search:
                intent = new Intent(this, SearchActivity.class);
                break;
            case R.id.action_add_language:
                intent = new Intent(this, AddLanguageActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return true;
    }

    private void startBrowser(String uri) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);
    }

    public void changeLanguage(View v) {
        onNavigationDrawerItemSelected(4);
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

    //message  sscribed
    @Subscribe
    public void onVideoItemMenuEvent(VideoItemMenuEvent event) {
        VideoItemMenuDialog dialog = new VideoItemMenuDialog();
        dialog.show(getSupportFragmentManager(), "tag");
    }

    @Subscribe
    public void onMyVideoItemShareEvent(MyVideoItemShareEvent event) {
        shareDialog.show();
    }

    @Subscribe
    public void onRecyclerViewScrollEndedEvent(RecyclerViewScrollEndedEvent event) {

        ArrayList<VideoListItem> mVideoItemList = new ArrayList<>();
        BusProvider.getInstance().post(new AddDiscoverVideoItemListEvent(mVideoItemList));
        Toast.makeText(getApplicationContext(), "scroll end message recived " + String.valueOf(event.getmId()), Toast.LENGTH_SHORT).show();

    }

    @Subscribe
    public void onTrendingViewScrollEndedEvent(TrendingViewScrollEndedEvent event) {
        RequestParams params = new RequestParams();
        params.add("start", String.valueOf(0));
        params.add("end", String.valueOf(4));
        params.add("region", "India");
        params.add(ConstantsStore.PARAM_USER, "mannuk");// TO DO get user name
        mTrendingVideosDownloader.downloadVideos(ConstantsStore.GET_TRENDING_VIDEOS_URL, params, new VideoListDownloaderCallback() {
            @Override
            public void onVideosDownloadSuccess(ArrayList<VideoListItem> videos) {
                BusProvider.getInstance().post(new AddTrendingVideoListEvent(videos));
            }

            @Override
            public void onVideosDownloadFailure() {

            }
        });

        Toast.makeText(getApplicationContext(), "scroll end message recived " + String.valueOf(event.getmId()), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onCreateDubEvent(CreateDubEvent event) {
        Intent intent = new Intent(this, CreateDubActivity.class);
        intent.putExtra(ConstantsStore.VIDEO_ID, event.getId());
        startActivity(intent);
    }

    @Subscribe
    public void onnVideoFavriouteChangedEvent(VideoFavriouteChangedEvent event) {
        new VideoFavoriteMarker().markVavrioute(event.getId(), event.ismFavrioute());
    }

    // private functions
    private AlertDialog getShareAlertDialog(String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.share_alert_tite);
        //builder.setCancelable(true);
        builder.setNegativeButton(R.string.share_alert_cancel, (dialog, which) -> {
            shareDialog.dismiss();
        });
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
            }
        });
        return builder.create();
    }
}
