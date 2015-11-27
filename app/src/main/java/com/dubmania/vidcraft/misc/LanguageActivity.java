package com.dubmania.vidcraft.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dubmania.vidcraft.Adapters.LanguageAndCountryDataHandler;
import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.networkcommunicator.AccountLanguageHandler;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.SessionManager;
import com.dubmania.vidcraft.utils.database.InstalledLanguage;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class LanguageActivity extends AppCompatActivity {

    private ArrayAdapter mAdapter;
    private Realm realm;
    private RealmResults<InstalledLanguage> languages;
    private ArrayList<LanguageAndCountryDataHandler.Language> slanguages;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        realm = Realm.getInstance(getApplicationContext());

        if(new SessionManager(this).isLoggedIn()) {
            mProgressBar.setVisibility(View.VISIBLE);
            slanguages = new ArrayList<>();
            mAdapter = new LanguageListServerArrayAdapter(this, slanguages);
            new AccountLanguageHandler().getUserLanguage(new AccountLanguageHandler.GetLanguageCallback() {
                @Override
                public void onGetLanguageCallbackSuccess(ArrayList<LanguageAndCountryDataHandler.Language> mLanguageList) {
                    mProgressBar.setVisibility(View.GONE);
                    slanguages.addAll(mLanguageList);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onGetLanguageCallbackFailure() {
                    mProgressBar.setVisibility(View.GONE);
                    // show toast
                }
            });
        }
        else {
            languages = realm.allObjects(InstalledLanguage.class).where().findAll();

            mAdapter = new LanguageListInstalledArrayAdapter(this, languages);
        }

        AbsListView mListView = (AbsListView) findViewById(R.id.language_list);
        mListView.setAdapter(mAdapter);
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
            if(data.getLongExtra(ConstantsStore.INTENT_INSTALL_LANGUAGE_ID, 0l) != 0) {
                slanguages.add(new LanguageAndCountryDataHandler.Language(data.getLongExtra(ConstantsStore.INTENT_INSTALL_LANGUAGE_ID, 0l),
                        data.getStringExtra(ConstantsStore.INTENT_INSTALL_LANGUAGE)));
            }
            else {
                languages = realm.allObjects(InstalledLanguage.class).where().findAll();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    class LanguageListInstalledArrayAdapter extends ArrayAdapter<InstalledLanguage> {
        private final Context context;
        private RealmResults<InstalledLanguage> values;

        public LanguageListInstalledArrayAdapter(Context context, RealmResults<InstalledLanguage> languages) {
            super(context, R.layout.language_list_layout, languages);
            this.values = languages;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.language_list_layout, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.label);
            textView.setText(values.get(position).getLanguage());

            if(this.values.size() > 1 ) {
                ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
                imageView.setImageResource(android.R.drawable.ic_delete);
                imageView.setOnClickListener(new View.OnClickListener() {
                    int mPosition;
                    @Override
                    public void onClick(View view) {
                        realm.beginTransaction();
                        languages.remove(mPosition);
                        realm.commitTransaction();
                        languages = realm.allObjects(InstalledLanguage.class).where().findAll();
                        notifyDataSetChanged();
                    }

                    private View.OnClickListener position(int position) {
                        mPosition = position;
                        return this;
                    }
                }.position(position));
            }

            return rowView;
        }
    }

    class LanguageListServerArrayAdapter extends ArrayAdapter<LanguageAndCountryDataHandler.Language> {
        private final Context context;
        private ArrayList<LanguageAndCountryDataHandler.Language> values;

        public LanguageListServerArrayAdapter(Context context, ArrayList<LanguageAndCountryDataHandler.Language> languages) {
            super(context, R.layout.language_list_layout, languages);
            this.values = languages;
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.language_list_layout, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.label);
            textView.setText(values.get(position).getLanguage());

            if(this.values.size() > 1 ) {
                ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
                imageView.setImageResource(android.R.drawable.ic_delete);
                imageView.setOnClickListener(new View.OnClickListener() {
                    int mPosition;
                    @Override
                    public void onClick(View view) {
                        new AccountLanguageHandler().deleteUserLanguage(values.get(position).getId(), new AccountLanguageHandler.DeleteLanguageCallback() {
                            @Override
                            public void onDeleteLanguageCallbackSuccess() {

                            }

                            @Override
                            public void onDeleteLanguageCallbackFailure() {

                            }
                        });
                        values.remove(position);
                        notifyDataSetChanged();
                    }

                    private View.OnClickListener position(int position) {
                        mPosition = position;
                        return this;
                    }
                }.position(position));
            }

            return rowView;
        }
    }
}
