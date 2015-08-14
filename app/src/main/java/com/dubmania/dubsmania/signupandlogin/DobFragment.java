package com.dubmania.dubsmania.signupandlogin;

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
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.loginandsignupevent.SetDobEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;


public class DobFragment extends Fragment {

    DatePicker picker;
    TextView DOB;
    Button next;

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

        next = (Button) rootView.findViewById(R.id.next);
        next.setVisibility(View.INVISIBLE);
        DOB = (TextView) rootView.findViewById(R.id.text_dob);
        picker = (DatePicker) rootView.findViewById(R.id.dob_picker);
        final Date date = new Date();
        picker.setMaxDate(new Date().getTime());
        picker.init(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if( Calendar.getInstance().get(Calendar.YEAR) - year > 14) {
                    next.setVisibility(View.VISIBLE);
                }
                else {
                    next.setVisibility(View.INVISIBLE);
                }
                DOB.setText(String.valueOf(dayOfMonth)+'/'+String.valueOf(monthOfYear)+'/'+String.valueOf(year));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new SetDobEvent(String.valueOf(picker.getYear()) +
                                                                String.valueOf(picker.getMonth()) +
                                                                String.valueOf(picker.getDayOfMonth())
                ));
            }
        });
        return rootView;
    }
}

