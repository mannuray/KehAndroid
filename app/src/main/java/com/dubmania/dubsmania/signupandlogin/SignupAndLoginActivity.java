package com.dubmania.dubsmania.signupandlogin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.EmailCheckEvent;
import com.dubmania.dubsmania.communicator.eventbus.EmailExistEvent;
import com.dubmania.dubsmania.communicator.eventbus.FragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.LoginEvent;
import com.dubmania.dubsmania.communicator.eventbus.LoginFragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.LoginSetEmailEvent;
import com.dubmania.dubsmania.communicator.eventbus.PasswordResetEvent;
import com.dubmania.dubsmania.communicator.eventbus.SetDobEvent;
import com.dubmania.dubsmania.communicator.eventbus.SetUsernameEvent;
import com.dubmania.dubsmania.communicator.eventbus.SignupFragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.SignupPasswordEvent;
import com.dubmania.dubsmania.communicator.eventbus.UserNameExistEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);
        mFragmentManager = getSupportFragmentManager();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment f = mFragmentManager.findFragmentById(R.id.container);
        if (f instanceof PagerSignupFragment) {
            BusProvider.getInstance().post(new SignupFragmentChangeEvent(-1));
        }
        else if (f instanceof PagerLoginFragment) {
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

    public void changeFragment(int position) {
        // update the main content by replacing fragments
        mFragmentManager.beginTransaction()
                .replace(R.id.container, getFragment(position))
                .commit();
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
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, getFragment(1))
                        .commit();
                BusProvider.getInstance().post(new LoginSetEmailEvent(signUpInfo.getEmail()));
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

    /*@Subscribe
    public void onFragmentCallbackEvent(FragmentChangeEvent event) {
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
    public void onFragmentChangeEvent(FragmentChangeEvent event) {
        Fragment f = mFragmentManager.findFragmentById(R.id.container);
        if (f instanceof PagerSignupFragment) {
            finish();
        }
        else if (f instanceof PagerLoginFragment) {
            changeFragment(0);
        }
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
                    if (!json.getBoolean("result")) {
                        BusProvider.getInstance().post(new SetUsernameEvent(signUpInfo.getUserName()));
                        BusProvider.getInstance().post(new SignupFragmentChangeEvent(1));
                    } else {
                        getEmailExistDialog().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //mPager.setCurrentItem(1);
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
        BusProvider.getInstance().post(new SignupFragmentChangeEvent(3));
    }

    @Subscribe
    public void onSetDobEvent(SetDobEvent event) {
        signUpInfo.setDob(event.getDob());
        BusProvider.getInstance().post(new SignupFragmentChangeEvent(4));
        Toast.makeText(getApplicationContext(), "user rister check event :" , Toast.LENGTH_LONG).show();
        RequestParams params = new RequestParams();
        params.add("username", signUpInfo.getUserName());
        params.add("useremail", signUpInfo.getEmail());
        params.add("mPassword", signUpInfo.getPassword());
        params.add("dob", signUpInfo.getDob());
        DubsmaniaHttpClient.get("userservice/register", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Toast.makeText(getApplicationContext(), "user rister check event " + new String(responseBody), Toast.LENGTH_LONG).show();
                    JSONObject json = new JSONObject(new String(responseBody));
                    if (!json.getBoolean("result")) {
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

    @Subscribe
    public void onLoginEvent(LoginEvent event) {
        Toast.makeText(getApplicationContext(), "user rister check event :" , Toast.LENGTH_LONG).show();
        RequestParams params = new RequestParams();
        params.add("useremail", signUpInfo.getEmail());
        params.add("mPassword", signUpInfo.getPassword());
        DubsmaniaHttpClient.get("userservice/login", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Toast.makeText(getApplicationContext(), "user login check event " + new String(responseBody), Toast.LENGTH_LONG).show();
                    JSONObject json = new JSONObject(new String(responseBody));
                    if (!json.getBoolean("result")) {
                        Toast.makeText(getApplicationContext(), "Unable to register login", Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Unable to register user", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Subscribe
    public void onPasswordResetEvent(PasswordResetEvent event) {
        DubsmaniaHttpClient.get("userservice/resetpassword", new RequestParams("useremail", event.getEmail()), new AsyncHttpResponseHandler() {
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
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }


}
