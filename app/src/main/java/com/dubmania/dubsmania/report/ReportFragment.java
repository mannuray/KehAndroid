package com.dubmania.dubsmania.report;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.feedbackevent.FragmentFeedbackCreateEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.otto.Subscribe;

import org.apache.http.Header;

public class ReportFragment extends Fragment {

    private Long mVideoId;
    private RadioGroup mRadioGroup;
    private View mInformationBox;
    private EditText mName;
    private EditText mEmail;
    private EditText mDescription;

    public ReportFragment() {
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
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        RadioButton mIntellectualProperty = (RadioButton) view.findViewById(R.id.radioButtonPropertyViolation);
        mInformationBox = view.findViewById(R.id.informationBox);
        mInformationBox.setVisibility(View.GONE);
        mName = (EditText) view.findViewById(R.id.editTextName);
        mEmail = (EditText) view.findViewById(R.id.editTextEmail);
        mDescription = (EditText) view.findViewById(R.id.editTextDescreption);

        mIntellectualProperty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mInformationBox.setVisibility(View.VISIBLE);
                else
                    mInformationBox.setVisibility(View.GONE);
            }
        });

        view.findViewById(R.id.report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = mRadioGroup.getCheckedRadioButtonId();
                if(selectedId == R.id.radioButtonPropertyViolation) {
                    if(mName.getText().toString().equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter your name", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(mEmail.getText().toString().equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter your email", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(mDescription.getText().toString().equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter description", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    report();
                }
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

    private void report() {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_VIDEO_ID, String.valueOf(mVideoId));

        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        if(selectedId == R.id.radioButtonPropertyViolation) {
            params.add(ConstantsStore.PARAM_REPORT_REASON_CODE, String.valueOf(3));
            params.add(ConstantsStore.PARAM_REPORT_NAME, mName.getText().toString());
            params.add(ConstantsStore.PARAM_REPORT_EMAIL, mEmail.getText().toString());
            params.add(ConstantsStore.PARAM_REPORT_DESC, mDescription.getText().toString());
        }
        else {
            if(selectedId == R.id.radioButtonSexual)
                params.add(ConstantsStore.PARAM_REPORT_REASON_CODE, String.valueOf(0));
            else if(selectedId == R.id.radioButtonViolence)
                params.add(ConstantsStore.PARAM_REPORT_REASON_CODE, String.valueOf(1));
            else if(selectedId == R.id.radioButtonHateSpeech)
                params.add(ConstantsStore.PARAM_REPORT_REASON_CODE, String.valueOf(2));
        }
        DubsmaniaHttpClient.post(ConstantsStore.URL_REPORT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                getActivity().finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity().getApplicationContext(), "Please check internet", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });
    }

    @Subscribe
    public void onFragmentFeedbackCreateEvent(FragmentFeedbackCreateEvent event) {
        mVideoId = event.getVideoId();
    }
}
