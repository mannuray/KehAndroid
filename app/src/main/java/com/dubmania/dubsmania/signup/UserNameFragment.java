package com.dubmania.dubsmania.signup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dubmania.dubsmania.R;

public class UserNameFragment extends Fragment {

    OnButtonClickListner mCallback;
    Button next;
    EditText mEdit;
    SignUpInfo info;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnButtonClickListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonClickListner");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_signup_username, container, false);
        next = (Button) rootView.findViewById(R.id.next);
        mEdit = (EditText) rootView.findViewById(R.id.username);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setUserName(mEdit.getText().toString());
                mCallback.onClickNextButton(2);
            }
        });
        return rootView;
    }

    public UserNameFragment setSignUpInfo(SignUpInfo _info) {
        info = _info;
        return this;
    }
}