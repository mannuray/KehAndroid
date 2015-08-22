package com.dubmania.dubsmania.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.signupandlogin.SignupAndLoginActivity;

import java.util.HashMap;

/**
 * Created by rat on 8/14/2015.
 */
public class SessionManager {
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    Activity mActivity;
    private LoginListener mLoginListener;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "dubsmaniaPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";

    public SessionManager(Activity activity){
        this.mActivity = activity;
        mSharedPreferences = mActivity.getApplicationContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
    }

    public void createLoginSession(String name, String email){
        mEditor.putBoolean(IS_LOGIN, true);
        mEditor.putString(KEY_NAME, name);
        mEditor.putString(KEY_EMAIL, email);
        mEditor.commit();
    }

    public void checkLogin(LoginListener listener){
        mLoginListener = listener;
        if(!this.isLoggedIn()){
            displayRegisterDialog();
        }
    }

    public String getUser() {
        if(isLoggedIn())
            return mSharedPreferences.getString(KEY_NAME, null);
        else
            return "";
    }

    public String getUserEmail() {

        if(isLoggedIn())
            return mSharedPreferences.getString(KEY_EMAIL, null);
        else
            return "";
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_NAME, mSharedPreferences.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, mSharedPreferences.getString(KEY_EMAIL, null));
        return user;
    }

    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }

    public boolean isLoggedIn(){
        return mSharedPreferences.getBoolean(IS_LOGIN, false);
    }

    private void displayRegisterDialog() {
        new AlertDialog.Builder(mActivity)
            .setTitle(R.string.register_dialog_title)
            .setMessage(R.string.register_dialog_text)
            .setCancelable(false)
            .setPositiveButton(R.string.register_dialog_register, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(mActivity, SignupAndLoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mActivity.startActivityForResult(intent, 1);
                }
            })
            .setNegativeButton(R.string.register_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mLoginListener.onFailure();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    public static abstract class LoginListener {
        public abstract void onSuccess();
        public abstract void onFailure();
    }
}
