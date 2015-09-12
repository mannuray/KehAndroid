package com.dubmania.dubsmania.signupandlogin;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.EmailCheckEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.EmailExistEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.LoginFragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SignupFragmentChangeEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.dubsmania.utils.ClearableEditBox;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.otto.Subscribe;

import org.json.JSONException;

public class EmailFragment extends Fragment {

    private View next;
    private EditText mEmail;
    private ProgressBar mProgressBar;
    private ImageView mResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_signup_email, container, false);
        mEmail = (EditText) rootView.findViewById(R.id.email);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mResult = (ImageView) rootView.findViewById(R.id.resultImageView);
        ClearableEditBox mEmailEdit = new ClearableEditBox(mEmail, (ImageView) rootView.findViewById(R.id.crossImageView));

        try {
            Account[] accounts = AccountManager.get(getActivity()).getAccountsByType("com.google");
            for (int i = 0, accountsLength = accounts.length; i < accountsLength; i++) {
                Account account = accounts[i];
                mEmail.setText(account.name);
                break;
            }
        } catch (Exception exception) {
            Log.i("Exception", "Exception:" + exception);
        }

        next = rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
                //BusProvider.getInstance().post(new EmailCheckEvent(mEmail.getText().toString()));
            }
        });
        return rootView;
    }

    private void verify() {
        mProgressBar.setVisibility(View.VISIBLE);
        DubsmaniaHttpClient.post(ConstantsStore.URL_VERIFY_EMAIL, new RequestParams(ConstantsStore.PARAM_USER_EMAIL, mEmail.getText().toString()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                mProgressBar.setVisibility(View.GONE);
                try {
                    Log.i("Server res", response.toString());
                    if (response.getBoolean("result")) {
                        mResult.setImageResource(R.drawable.tick);
                        mResult.setVisibility(View.VISIBLE);
                        BusProvider.getInstance().post(new EmailCheckEvent(mEmail.getText().toString()));
                    } else {
                        mResult.setImageResource(R.drawable.exclamation);
                        mResult.setVisibility(View.VISIBLE);
                        BusProvider.getInstance().post(new EmailExistEvent(mEmail.getText().toString(), response.getString(ConstantsStore.PARAM_USER_NAME)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                mProgressBar.setVisibility(View.GONE);
                // see if internet is not available
            }
        });
    }

    // TO DO see if we need to listen
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onLoginFragmentChangeEvent(LoginFragmentChangeEvent event) {
        mResult.setVisibility(View.GONE);
    }

    @Subscribe
    public void onSignupFragmentChangeEvent(SignupFragmentChangeEvent event) {
        mResult.setVisibility(View.GONE);
    }
}