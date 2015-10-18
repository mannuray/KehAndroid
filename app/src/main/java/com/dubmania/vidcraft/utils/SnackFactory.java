package com.dubmania.vidcraft.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by rat on 10/18/2015.
 */
public class SnackFactory {

    public static Snackbar getSnack(View mView, String mMessage) {

        return Snackbar
                .make(mView, mMessage, Snackbar.LENGTH_LONG);
    }

    public static Snackbar getActionSnack(View mView, String mMessage, String mAction, View.OnClickListener mListener) {

        return Snackbar
                .make(mView, mMessage, Snackbar.LENGTH_LONG)
                .setAction(mAction, mListener);
    }

    public static Snackbar getInternetConnectionRetrySnack(View mView,View.OnClickListener mListener) {
        return getActionSnack(mView, "Please check you internet connection", "RETRY", mListener);
    }

    public static Snackbar getLoginFailSnack(View mView) {
        return getSnack(mView, "Login Failed, unknown user or wrong password");
    }
}
