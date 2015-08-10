package com.dubmania.dubsmania.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.misc.ConstantsStore;
import com.dubmania.dubsmania.misc.MyAccountActivity;
import com.dubmania.dubsmania.signupandlogin.SignupAndLoginActivity;

public class SettingFragment extends Fragment {

    private TextView mLogin;
    private View mUsername;
    private View mEmail;
    private TextView mUsernameText;
    private TextView mEmailText;
    private TextView mLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_setting, container, false);
        mLogin = (TextView) rootView.findViewById(R.id.login_signup);
        mUsername = rootView.findViewById(R.id.fragment_setting_username_view);
        mUsernameText = (TextView) rootView.findViewById(R.id.frament_setting_username_edit);
        mEmail = rootView.findViewById(R.id.fragment_setting_email_view);
        mEmailText = (TextView) rootView.findViewById(R.id.frament_setting_email_edit);
        mLogout = (TextView) rootView.findViewById(R.id.logout_text);
        setLoginView();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignupAndLoginActivity.class);
                startActivity(intent);
            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(intent);
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

    private void setLoginView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if(sharedPreferences.getBoolean(ConstantsStore.USER_LOGIN_KEY, false)) {
            mLogin.setVisibility(View.GONE);
            mUsername.setVisibility(View.VISIBLE);
            //mUsernameText.setVisibility(View.VISIBLE);
            mUsernameText.setText(sharedPreferences.getString(ConstantsStore.USER_NAME_KEY, ""));
            mEmail.setVisibility(View.VISIBLE);
            //mEmailText.setVisibility(View.VISIBLE);
            mEmailText.setText(sharedPreferences.getString(ConstantsStore.USER_EMAL_KEY, ""));
            mLogout.setVisibility(View.VISIBLE);
        }
        else {
            mLogin.setVisibility(View.VISIBLE);
            mUsername.setVisibility(View.GONE);
            //mUsernameText.setVisibility(View.INVISIBLE);
            //mUsernameText.setText(sharedPreferences.getString(ConstantsStore.USER_NAME_KEY, ""));
            mEmail.setVisibility(View.GONE);
            //mEmailText.setVisibility(View.INVISIBLE);
            //mEmailText.setText(sharedPreferences.getString(ConstantsStore.USER_EMAL_KEY, ""));
            mLogout.setVisibility(View.GONE);
        }
    }
}
