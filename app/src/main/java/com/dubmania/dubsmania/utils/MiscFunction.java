package com.dubmania.dubsmania.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mannuk on 9/16/15.
 */
public class MiscFunction {

    public static String getRandomFileName(String prefix) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_hhmmss_SSS", Locale.getDefault());
        return String.format(prefix + "_File_%s", sdf.format(new Date()));
    }
}
