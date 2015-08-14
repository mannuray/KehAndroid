package com.dubmania.dubsmania.utils;

import android.content.Context;
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
    Context mContext;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "dubsmaniaPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";

    public SessionManager(Context context){
        this.mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
    }

    public void createLoginSession(String name, String email){
        mEditor.putBoolean(IS_LOGIN, true);
        mEditor.putString(KEY_NAME, name);
        mEditor.putString(KEY_EMAIL, email);
        mEditor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            displayRegisterDialog();
        }
    }

    public String getUser() {
        return mSharedPreferences.getString(KEY_NAME, null);
    }

    public String getUserEmail() {
        return mSharedPreferences.getString(KEY_EMAIL, null);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
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
        new AlertDialog.Builder(mContext)
            .setTitle(R.string.register_dialog_title)
            .setMessage(R.string.register_dialog_text)
            .setPositiveButton(R.string.register_dialog_register, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(mContext, SignupAndLoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            })
            .setNegativeButton(R.string.register_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }
}
