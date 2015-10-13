package com.dubmania.vidcraft.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.dubmania.vidcraft.utils.InstalledLanguage;

import io.realm.Realm;
import io.realm.RealmResults;

public class LanguageActivity extends AppCompatActivity {

    private ArrayAdapter mAdapter;
    private Realm realm;
    private RealmResults<InstalledLanguage> languages;

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

        realm = Realm.getInstance(getApplicationContext());
        languages = realm.allObjects(InstalledLanguage.class).where().findAll();

        mAdapter = new LanguageListArrayAdapter(this, languages);

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
            /*String language = data.getStringExtra(ConstantsStore.INTENT_INSTALL_LANGUAGE);
            Long id = data.getLongExtra(ConstantsStore.INTENT_INSTALL_LANGUAGE_ID, 0);
            realm.beginTransaction();
            InstalledLanguage installLanguage = realm.createObject(InstalledLanguage.class);
            installLanguage.setLanguage(language);
            installLanguage.setLanguageId(id);
            realm.commitTransaction();*/
            languages = realm.allObjects(InstalledLanguage.class).where().findAll();
            mAdapter.notifyDataSetChanged();
        }
    }

    class LanguageListArrayAdapter extends ArrayAdapter<InstalledLanguage> {
        private final Context context;
        private RealmResults<InstalledLanguage> values;

        public LanguageListArrayAdapter(Context context, RealmResults<InstalledLanguage> languages) {
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
}
