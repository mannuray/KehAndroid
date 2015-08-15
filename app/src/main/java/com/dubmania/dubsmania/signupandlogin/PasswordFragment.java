package com.dubmania.dubsmania.signupandlogin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SignupPasswordEvent;

public class PasswordFragment extends Fragment {

    EditText mPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_signup_password, container, false);
        TextView next = (TextView) rootView.findViewById(R.id.next);
        mPassword = (EditText) rootView.findViewById(R.id.enter_password);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new SignupPasswordEvent(mPassword.getText().toString()));
            }
        });
        return rootView;
    }
}