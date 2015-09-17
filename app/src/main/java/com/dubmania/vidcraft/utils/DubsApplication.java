package com.dubmania.vidcraft.utils;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by rat on 9/2/2015.
 */
public class DubsApplication extends Application {
    private static Application mInstance;
    private static GoogleAnalytics mAnalytics;
    private static Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        mAnalytics = GoogleAnalytics.getInstance(this);
        mInstance = this;

        mTracker = mAnalytics.newTracker("UA-67048919-1");
        mTracker.enableExceptionReporting(true);
        //tracker.enableAdvertisingIdCollection(true);
        //tracker.enableAutoActivityTracking(true);
    }

    public static GoogleAnalytics analytics() {
        return mAnalytics;
    }

    public static Tracker tracker() {
        return mTracker;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

}
