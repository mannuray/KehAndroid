package com.dubmania.vidcraft.signupandlogin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.loginandsignupevent.SignupPasswordEvent;
import com.dubmania.vidcraft.utils.ClearableEditBox;

public class PasswordFragment extends Fragment {

    EditText mPassword;
    private ProgressBar mProgressBar;
    private ImageView mResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_signup_password, container, false);
        TextView next = (TextView) rootView.findViewById(R.id.next);
        mPassword = (EditText) rootView.findViewById(R.id.enter_password);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar); // set progress bar for password verifier
        mResult = (ImageView) rootView.findViewById(R.id.resultImageView);
        ClearableEditBox mEmailEdit = new ClearableEditBox(mPassword, (ImageView) rootView.findViewById(R.id.crossImageView));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new SignupPasswordEvent(mPassword.getText().toString()));
            }
        });
        return rootView;
    }
}