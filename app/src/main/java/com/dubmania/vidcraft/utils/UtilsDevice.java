package com.dubmania.vidcraft.utils;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.DisplayMetrics;

import com.dubmania.vidcraft.addvideo.RecordVideoFragment;

/**
 * This utility class is for device related stuff.
 *
 * @author Sotti https://plus.google.com/+PabloCostaTirado/about
 */
public class UtilsDevice
{
    /**
     * Returns the screen width in pixels
     *
     * @param context is the context to get the resources
     *
     * @return the screen width in pixels
     */
    public static int getScreenWidth(Context context)
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return metrics.widthPixels;
    }


}
