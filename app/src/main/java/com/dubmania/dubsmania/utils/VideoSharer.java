package com.dubmania.dubsmania.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by rat on 8/10/2015.
 */
public class VideoSharer {
    public static void saveInGallery(Activity mActivity, File mFile) {
        /*ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "Video_" + mFile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Audio.Media.DATA, mFile.getAbsolutePath());

        //creating content resolver and storing it in the external content uri
        ContentResolver contentResolver = mActivity.getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);*/

        //sending broadcast message to scan the media file so that it can be available
        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mFile)));
    }
}
