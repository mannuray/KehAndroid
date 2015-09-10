package com.dubmania.dubsmania.report;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.feedbackevent.FragmentFeedbackCreateEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.otto.Subscribe;

import org.json.JSONException;

public class ImproveFragment extends Fragment {

    private Long mVideoId;
    private CheckBox mTitle;
    private EditText mSaid;
    private CheckBox mNoise;
    private CheckBox mCrop;
    private CheckBox mLanguage;
    private Spinner mLanguageSelector;

    public ImproveFragment() {
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
        View view = inflater.inflate(R.layout.fragment_improve, container, false);
        mTitle = (CheckBox) view.findViewById(R.id.checkBoxTitle);
        mSaid = (EditText) view.findViewById(R.id.editText);
        mSaid.setVisibility(View.GONE);
        mTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mSaid.setVisibility(View.VISIBLE);
                else
                    mSaid.setVisibility(View.GONE);
            }
        });

        mNoise = (CheckBox) view.findViewById(R.id.checkBoxTitle);
        mCrop = (CheckBox) view.findViewById(R.id.checkBoxCrop);
        mLanguage = (CheckBox) view.findViewById(R.id.checkBoxLanguage);
        mLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mLanguageSelector.setVisibility(View.VISIBLE);
                else
                    mLanguageSelector.setVisibility(View.GONE);
            }
        });
        mLanguageSelector = (Spinner) view.findViewById(R.id.spinner); // write code to populate spinner
        mLanguageSelector.setVisibility(View.GONE);

        view.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitle.isChecked()) {
                    if (mSaid.getText().toString().equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter reason for title mismatch", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                report();
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

        if(mTitle.isChecked()) {
            params.add(ConstantsStore.PARAM_IMPROVE_TITLE, "1");
            params.add(ConstantsStore.PARAM_IMPROVE_SAID, mSaid.getText().toString());
        }

        if(mNoise.isChecked())
            params.add(ConstantsStore.PARAM_IMPROVE_NOISE, "1");

        if(mCrop.isChecked())
            params.add(ConstantsStore.PARAM_IMPROVE_CROP, "1");

        if(mLanguage.isChecked()) {
            params.add(ConstantsStore.PARAM_IMPROVE_LANGUAGE, "1");
            params.add(ConstantsStore.PARAM_IMPROVE_SAID, "Hindi"); // modify to pick from spinner
        }

        DubsmaniaHttpClient.post(ConstantsStore.URL_IMPROVE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                try {
                    Log.i("Server res", response.toString());
                    if (response.getBoolean("result")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Thank you for your feedback", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                Log.i("Server res", String.valueOf(statusCode));
                Toast.makeText(getActivity().getApplicationContext(), "Please check internet", Toast.LENGTH_LONG).show();
                //getActivity().finish();
            }
        });
    }

    @Subscribe
    public void onFragmentFeedbackCreateEvent(FragmentFeedbackCreateEvent event) {
        mVideoId = event.getVideoId();
    }
}
