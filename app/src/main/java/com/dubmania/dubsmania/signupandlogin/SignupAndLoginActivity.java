package com.dubmania.dubsmania.signupandlogin;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.EmailCheckEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.EmailExistEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.FragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.LoginEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.LoginFragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.LoginSetEmailEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.PasswordResetEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SetDobEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SetUsernameEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SignupFragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SignupInfoEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SignupPasswordEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.UserNameExistEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.dubmania.dubsmania.utils.SessionManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class SignupAndLoginActivity extends AppCompatActivity {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private SignUpInfo signUpInfo;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);

        PagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.loginAndSignupViewPager);
        mPager.setAdapter(mPagerAdapter);
        signUpInfo = new SignUpInfo();
        changeFragment(0);
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
        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int position = mPager.getCurrentItem();
        switch (position) {
            case 0:
                BusProvider.getInstance().post(new SignupFragmentChangeEvent(-1));
                break;
            case 1:
                BusProvider.getInstance().post(new LoginFragmentChangeEvent(-1));
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
                    return new PagerSignupFragment();
                case 1:
                    return new PagerLoginFragment();
            }
            return new PagerSignupFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    public void changeFragment(int position) {
        mPager.setCurrentItem(position);
    }

    Fragment getFragment(int position) {

        switch (position) {
            case 0:
                return new PagerSignupFragment();
            case 1:
                return new PagerLoginFragment();
        }
        return new EmailFragment();
    }

    private AlertDialog getEmailExistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BusProvider.getInstance().post(new LoginSetEmailEvent(signUpInfo.getUserName())); // change this when login becom based no email
                changeFragment(1);
                Log.d("otto event", "seind set email evnte ");
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

    private void setLoginResult(int action) {
        this.setResult(action);
        this.finish();
    }
    
    @Subscribe
    public void onFragmentChangeEvent(FragmentChangeEvent event) {
        int position = mPager.getCurrentItem();
        switch (position) {
            case 0:
                finish();
            case 1:
                changeFragment(0);
        }
    }

    @Subscribe
    public void onEmailCheckEvent(EmailCheckEvent event) {
        signUpInfo.setEmail(event.getEmail());
        DubsmaniaHttpClient.get(ConstantsStore.URL_VERIFY_EMAIL, new RequestParams(ConstantsStore.PARAM_USER_EMAL, event.getEmail()), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                try {
                    if (!response.getBoolean("result")) {
                        changeFragment();
                    } else {
                        signUpInfo.setUserName(response.getString(ConstantsStore.PARAM_USER_NAME));
                        getEmailExistDialog().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                changeFragment();
            }

            private void changeFragment() {
                BusProvider.getInstance().post(new SignupInfoEvent(signUpInfo.getUserName(), signUpInfo.getEmail(), signUpInfo.getPassword(), signUpInfo.getDob()));
                BusProvider.getInstance().post(new SignupFragmentChangeEvent(1));
            }
        });
    }

    @Subscribe
    public void onSetUsernameEvent(SetUsernameEvent event) {
        signUpInfo.setUserName(event.getUsername());
        DubsmaniaHttpClient.get(ConstantsStore.URL_VERIFY_USER, new RequestParams(ConstantsStore.PARAM_USER, event.getUsername()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), "user name check event " + response.toString(), Toast.LENGTH_LONG).show();
                    BusProvider.getInstance().post(new UserNameExistEvent(response.getBoolean("result")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {

            }
        });
    }

    @Subscribe
    public void onSignupPasswordEvent(SignupPasswordEvent event) {
        signUpInfo.setPassword(event.getPassword());
        BusProvider.getInstance().post(new SignupFragmentChangeEvent(3));
    }

    @Subscribe
    public void onSetDobEvent(SetDobEvent event) {
        signUpInfo.setDob(event.getDob());
        BusProvider.getInstance().post(new SignupFragmentChangeEvent(4));
        Toast.makeText(getApplicationContext(), "user rister check event :" , Toast.LENGTH_LONG).show();
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_USER_NAME, signUpInfo.getUserName());
        params.add(ConstantsStore.PARAM_USER_EMAL, signUpInfo.getEmail());
        params.add(ConstantsStore.PARAM_PASSWORD, signUpInfo.getPassword());
        params.add(ConstantsStore.PARAM_DOB, signUpInfo.getDob());
        DubsmaniaHttpClient.get(ConstantsStore.URL_REGISTER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), "user rister check event " + response.toString(), Toast.LENGTH_LONG).show();
                    int responseCode = response.getInt("result");
                    if (responseCode == ConstantsStore.PARAM_SUCCESS) {
                        Toast.makeText(getApplicationContext(), "Unable to register user", Toast.LENGTH_LONG).show();
                        setLoginResult(Activity.RESULT_OK);
                    }
                    else if (responseCode == ConstantsStore.PARAM_REGISTER_USER_EXIST) {
                        Toast.makeText(getApplicationContext(), "user name taken", Toast.LENGTH_LONG).show();
                        BusProvider.getInstance().post(new SignupFragmentChangeEvent(1));
                    }
                    else if (responseCode == ConstantsStore.PARAM_REGISTER_EMAIL_EXIST) {
                        Toast.makeText(getApplicationContext(), "email name taken", Toast.LENGTH_LONG).show();
                        BusProvider.getInstance().post(new SignupFragmentChangeEvent(0));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Unable to register user", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Subscribe
    public void onLoginEvent(LoginEvent event) {
        Toast.makeText(getApplicationContext(), "user login check event :" , Toast.LENGTH_LONG).show();
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_USER_NAME, event.getEmail());
        params.add(ConstantsStore.PARAM_PASSWORD, event.getPassword());
        DubsmaniaHttpClient.get(ConstantsStore.URL_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), "user login check event " + response.toString(), Toast.LENGTH_LONG).show();
                    if (!response.getBoolean(ConstantsStore.PARAM_RESULT)) {
                        Toast.makeText(getApplicationContext(), "password or user invalid", Toast.LENGTH_LONG).show();
                    }
                    else {
                        new SessionManager(SignupAndLoginActivity.this).createLoginSession(response.getLong(ConstantsStore.PARAM_USER_ID), response.getString(ConstantsStore.PARAM_USER_NAME),
                                response.getString(ConstantsStore.PARAM_USER_EMAL));
                        setLoginResult(Activity.RESULT_OK);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Unable to register user", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Subscribe
    public void onPasswordResetEvent(PasswordResetEvent event) {
        DubsmaniaHttpClient.get(ConstantsStore.URL_RESET_PASSWORD, new RequestParams(ConstantsStore.PARAM_USER_EMAL, event.getEmail()), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Toast.makeText(getApplicationContext(), "email check event " + new String(responseBody), Toast.LENGTH_LONG).show();
                    JSONObject json = new JSONObject(new String(responseBody));
                    if (!json.getBoolean("result")) {
                        Toast.makeText(getApplicationContext(), "Unable to reset password", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setLoginResult(Activity.RESULT_CANCELED);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    @Produce
    public SignupInfoEvent produceSignupInfo() {
        return new SignupInfoEvent(signUpInfo.getUserName(), signUpInfo.getEmail(), signUpInfo.getPassword(), signUpInfo.getDob());
    }
}
