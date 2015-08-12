package com.dubmania.dubsmania.misc;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;

public class MyAccountActivity extends AppCompatActivity {
    private Button mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        mLogout = (Button) findViewById(R.id.my_account_logout_button);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_account, menu);
        return true;
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

    public void onLogoutClick(View v) {
        DubsmaniaHttpClient.get(ConstantsStore.USER_LOGOUT_URL, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), "user logout " + response.toString(), Toast.LENGTH_LONG).show();
                    if (response.getBoolean("result")) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(ConstantsStore.SHARED_KEY_USER_NAME, ""); //put after result modification
                        editor.putString(ConstantsStore.SHARED_KEY_USER_EMAIL, "");
                        editor.putBoolean(ConstantsStore.SHARED_KEY_USER_LOGIN, false);
                        editor.commit();
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), "Unable to logout user", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Unable to logout user", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
