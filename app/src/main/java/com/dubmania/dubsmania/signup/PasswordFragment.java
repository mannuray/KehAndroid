package com.dubmania.dubsmania.signup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.restclient.RequestListener;
import com.dubmania.dubsmania.restclient.RestClient;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PasswordFragment extends Fragment implements RequestListener {

    OnButtonClickListner mCallback;
    EditText mEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_signup_password, container, false);
        Button next = (Button) rootView.findViewById(R.id.next);
        mEdit = (EditText) rootView.findViewById(R.id.password);
        //TODO Change URL. This is dummy for testing. Make sure Data Connection is working.
        RestClient.get("http://jsonplaceholder.typicode.com/posts/1", this);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onClickNextButton(3);
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

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        //TODO Change it once Server is ready. This is dummy for testing purpose
        try {
            mEdit.setText(response.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        mEdit.setText("Cannot Connect to Server");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        mEdit.setText("Cannot Connect to Server");
    }
}