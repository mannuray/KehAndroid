package com.dubmania.dubsmania.signupandlogin;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.LoginEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.LoginSetEmailEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.dubsmania.utils.ClearableEditBox;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.otto.Subscribe;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private View mInfoBox;
    private ProgressBar mProgressBar;
    private TextView mLogin;
    private EditText mEmail;
    private EditText mPassword;
    RelativeLayout next_layout;
    private String mStoreEmail; // need to store email as view will not created when signupinfo event will be recived



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mInfoBox = view.findViewById(R.id.informationBox);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mLogin = (TextView)view.findViewById(R.id.next);
        mEmail = (EditText) view.findViewById(R.id.login_email);
        mEmail.setText(mStoreEmail);
        mPassword = (EditText) view.findViewById(R.id.login_password);
        TextView mForgot = (TextView) view.findViewById(R.id.login_forgot_password);

        ClearableEditBox mEmailEdit = new ClearableEditBox(mEmail, (ImageView) view.findViewById(R.id.emailCross));
        ClearableEditBox mUserEdit = new ClearableEditBox(mPassword, (ImageView) view.findViewById(R.id.passwordCross));

        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword.getText().length() >= 4)
                    mLogin.setVisibility(View.VISIBLE);
            }
        });
        mForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResetDialog().show();
            }
        });
        //mLogin.setVisibility(View.INVISIBLE);
        next_layout=(RelativeLayout)view.findViewById(R.id.next_layout);
        next_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                //BusProvider.getInstance().post(new LoginEvent(mEmail.getText().toString(), mPassword.getText().toString()));// TO DO change it login from email
            }
        });
        return view;
    }

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

    private AlertDialog getResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                resetPassword();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.setMessage("Are you sure you want to reset password")
                .setTitle("Reset Password");

        return builder.create();
    }

    private void login() {
        mInfoBox.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        Toast.makeText(getActivity().getApplicationContext(), "user login check event :", Toast.LENGTH_LONG).show();
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_USER_EMAIL, mEmail.getText().toString());
        params.add(ConstantsStore.PARAM_PASSWORD, mPassword.getText().toString());

        DubsmaniaHttpClient.post(ConstantsStore.URL_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                try {
                    Toast.makeText(getActivity().getApplicationContext(), "user login check event " + response.toString(), Toast.LENGTH_LONG).show();
                    if (!response.getBoolean(ConstantsStore.PARAM_RESULT)) {
                        Toast.makeText(getActivity().getApplicationContext(), "password or user invalid", Toast.LENGTH_LONG).show();
                        mInfoBox.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        BusProvider.getInstance().post(new LoginEvent(response.getLong(ConstantsStore.PARAM_USER_ID), response.getString(ConstantsStore.PARAM_USER_NAME),
                                response.getString(ConstantsStore.PARAM_USER_EMAIL)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                mInfoBox.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), "Unable to Login user", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void resetPassword() {
        DubsmaniaHttpClient.post(ConstantsStore.URL_RESET_PASSWORD, new RequestParams(ConstantsStore.PARAM_USER_EMAIL, mEmail.getText().toString()), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Toast.makeText(getActivity().getApplicationContext(), "email check event " + new String(responseBody), Toast.LENGTH_LONG).show();
                    JSONObject json = new JSONObject(new String(responseBody));
                    if (!json.getBoolean("result")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Unable to reset password", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    @Subscribe
    public void onLoginSetEmailEvent(LoginSetEmailEvent event) {
        mStoreEmail = event.getEmail();
        mEmail.setText(mStoreEmail);

    }
}
