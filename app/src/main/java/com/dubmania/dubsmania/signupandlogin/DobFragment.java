package com.dubmania.dubsmania.signupandlogin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.FailEvent;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SetDobEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.RecyclerViewScrollEndedEvent;
import com.squareup.otto.Subscribe;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;


public class DobFragment extends Fragment {

    private DatePicker picker;
    private TextView DOB;
    private TextView next;
    private ProgressBar mProgressBar;
    private View mInfoBox;
    ViewGroup rootView;
    RelativeLayout next_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_dob, container, false);


        TextView confirmation = (TextView) rootView.findViewById(R.id.text_confirmation);
        Pattern termofService = Pattern.compile("Term of Service");
        Pattern privacyPolicy = Pattern.compile("Privacy Policy");
        Linkify.addLinks(confirmation, termofService, "http://www.google.ie/search?q=");
        Linkify.addLinks(confirmation, privacyPolicy, "http://www.google.ie/search?q=");


        mInfoBox = rootView.findViewById(R.id.informationBox);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        next = (TextView) rootView.findViewById(R.id.next);
        next_layout=(RelativeLayout)rootView.findViewById(R.id.next_layout);

        DOB = (TextView) rootView.findViewById(R.id.text_dob);
        picker = (DatePicker) rootView.findViewById(R.id.dob_picker);
        picker.setMaxDate(new Date().getTime());
        picker.init(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if( Calendar.getInstance().get(Calendar.YEAR) - year > 14) {
                    next_layout.setVisibility(View.VISIBLE);
                    //next.setVisibility(View.VISIBLE);
                }
                else {
                    next_layout.setVisibility(View.INVISIBLE);
                   // next.setVisibility(View.INVISIBLE);
                }
                DOB.setText(String.valueOf(dayOfMonth)+'/'+String.valueOf(monthOfYear)+'/'+String.valueOf(year));
            }
        });

        next_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoBox.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                BusProvider.getInstance().post(new SetDobEvent(String.valueOf(picker.getYear()) +
                                                                String.valueOf(picker.getMonth()) +
                                                                String.valueOf(picker.getDayOfMonth())
                ));
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if(visible) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onFailEvent(FailEvent event) {
        mInfoBox.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }




}

