package com.dubmania.dubsmania.signup;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.BusProvider;
import com.dubmania.dubsmania.events.OnEmailCheckEvent;
import com.dubmania.dubsmania.restclient.EmailCheckClient;
import com.squareup.otto.Subscribe;

public class EmailFragment extends Fragment {

    OnButtonClickListner mCallback;
    SignUpInfo info;
    Button next;
    EditText mEdit;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_signup_email, container, false);
        mEdit = (EditText) rootView.findViewById(R.id.email);
        try {
            Account[] accounts = AccountManager.get(getActivity()).getAccountsByType("com.google");
            for (Account account : accounts) {
                mEdit.setText(account.name);
                break;
            }
        } catch (Exception exception) {
            Log.i("Exception", "Exception:" + exception);
        }

        next = (Button) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setEmail(mEdit.getText().toString());
                new EmailCheckClient().checkEmail(mEdit.getText().toString());
            }
        });
        BusProvider.getInstance().register(this);
        return rootView;
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
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

    public EmailFragment setSignUpInfo(SignUpInfo _info) {
        info = _info;
        return this;
    }
    @Subscribe
    public void getMessage(OnEmailCheckEvent data) {
        if(data.isExist()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            builder.setMessage(R.string.email_already_exist)
                    .setTitle(R.string.email);

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            mCallback.onClickNextButton(1);
        }

    }
}