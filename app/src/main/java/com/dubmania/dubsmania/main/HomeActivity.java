package com.dubmania.dubsmania.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dubmania.dubsmania.Adapters.ListItem;
import com.dubmania.dubsmania.Adapters.VideoBoardListItem;
import com.dubmania.dubsmania.Adapters.VideoListItem;
import com.dubmania.dubsmania.Adapters.VideoPlayEvent;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.AddDiscoverItemListEvent;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.AddTrendingVideoListEvent;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.AddVideoBoardListEvent;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.MyVideoItemShareEvent;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.TrendingViewScrollEndedEvent;
import com.dubmania.dubsmania.communicator.eventbus.mainevent.VideoBoardScrollEndedEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.CreateDubEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.RecyclerViewScrollEndedEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.VideoBoardClickedEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.VideoFavriouteChangedEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.VideoItemMenuEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoAndBoardDownloader;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoAndBoardDownloaderCallback;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoBoardDownloaderCallback;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoBoardsDownloader;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoFavoriteMarker;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoListDownloader;
import com.dubmania.dubsmania.communicator.networkcommunicator.VideoListDownloaderCallback;
import com.dubmania.dubsmania.createdub.CreateDubActivity;
import com.dubmania.dubsmania.dialogs.VideoItemPopupMenu;
import com.dubmania.dubsmania.misc.AddLanguageActivity;
import com.dubmania.dubsmania.misc.PlayVideoActivity;
import com.dubmania.dubsmania.misc.SearchActivity;
import com.dubmania.dubsmania.misc.VideoBoardActivity;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.dubmania.dubsmania.utils.ScrimInsetsFrameLayout;
import com.dubmania.dubsmania.utils.SessionManager;
import com.dubmania.dubsmania.utils.UtilsDevice;
import com.dubmania.dubsmania.utils.UtilsMiscellaneous;
import com.dubmania.dubsmania.utils.VideoSharer;
import com.loopj.android.http.PersistentCookieStore;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    FragmentManager mFragmentManager;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Realm.deleteRealmFile(getApplicationContext());
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        DubsmaniaHttpClient.setCookieStore(myCookieStore);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        mFragmentManager = getSupportFragmentManager();
        init_navigator();

        findViewById(R.id.navigation_drawer_items_list_linearLayout_create_dub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new PagerFragment(), "VidCraft");
            }
        });

        findViewById(R.id.navigation_drawer_items_list_linearLayout_add_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new AddVideoFragment(), "Add Video");
            }
        });

        findViewById(R.id.navigation_drawer_items_list_linearLayout_my_dubs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new MyDubsFragment(), "My Dubs");
            }
        });

        findViewById(R.id.navigation_drawer_items_list_linearLayout_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new SettingFragment(), "Settings");
            }
        });

        changeFragment(new PagerFragment(), "VidCraft");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
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

    private void changeFragment(Fragment fragment, String title) {
        mFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
        mDrawerLayout.closeDrawers();
    }

    private void init_navigator(){
        // Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_DrawerLayout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primaryDark));
        ScrimInsetsFrameLayout mScrimInsetsFrameLayout = (ScrimInsetsFrameLayout) findViewById(R.id.main_activity_navigation_drawer_rootLayout);

        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle
                (
                        this,
                        mDrawerLayout,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close
                ) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Disables the burger/arrow animation by default
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mActionBarDrawerToggle.syncState();

        // Navigation Drawer layout width
        int possibleMinDrawerWidth = UtilsDevice.getScreenWidth(this) -
                UtilsMiscellaneous.getThemeAttributeDimensionSize(this, android.R.attr.actionBarSize);
        int maxDrawerWidth = getResources().getDimensionPixelSize(R.dimen.navigation_drawer_max_width);

        mScrimInsetsFrameLayout.getLayoutParams().width = Math.min(possibleMinDrawerWidth, maxDrawerWidth);
        getSupportActionBar().setTitle("VidCraft");
    }

    public void pushNotification(View v) {

    }

    @Subscribe
    public void onVideoItemMenuEvent(VideoItemMenuEvent event) {
        new VideoItemPopupMenu(this, event.getId(), event.getView()).show();
    }

    @Subscribe
    public void onMyVideoItemShareEvent(MyVideoItemShareEvent event) {
        new VideoSharer(this).showAlertDialog(event.getFilePath());
    }

    @Subscribe
    public void onRecyclerViewScrollEndedEvent(RecyclerViewScrollEndedEvent event) {
        new VideoAndBoardDownloader(getApplicationContext()).discover(event.getCurrent_page(), new SessionManager(this).getId(), new VideoAndBoardDownloaderCallback() {
            @Override
            public void onVideoAndBoardDownloaderSuccess(ArrayList<ListItem> videos) {
                BusProvider.getInstance().post(new AddDiscoverItemListEvent(videos));
            }

            @Override
            public void onVideoAndBoardDownloaderFailure() {

            }
        });

        Toast.makeText(getApplicationContext(), "scroll end message recived " + String.valueOf(event.getmId()), Toast.LENGTH_SHORT).show();

    }

    @Subscribe
    public void onTrendingViewScrollEndedEvent(TrendingViewScrollEndedEvent event) {
        // TO DO get user name
        new VideoListDownloader().downloadTrendingVideos("India", 0, 4, new SessionManager(this).getUser(), new VideoListDownloaderCallback() {
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
    public void onVideoBoardScrollEndedEvent(VideoBoardScrollEndedEvent event) {
        SessionManager manager = new SessionManager(this);
        if(!manager.isLoggedIn()) {
            Log.d("Board ,", "not logged in");
            return;
        }

        new VideoBoardsDownloader(getApplicationContext()).getUserBoards(manager.getUser(), new VideoBoardDownloaderCallback() {
            @Override
            public void onVideoBoardsDownloadSuccess(ArrayList<VideoBoardListItem> boards) {
                BusProvider.getInstance().post(new AddVideoBoardListEvent(boards));
            }

            @Override
            public void onVideosBoardsDownloadFailure() {

            }
        });
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
