package com.dubmania.dubsmania.misc;

import android.app.Application;
import android.content.Context;

/**
 * Created by rat on 8/7/2015.
 */
public class DubsmaniaApplication extends Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
