package com.dubmania.vidcraft.misc;

import android.app.Activity;
import android.content.Intent;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.dubmania.vidcraft.Adapters.MyVideoListItem;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.utils.InstalledLanguage;
import com.dubmania.vidcraft.utils.SavedDubsData;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class LanguageActivity extends AppCompatActivity implements AbsListView.OnItemClickListener  {

    Toolbar mToolbar;
    private AbsListView mListView;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ArrayList<String> installedLanguages = new ArrayList<>();
        Realm realm = Realm.getInstance(getApplicationContext());
        RealmResults<InstalledLanguage> languages = realm.allObjects(InstalledLanguage.class).where().findAll();
        for(InstalledLanguage language: languages) {
            installedLanguages.add(language.getLanguage());
        }

        String[] values = installedLanguages.toArray(new String[languages.size()]);

        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        mListView = (AbsListView) findViewById(R.id.language_list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_language, menu);
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
            case R.id.action_add_language:
                intent = new Intent(this, AddLanguageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
           // put code to display the installed language in language List
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // delete the language from realm database
    }
}
