package com.dubmania.vidcraft.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.Adapters.ListItem;
import com.dubmania.vidcraft.Adapters.VideoBoardListItem;
import com.dubmania.vidcraft.Adapters.VideoListItem;
import com.dubmania.vidcraft.Adapters.VideoPlayEvent;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.AddDiscoverItemListEvent;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.AddTrendingVideoListEvent;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.AddVideoBoardListEvent;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.MyVideoItemShareEvent;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.TrendingViewScrollEndedEvent;
import com.dubmania.vidcraft.communicator.eventbus.mainevent.VideoBoardScrollEndedEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.CreateDubEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.RecyclerViewScrollEndedEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoBoardClickedEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoBoardDeletedEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoFavoriteChangedEvent;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoItemMenuEvent;
import com.dubmania.vidcraft.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoAndBoardDownloader;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoAndBoardDownloaderCallback;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoBoardDownloaderCallback;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoBoardsDownloader;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoFavoriteMarker;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoListDownloader;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoListDownloaderCallback;
import com.dubmania.vidcraft.dialogs.VideoItemPopupMenu;
import com.dubmania.vidcraft.misc.PlayVideoActivity;
import com.dubmania.vidcraft.misc.SearchActivity;
import com.dubmania.vidcraft.misc.VideoBoardActivity;
import com.dubmania.vidcraft.utils.ActivityStarter;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.InstalledLanguage;
import com.dubmania.vidcraft.utils.SessionManager;
import com.dubmania.vidcraft.utils.VideoSharer;
import com.loopj.android.http.PersistentCookieStore;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FragmentManager mFragmentManager;
    private DrawerLayout mDrawerLayout;
    private ArrayList<Long> languages;
    private NavigationView navigationView;

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
        init_languages();



        final Intent intent = getIntent();
        final String action = intent.getAction();

        if ("com.dubmania.action.VIEW".equals(action)) {
            Log.i("ezp", "EXTRA: "+intent.getExtras().getString("id"));
            ActivityStarter.createDub(this, new Long(intent.getExtras().getString("id")), " as of now" );
        }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.i("Board", "Add video booard result recived " + requestCode + " " + resultCode);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            if(data.getBooleanExtra(ConstantsStore.INTENT_BOARD_DELETED, false)) {
                BusProvider.getInstance().post(new VideoBoardDeletedEvent(data.getLongExtra(ConstantsStore.INTENT_BOARD_ID, 0)));
            }
        }
        // this is because onActivityResult for fragment is not firing, don't know why request code is behaving in such maner
        if (requestCode == 196610 && resultCode == Activity.RESULT_OK && data != null) {
            String boardName = data.getStringExtra(ConstantsStore.INTENT_BOARD_NAME);
            Long id = data.getLongExtra(ConstantsStore.INTENT_BOARD_ID, 0);
            int iconId = data.getIntExtra(ConstantsStore.INTENT_BOARD_ICON, 0);
            TypedArray mBoardIcons = getResources()
                    .obtainTypedArray(R.array.video_board_icons);

            ArrayList<VideoBoardListItem> boards = new ArrayList<>();
            boards.add(new VideoBoardListItem(id, boardName, new SessionManager(this).getUser(), mBoardIcons.getResourceId(iconId, -1)));
            mBoardIcons.recycle();
            BusProvider.getInstance().post(new AddVideoBoardListEvent(boards));
        }
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
            default:
                return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return true;
    }


    private void init_navigator(){
        // Navigation Drawer
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_DrawerLayout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primaryDark));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()){
                    case R.id.create_dub:
                        changeFragment(new PagerFragment(), "VidCraft");
                        return  true;
                    case R.id.add_video:
                        changeFragment(new AddVideoFragment(), "Add Video");
                        return  true;
                    case R.id.my_dubs:
                        changeFragment(new MyDubsFragment(), "My Dubs");
                        return  true;
                    case R.id.settings:
                        changeFragment(new SettingFragment(), "Settings");
                        return  true;
                    default:
                        return  true;
                }
            }
        });

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
            //getSupportActionBar().setHomeButtonEnabled(true);
        }

        mActionBarDrawerToggle.syncState();
        getSupportActionBar().setTitle("VidCraft");
    }

    private void changeFragment(Fragment fragment, String title) {
        mFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(title)
                .commit();
        mDrawerLayout.closeDrawers();
    }

    private void init_languages() {
        languages = new ArrayList<>();
        Realm realm = Realm.getInstance(getApplicationContext());
        RealmResults<InstalledLanguage> installedLanguages = realm.allObjects(InstalledLanguage.class).where().findAll();
        for(InstalledLanguage language: installedLanguages) {
            languages.add(language.getLanguageId());
        }
    }

    public void pushNotification(View v) {

    }

    @Subscribe
    public void onVideoItemMenuEvent(VideoItemMenuEvent event) {
        new VideoItemPopupMenu(new Long(0), this, event.getId(), event.getTitle(), event.getView(), false).show();
    }

    @Subscribe
    public void onMyVideoItemShareEvent(MyVideoItemShareEvent event) {
        new VideoSharer(this, event.getFilePath()).showAlertDialog();
    }

    @Subscribe
    public void onRecyclerViewScrollEndedEvent(RecyclerViewScrollEndedEvent event) {
        new VideoAndBoardDownloader(getApplicationContext()).discover(event.getCurrent_page(), new SessionManager(this).getId(), languages, new VideoAndBoardDownloaderCallback() {
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
        new VideoListDownloader().downloadTrendingVideos(event.getCurrent_page(), event.getCurrent_page() + 10, new SessionManager(this).getId(), languages, new VideoListDownloaderCallback() {
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
        ActivityStarter.createDub(this, event.getId(), event.getTitle());
    }

    @Subscribe
    public void onnVideoFavriouteChangedEvent(VideoFavoriteChangedEvent event) {
        new VideoFavoriteMarker().markFavorite(event.getId(), event.ismFavrioute());
    }

    @Subscribe
    public void onVideoBoardClickedEvent(VideoBoardClickedEvent event) {
        Intent intent = new Intent(this, VideoBoardActivity.class);
        intent.putExtra(ConstantsStore.INTENT_BOARD_ID, event.getId());
        intent.putExtra(ConstantsStore.INTENT_BOARD_NAME, event.getBoardName());
        intent.putExtra(ConstantsStore.INTENT_BOARD_USER_NAME, event.getBoardUsername());
        intent.putExtra(ConstantsStore.INTENT_BOARD_ICON, event.getIcon());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String userName = new SessionManager(this).getUser();
        if(userName.equals(event.getBoardUsername()))
            intent.putExtra(ConstantsStore.INTENT_BOARD_USER, true);
        else
            intent.putExtra(ConstantsStore.INTENT_BOARD_USER, false);
        startActivityForResult(intent, 1);
    }

    @Subscribe
    public void onVideoPlayEvent(VideoPlayEvent event) {
        Intent intent = new Intent(this, PlayVideoActivity.class);
        intent.putExtra(ConstantsStore.INTENT_FILE_PATH, event.getFilePath());
        startActivity(intent);
    }


}
