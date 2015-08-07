package com.dubmania.dubsmania.signupandlogin;

import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.EmailCheckEvent;
import com.dubmania.dubsmania.communicator.eventbus.EmailExistEvent;
import com.dubmania.dubsmania.communicator.eventbus.FragmentCallbackEvent;
import com.dubmania.dubsmania.communicator.eventbus.SetDobEvent;
import com.dubmania.dubsmania.communicator.eventbus.SetUsernameEvent;
import com.dubmania.dubsmania.communicator.eventbus.SignupPasswordEvent;
import com.dubmania.dubsmania.communicator.eventbus.UserNameExistEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.otto.Subscribe;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private SignUpInfo signUpInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mPager = (ViewPager) findViewById(R.id.view_pager);
        signUpInfo = new SignUpInfo();
        /*
      The pager adapter, which provides the pages to the view pager widget.
     */
        PagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int positon = mPager.getCurrentItem();
        switch (positon) {
            case 0:
            case 4:
                finish();
                return;
            default:
                mPager.setCurrentItem(positon - 1);

        }
    }

    @Override public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    private AlertDialog getEmailExistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // TO DO
                // show loging fagment to user
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BusProvider.getInstance().post(new EmailExistEvent());
                dialog.cancel();
            }
        });

        builder.setMessage(R.string.email_already_exist)
                .setTitle(R.string.email);

        return builder.create();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        TypedArray title = getResources()
                .obtainTypedArray(R.array.pager_signup_titles);
        String titles[] = {title.getString(0), title.getString(1), title.getString(2), title.getString(3)};
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {

            switch (i) {
                case 0:
                    return new EmailFragment();
                case 1:
                    return new UserNameFragment();
                case 2:
                    return new PasswordFragment();
                case 3:
                    return new DobFragment();
                case 4:
                    return new FinishFragment();
            }
            return new EmailFragment();
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    /*@Subscribe
    public void onFragmentCallbackEvent(FragmentCallbackEvent event) {
        if (event.getPosition() == 4) {
            Realm realm = Realm.getInstance(getApplicationContext());
            realm.beginTransaction();
            SignUpInfo signUp = realm.createObject(SignUpInfo.class);
            signUp.setId("0");
            signUp.setEmail(signUpInfo.getEmail());
            signUp.setUserName(signUpInfo.getUserName());
            signUp.setPassword(signUpInfo.getPassword());
            signUp.setDob(signUpInfo.getDob());
            realm.commitTransaction();
        }
    }*/
    @Subscribe
    public void onFragmentCallbackEvent(FragmentCallbackEvent event) {
        mPager.setCurrentItem(event.getPosition());
    }

    @Subscribe
    public void onEmailCheckEvent(EmailCheckEvent event) {
        signUpInfo.setEmail(event.getEmail());
        signUpInfo.setUserName(event.getEmail().split("@")[0]);
        DubsmaniaHttpClient.get("userservice/verifyUserEmail", new RequestParams("useremail", event.getEmail()), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Toast.makeText(getApplicationContext(), "email check event " + new String(responseBody), Toast.LENGTH_LONG).show();
                    JSONObject json = new JSONObject(new String(responseBody));
                    if( !json.getBoolean("result")) {
                        BusProvider.getInstance().post(new SetUsernameEvent(signUpInfo.getUserName()));
                        mPager.setCurrentItem(1);
                    }
                    else {
                        getEmailExistDialog().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mPager.setCurrentItem(1);
            }
        });
    }

    @Subscribe
    public void onSetUsernameEvent(SetUsernameEvent event) {
        signUpInfo.setUserName(event.getUsername());
        DubsmaniaHttpClient.get("userservice/verifyUser", new RequestParams("username", event.getUsername()), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Toast.makeText(getApplicationContext(), "user name check event " + new String(responseBody), Toast.LENGTH_LONG).show();
                    JSONObject json = new JSONObject(new String(responseBody));
                    BusProvider.getInstance().post(new UserNameExistEvent(json.getBoolean("result")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //mPager.setCurrentItem(2);
            }
        });
    }

    @Subscribe
    public void onSignupPasswordEvent(SignupPasswordEvent event) {
        signUpInfo.setPassword(event.getPassword());
        mPager.setCurrentItem(3);
    }

    @Subscribe
    public void onSetDobEvent(SetDobEvent event) {
        signUpInfo.setDob(event.getDob());
        mPager.setCurrentItem(4);
        Toast.makeText(getApplicationContext(), "user rister check event :" , Toast.LENGTH_LONG).show();
        RequestParams params = new RequestParams();
        params.add("username", signUpInfo.getUserName());
        params.add("useremail", signUpInfo.getEmail());
        params.add("password", signUpInfo.getPassword());
        params.add("dob", signUpInfo.getDob());
        DubsmaniaHttpClient.get("userservice/register", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Toast.makeText(getApplicationContext(), "user rister check event " + new String(responseBody), Toast.LENGTH_LONG).show();
                    JSONObject json = new JSONObject(new String(responseBody));
                    if(!json.getBoolean("result")) {
                        Toast.makeText(getApplicationContext(), "Unable to register user", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Unable to register user", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
