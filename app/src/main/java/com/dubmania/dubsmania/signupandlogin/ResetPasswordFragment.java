package com.dubmania.dubsmania.signupandlogin;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.PasswordResetEvent;
import com.dubmania.dubsmania.communicator.eventbus.SignupInfoEvent;
import com.squareup.otto.Subscribe;


public class ResetPasswordFragment extends Fragment {

    private Button mLogin;
    private EditText mEmail;
    private String mStoreEmail; // need to store email as view will not created when signupinfo event will be recived

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        mLogin = (Button)view.findViewById(R.id.next);
        mEmail = (EditText) view.findViewById(R.id.login_email);
        mEmail.setText(mStoreEmail);
        mLogin.setVisibility(View.INVISIBLE);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new PasswordResetEvent(mEmail.getText().toString()));
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
    public void onSignupInfoEvent(SignupInfoEvent event) {
        mStoreEmail = event.getEmail();
    }
}