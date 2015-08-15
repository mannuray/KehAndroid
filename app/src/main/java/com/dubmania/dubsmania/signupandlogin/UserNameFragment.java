package com.dubmania.dubsmania.signupandlogin;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SetUsernameEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SignupFragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SignupInfoEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.UserNameExistEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.OnClickListnerEvent;
import com.squareup.otto.Subscribe;

public class UserNameFragment extends Fragment {

    private TextView next;
    private String mUsernameStore;
    EditText mUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_signup_username, container, false);
        next = (TextView) rootView.findViewById(R.id.next);
        mUsername = (EditText) rootView.findViewById(R.id.enter_username);
        mUsername.setText(mUsernameStore);
        next.setOnClickListener(new OnClickListnerEvent<>(new SignupFragmentChangeEvent(2)));
        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                BusProvider.getInstance().post(new SetUsernameEvent(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return rootView;
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
    public void onSignupInfoEvent(SignupInfoEvent event) {
        mUsernameStore = event.getUsername();
        if(mUsername != null) {
            mUsername.setText(mUsernameStore);
        }
    }

    @Subscribe
    public void onSetUsernameEvent(SetUsernameEvent event) {
        if(mUsername != null) {
            mUsername.setText(event.getUsername());
        }
    }

    @Subscribe
    public void onUserNameExistEvent(UserNameExistEvent event) {
        if(event.isUserNameExist())
            next.setVisibility(View.INVISIBLE);
        else
            next.setVisibility(View.VISIBLE);
    }
}