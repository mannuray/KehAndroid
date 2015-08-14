package com.dubmania.dubsmania.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.style.DynamicDrawableSpan;

import com.dubmania.dubsmania.R;

/**
 * Created by rat on 8/13/2015.
 */
class BubbleSpan extends DynamicDrawableSpan {
    private Context c;

    public BubbleSpan(Context context) {
        super();
        c = context;
    }

    @Override
    public Drawable getDrawable() {
        Resources res = c.getResources();
        Drawable d = ContextCompat.getDrawable(c, R.drawable.oval);
        d.setBounds(0, 0, 100, 20);
        return d;
    }
}