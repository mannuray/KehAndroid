package com.dubmania.dubsmania.main;

import android.content.Intent;
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

import com.dubmania.dubsmania.Adapters.VideoBoardListItem;
import com.dubmania.dubsmania.Adapters.VideoListItem;
import com.dubmania.dubsmania.Adapters.VideoPlayEvent;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.AddDiscoverVideoItemListEvent;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.AddTrendingBoardListEvent;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.AddTrendingVideoListEvent;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.CreateDubEvent;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.MyVideoItemShareEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.RecyclerViewScrollEndedEvent;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.TrendingViewScrollEndedEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.VideoBoardClickedEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.VideoFavriouteChangedEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.VideoItemMenuEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoBoardDownloaderCallback;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoBoardsDownloader;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoFavoriteMarker;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoListDownloader;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoListDownloaderCallback;
import com.dubmania.dubsmania.createdub.CreateDubActivity;
import com.dubmania.dubsmania.dialogs.VideoItemMenuDialog;
import com.dubmania.dubsmania.misc.AddLanguageActivity;
import com.dubmania.dubsmania.misc.PlayVideoActivity;
import com.dubmania.dubsmania.misc.SearchActivity;
import com.dubmania.dubsmania.misc.VideoBoardActivity;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.dubmania.dubsmania.utils.VideoSharer;
import com.loopj.android.http.PersistentCookieStore;
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

    public void changeLanguage(View v) {
        onNavigationDrawerItemSelected(4);
    }

    public void pushNotification(View v) {

    }

    @Subscribe
    public void onVideoItemMenuEvent(VideoItemMenuEvent event) {
        VideoItemMenuDialog dialog = new VideoItemMenuDialog();
        dialog.mVideoId = event.getId();
        dialog.show(getSupportFragmentManager(), "tag");
    }

    @Subscribe
    public void onMyVideoItemShareEvent(MyVideoItemShareEvent event) {
        new VideoSharer(this).showAlertDialog(event.getFilePath());
    }

    @Subscribe
    public void onRecyclerViewScrollEndedEvent(RecyclerViewScrollEndedEvent event) {
        new VideoListDownloader().downloadDiscoverVideos(event.getCurrent_page(), "mannuk", new VideoListDownloaderCallback() {
            @Override
            public void onVideosDownloadSuccess(ArrayList<VideoListItem> videos) {
                BusProvider.getInstance().post(new AddDiscoverVideoItemListEvent(videos));
            }

            @Override
            public void onVideosDownloadFailure() {

            }
        });
        Toast.makeText(getApplicationContext(), "scroll end message recived " + String.valueOf(event.getmId()), Toast.LENGTH_SHORT).show();

    }

    @Subscribe
    public void onTrendingViewScrollEndedEvent(TrendingViewScrollEndedEvent event) {
        // TO DO get user name
        mTrendingVideosDownloader.downloadTrendingVideos("India", 0, 4, "mannuk", new VideoListDownloaderCallback() {
            @Override
            public void onVideosDownloadSuccess(ArrayList<VideoListItem> videos) {
                BusProvider.getInstance().post(new AddTrendingVideoListEvent(videos));
            }

            @Override
            public void onVideosDownloadFailure() {

            }
        });

        new VideoBoardsDownloader(getApplicationContext()).getTrendingBoards("India", 0, 15, new VideoBoardDownloaderCallback() {
            @Override
            public void onVideoBoardsDownloadSuccess(ArrayList<VideoBoardListItem> boards) {
                BusProvider.getInstance().post(new AddTrendingBoardListEvent(boards));
            }

            @Override
            public void onVideosBoardsDownloadFailure() {

            }
        });

        Toast.makeText(getApplicationContext(), "scroll end message recived " + String.valueOf(event.getmId()), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onCreateDubEvent(CreateDubEvent event) {
        Intent intent = new Intent(this, CreateDubActivity.class);
        intent.putExtra(ConstantsStore.VIDEO_ID, event.getId());
        intent.putExtra(ConstantsStore.INTENT_VIDEO_TITLE, event.getTitle());
        startActivity(intent);
    }

    @Subscribe
    public void onnVideoFavriouteChangedEvent(VideoFavriouteChangedEvent event) {
        new VideoFavoriteMarker().markVavrioute(event.getId(), event.ismFavrioute());
    }

    @Subscribe
    public void onVideoBoardClickedEvent(VideoBoardClickedEvent event) {
        Intent intent = new Intent(this, VideoBoardActivity.class);
        intent.putExtra(ConstantsStore.INTENT_BOARD_ID, event.getId());
        intent.putExtra(ConstantsStore.INTENT_BOARD_NAME, event.getBoardName());
        intent.putExtra(ConstantsStore.INTENT_BOARD_USER_NAME, event.getBoardUsername());
        startActivity(intent);
    }

    @Subscribe
    public void onVideoPlayEvent(VideoPlayEvent event) {
        Intent intent = new Intent(this, PlayVideoActivity.class);
        intent.putExtra(ConstantsStore.INTENT_FILE_PATH, event.getFilePath());
        startActivity(intent);
    }
}
