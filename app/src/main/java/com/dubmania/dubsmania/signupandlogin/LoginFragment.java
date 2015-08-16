package com.dubmania.dubsmania.signupandlogin;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.LoginEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.LoginFragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.LoginSetEmailEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.OnClickListnerEvent;
import com.squareup.otto.Subscribe;

public class LoginFragment extends Fragment {

    private TextView mLogin;
    private EditText mEmail;
    private EditText mPassword;
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
        mLogin = (TextView)view.findViewById(R.id.next);
        mEmail = (EditText) view.findViewById(R.id.login_email);
        mEmail.setText(mStoreEmail);
        mPassword = (EditText) view.findViewById(R.id.login_password);
        TextView mForgot = (TextView) view.findViewById(R.id.login_forgot_password);
        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword.getText().length() >= 4)
                    mLogin.setVisibility(View.VISIBLE);
            }
        });
        mForgot.setOnClickListener(new OnClickListnerEvent<>(new LoginFragmentChangeEvent(1)));
        //mLogin.setVisibility(View.INVISIBLE);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new LoginEvent(mEmail.getText().toString().split("@")[0], mPassword.getText().toString()));// TO DO change it login from email
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

    @Subscribe
    public void onLoginSetEmailEvent(LoginSetEmailEvent event) {
        mStoreEmail = event.getEmail();
        mEmail.setText(mStoreEmail);

    }
}
