package com.dubmania.dubsmania.signup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.dubmania.dubsmania.R;

import java.util.regex.Pattern;


public class DobFragment extends Fragment {

    OnButtonClickListner mCallback;
    SignUpInfo info;
    DatePicker picker;
    TextView DOB;
    Button next;

    public DobFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_dob, container, false);

        TextView confirmation = (TextView) rootView.findViewById(R.id.text_confirmation);
        Pattern termofService = Pattern.compile("Term of Service");
        Pattern privacyPolicy = Pattern.compile("Privacy Policy");
        Linkify.addLinks(confirmation, termofService, "http://www.google.ie/search?q=");
        Linkify.addLinks(confirmation, privacyPolicy, "http://www.google.ie/search?q=");

        picker = (DatePicker) rootView.findViewById(R.id.dob_picker);
        next = (Button) rootView.findViewById(R.id.next);
        DOB = (TextView) rootView.findViewById(R.id.text_dob);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setDob(DOB.getText().toString());
                mCallback.onClickNextButton(4);
            }
        });

        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DOB.setText(getCurrentDate());
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

    public String getCurrentDate() {
        StringBuilder builder = new StringBuilder();
        builder.append((picker.getMonth() + 1) + "/");
        builder.append(picker.getDayOfMonth() + "/");
        builder.append(picker.getYear());
        return builder.toString();
    }

    public DobFragment setSignUpInfo(SignUpInfo _info) {
        info = _info;
        return this;
    }
}

