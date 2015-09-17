package com.dubmania.dubsmania.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.misc.LanguageActivity;
import com.dubmania.dubsmania.misc.MyAccountActivity;
import com.dubmania.dubsmania.signupandlogin.SignupAndLoginActivity;
import com.dubmania.dubsmania.utils.SessionManager;

public class SettingFragment extends Fragment {

    private View mLoginInfo;
    private TextView mLogin;
    private TextView mUsername;
    private TextView mEmail;
    private TextView mLogout;
    private TextView mPushStatus;
    private RelativeLayout login_signup_layout;
    private RelativeLayout change_language_layout;
    private RelativeLayout support_center_layout;
    private RelativeLayout library_weuse_layout;
    private RelativeLayout terms_ofuse_layout;
    private RelativeLayout privacy_policy_layout;
    private RelativeLayout push_notification_layout;
    private RelativeLayout daily_layout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View rootView =  inflater.inflate(R.layout.fragment_setting, container, false);
        mLogin = (TextView) rootView.findViewById(R.id.login_signup);
        login_signup_layout=(RelativeLayout)rootView.findViewById(R.id.login_signup_layout);
        change_language_layout=(RelativeLayout)rootView.findViewById(R.id.change_language_layout);
        support_center_layout=(RelativeLayout)rootView.findViewById(R.id.support_center_layout);
        library_weuse_layout=(RelativeLayout)rootView.findViewById(R.id.library_weuse_layout);
        terms_ofuse_layout=(RelativeLayout)rootView.findViewById(R.id.terms_ofuse_layout);
        privacy_policy_layout=(RelativeLayout)rootView.findViewById(R.id.privacy_policy_layout);
        push_notification_layout=(RelativeLayout)rootView.findViewById(R.id.push_notification_layout);
        daily_layout=(RelativeLayout)rootView.findViewById(R.id.daily_layout);


        mLoginInfo = rootView.findViewById(R.id.login_box);
        mUsername = (TextView) rootView.findViewById(R.id.frament_setting_username_edit);
        mEmail = (TextView) rootView.findViewById(R.id.frament_setting_email_edit);
        mPushStatus = (TextView) rootView.findViewById(R.id.settingPushNotification);
        mLogout = (TextView) rootView.findViewById(R.id.logout_text);

        login_signup_layout.setOnClickListener(new View.OnClickListener() {
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

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            ((TextView) rootView.findViewById(R.id.version_text)).setText("version: " + pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // set up text listners
        change_language_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LanguageActivity.class);
                startActivity(intent);
            }
        });
        support_center_layout.setOnClickListener(new OnSettingClickListner("http://www.google.com"));
        library_weuse_layout.setOnClickListener(new OnSettingClickListner("http://www.google.com"));
        terms_ofuse_layout.setOnClickListener(new OnSettingClickListner("http://www.google.com"));
        privacy_policy_layout.setOnClickListener(new OnSettingClickListner("http://www.google.com"));
        push_notification_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show list dialog box
            }
        });
        setLoginView();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Settings");
        setLoginView();
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
        SessionManager manager = new SessionManager(getActivity());
        if(manager.isLoggedIn()) {
            mUsername.setText(manager.getUser());
            mEmail.setText(manager.getUserEmail());

            mLogin.setVisibility(View.GONE);
            mLoginInfo.setVisibility(View.VISIBLE);
            mLogout.setVisibility(View.VISIBLE);
        }
        else {
            mLogin.setVisibility(View.VISIBLE);
            mLoginInfo.setVisibility(View.GONE);
            mLogout.setVisibility(View.GONE);
        }
    }

    private class OnSettingClickListner implements View.OnClickListener {
        private String uri;

        private OnSettingClickListner(String uri) {
            this.uri = uri;
        }

        @Override
        public void onClick(View v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(browserIntent);
        }
    }
}
