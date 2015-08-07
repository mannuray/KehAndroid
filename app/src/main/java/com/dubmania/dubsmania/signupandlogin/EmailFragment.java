package com.dubmania.dubsmania.signupandlogin;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.EmailCheckEvent;
import com.dubmania.dubsmania.communicator.eventbus.EmailExistEvent;
import com.squareup.otto.Subscribe;

public class EmailFragment extends Fragment {

    Button next;
    EditText mEmail;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_signup_email, container, false);
        mEmail = (EditText) rootView.findViewById(R.id.email);
        try {
            Account[] accounts = AccountManager.get(getActivity()).getAccountsByType("com.google");
            for (Account account : accounts) {
                //mEmail.setText(account.name);
                mEmail.setText("ducetapa@example.com");
                break;
            }
        } catch (Exception exception) {
            Log.i("Exception", "Exception:" + exception);
        }

        next = (Button) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new EmailCheckEvent(mEmail.getText().toString()));
            }
        });
        return rootView;
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
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

    @Subscribe
    public void onEmailExistEvent(EmailExistEvent event) {
        // TO DO reset, i.e. stop progress bar
    }
}