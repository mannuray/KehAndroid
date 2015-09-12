package com.dubmania.dubsmania.signupandlogin;

import android.app.Activity;
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
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.EmailCheckEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.EmailExistEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.FailEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.FragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.LoginEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.LoginFragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.LoginSetEmailEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SetDobEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SetUsernameEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SignupFragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SignupInfoEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SignupPasswordEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.dubmania.dubsmania.utils.SessionManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.otto.Subscribe;

import org.apache.http.Header;
import org.json.JSONException;

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
        //getSupportActionBar().hide();
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

    private AlertDialog getEmailExistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BusProvider.getInstance().post(new LoginSetEmailEvent(signUpInfo.getEmail()));
                changeFragment(1);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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
    public void onEmailExistEvent(EmailExistEvent event) {
        signUpInfo.setEmail(event.getEmail());
        signUpInfo.setUserName(event.getUserName());
        getEmailExistDialog().show();
    }

    @Subscribe
    public void onEmailCheckEvent(EmailCheckEvent event) {
        signUpInfo.setEmail(event.getEmail());
        signUpInfo.setUserName(event.getEmail().split("@")[0]);
        //BusProvider.getInstance().post(this.signUpInfo);
        BusProvider.getInstance().post(new SignupInfoEvent(signUpInfo.getUserName(), signUpInfo.getEmail(), signUpInfo.getPassword(), signUpInfo.getDob()));
        BusProvider.getInstance().post(new SignupFragmentChangeEvent(1));
    }

    @Subscribe
    public void onSetUsernameEvent(SetUsernameEvent event) {
        signUpInfo.setUserName(event.getUsername());
        BusProvider.getInstance().post(new SignupFragmentChangeEvent(2));
    }

    @Subscribe
    public void onSignupPasswordEvent(SignupPasswordEvent event) {
        signUpInfo.setPassword(event.getPassword());
        BusProvider.getInstance().post(new SignupFragmentChangeEvent(3));
    }

    @Subscribe
    public void onSetDobEvent(SetDobEvent event) {
        signUpInfo.setDob(event.getDob());
        Toast.makeText(getApplicationContext(), "user rister check event :" , Toast.LENGTH_LONG).show();

        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_USER_NAME, signUpInfo.getUserName());
        params.add(ConstantsStore.PARAM_USER_EMAIL, signUpInfo.getEmail());
        params.add(ConstantsStore.PARAM_PASSWORD, signUpInfo.getPassword());
        params.add(ConstantsStore.PARAM_DOB, signUpInfo.getDob());
        DubsmaniaHttpClient.post(ConstantsStore.URL_REGISTER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, @SuppressWarnings("deprecation") Header[] headers, org.json.JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), "user rister check event " + response.toString(), Toast.LENGTH_LONG).show();
                    if (response.getBoolean("result")) {
                        Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_LONG).show();
                        new SessionManager(SignupAndLoginActivity.this).createLoginSession(response.getLong(ConstantsStore.PARAM_USER_ID), response.getString(ConstantsStore.PARAM_USER_NAME),
                                response.getString(ConstantsStore.PARAM_USER_EMAIL));
                        setLoginResult(Activity.RESULT_OK);
                    } else if (response.getString("error").equals("user_name")) {
                        Toast.makeText(getApplicationContext(), "user name taken", Toast.LENGTH_LONG).show();
                        BusProvider.getInstance().post(new SignupFragmentChangeEvent(1));
                    } else if (response.getString("error").equals("user_email")) {
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
                BusProvider.getInstance().post(new FailEvent());
            }
        });
    }

    @Subscribe
    public void onLoginEvent(LoginEvent event) {
        new SessionManager(SignupAndLoginActivity.this).createLoginSession(event.getUserId(), event.getUserName(), event.getEmail());
        setLoginResult(Activity.RESULT_OK);
    }
}
