package com.dubmania.dubsmania.signupandlogin;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.LoginEvent;
import com.dubmania.dubsmania.communicator.eventbus.LoginFragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.LoginSetEmailEvent;
import com.dubmania.dubsmania.communicator.eventbus.OnClickListnerEvent;
import com.squareup.otto.Subscribe;

public class LoginFragment extends Fragment {

    Button mLogin;
    EditText mEmail;
    EditText mPassword;
    TextView mForgot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mLogin = (Button)view.findViewById(R.id.next);
        mEmail = (EditText) view.findViewById(R.id.login_email);
        mPassword = (EditText) view.findViewById(R.id.login_password);
        mForgot = (TextView) view.findViewById(R.id.login_forgot_password);
        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword.getText().length() > 8)
                    mLogin.setVisibility(View.VISIBLE);
            }
        });
        mForgot.setOnClickListener(new OnClickListnerEvent<>(new LoginFragmentChangeEvent(1)));
        mLogin.setVisibility(View.INVISIBLE);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new LoginEvent(mEmail.getText().toString(), mPassword.getText().toString()));
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
        mEmail.setText(event.getEmail());
    }
}
