package com.dubmania.dubsmania.misc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.dubmania.dubsmania.R;

public class AddLanguageActivity extends AppCompatActivity {
    private NumberPicker mCountryPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_language);


        NumberPicker mLanguagePicker = (NumberPicker) findViewById(R.id.language_picker);
        mCountryPicker = (NumberPicker) findViewById(R.id.country_picker);
        mLanguagePicker.setMinValue(0);
        mLanguagePicker.setMaxValue(2);
        mLanguagePicker.setDisplayedValues(new String[]{"Belgium", "France", "United Kingdom"});
        mLanguagePicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                mCountryPicker.setDisplayedValues(new String[]{"india", "nepal"});
            }
        });
        Button start = (Button) findViewById(R.id.add_language);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TO DO
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_language, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }
}
