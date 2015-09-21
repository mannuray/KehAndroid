package com.dubmania.vidcraft.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mannuk on 9/16/15.
 */
public class MiscFunction {

    public static String getRandomFileName(String prefix) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_hhmmss_SSS", Locale.getDefault());
        return String.format(prefix + "_File_%s", sdf.format(new Date()));
    }
}
