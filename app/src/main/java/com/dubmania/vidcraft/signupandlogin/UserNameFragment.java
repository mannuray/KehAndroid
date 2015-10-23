package com.dubmania.vidcraft.signupandlogin;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.loginandsignupevent.SetUsernameEvent;
import com.dubmania.vidcraft.communicator.eventbus.loginandsignupevent.SignupFragmentChangeEvent;
import com.dubmania.vidcraft.communicator.eventbus.loginandsignupevent.SignupInfoEvent;
import com.dubmania.vidcraft.communicator.eventbus.loginandsignupevent.UserNameExistEvent;
import com.dubmania.vidcraft.communicator.networkcommunicator.DubsmaniaHttpClient;
import com.dubmania.vidcraft.utils.ClearableEditBox;
import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.otto.Subscribe;

import org.apache.http.Header;
import org.json.JSONException;

public class UserNameFragment extends Fragment {

    private TextView next;
    private String mUsernameStore;
    EditText mUsername;

    private ProgressBar mProgressBar;
    private ImageView mResult;
    RelativeLayout next_layout;
    private ClearableEditBox mEmailEdit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_signup_username, container, false);
        next = (TextView) rootView.findViewById(R.id.next);
        next_layout=(RelativeLayout)rootView.findViewById(R.id.next_layout);
        mUsername = (EditText) rootView.findViewById(R.id.enter_username);
        mUsername.setText(mUsernameStore);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mResult = (ImageView) rootView.findViewById(R.id.resultImageView);
        ClearableEditBox mEmailEdit = new ClearableEditBox(mUsername, (ImageView) rootView.findViewById(R.id.crossImageView));

        next_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new SetUsernameEvent(mUsername.getText().toString()));
            }
        });

        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verify();
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

    private void verify() {
        mProgressBar.setVisibility(View.VISIBLE);
        //Toast.makeText(getActivity().getApplicationContext(), "user name " + mUsername.getText().toString(), Toast.LENGTH_LONG).show();
        DubsmaniaHttpClient.post(ConstantsStore.URL_VERIFY_USER, new RequestParams(ConstantsStore.PARAM_USER_NAME, mUsername.getText().toString()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                mProgressBar.setVisibility(View.GONE);
                try {
                    //Toast.makeText(getActivity().getApplicationContext(), "user name check event " + response.toString(), Toast.LENGTH_LONG).show();
                    if (response.getBoolean("result")) {
                        mResult.setImageResource(R.drawable.tick);
                        mResult.setVisibility(View.VISIBLE);
                    } else {
                        mResult.setImageResource(R.drawable.exclamation);
                        mResult.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), "user name check event fail " + errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Subscribe
    public void onSignupInfoEvent(SignupInfoEvent event) {
        mUsernameStore = event.getUsername();
        if(mUsername != null) {
            mUsername.setText(mUsernameStore);
        }
    }

    @Subscribe
    public void onUserNameExistEvent(UserNameExistEvent event) {
        if(event.isUserNameExist())
            next.setVisibility(View.INVISIBLE);
        else
            next.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onSignupFragmentChangeEvent(SignupFragmentChangeEvent event) {
        mResult.setVisibility(View.GONE);
    }
}