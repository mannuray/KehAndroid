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
import com.dubmania.dubsmania.restclient.RestClient;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PasswordFragment extends Fragment {

    OnButtonClickListner mCallback;
    EditText mEdit;
    SignUpInfo info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_signup_password, container, false);
        Button next = (Button) rootView.findViewById(R.id.next);
        mEdit = (EditText) rootView.findViewById(R.id.password);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setPassword(mEdit.getText().toString());
                mCallback.onClickNextButton(3);
            }
        });
        return rootView;
    }

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
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public PasswordFragment setSignUpInfo(SignUpInfo _info) {
        info = _info;
        return this;
    }
}