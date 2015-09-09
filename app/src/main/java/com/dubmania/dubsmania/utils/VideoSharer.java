package com.dubmania.dubsmania.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.dubmania.dubsmania.R;

import java.io.File;

/**
 * Created by rat on 8/10/2015.
 */
public class VideoSharer {
    private Activity mActivity;
    private File mFile;

    public VideoSharer(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void saveInGallery(File mFile) {
        //sending broadcast message to scan the media file so that it can be available
        Log.i("prepare", "file is " + mFile.getAbsolutePath());
        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mFile)));
    }

    public void shareViaApp(File mFile, String mPackage) {
        Uri uri = Uri.parse(mFile.getAbsolutePath());
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.setType("video/mp4");
        intent.setPackage(mPackage);
        mActivity.startActivity(intent);
    }

    public void showAlertDialog(String mFilePath) {
        this.mFile = new File(mFilePath);
        getShareAlertDialog().show();
    }

    public void showAlertDialog(File mFile) {
        this.mFile = mFile;
        getShareAlertDialog().show();
    }

    private AlertDialog getShareAlertDialog() {
        String[] items = mActivity.getResources()
                .getStringArray(R.array.messenger_list);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(R.string.share_dub_title);
        //builder.setCancelable(true);
        builder.setNegativeButton(R.string.share_alert_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        shareViaApp(mFile, "com.whatsapp"); // find package name for facebook messenger
                        return;
                    case 1:
                        shareViaApp(mFile, "com.whatsapp");
                        return;
                    case 3:
                        saveInGallery(mFile);
                }
            }
        });
        return builder.create();
    }
}
