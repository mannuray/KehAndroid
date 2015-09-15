package com.dubmania.dubsmania.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.dubmania.dubsmania.R;

import java.io.File;
import java.net.URLConnection;

/**
 * Created by rat on 8/10/2015.
 */
public class VideoSharer {
    private Activity mActivity;
    private String mFilePath;

    public VideoSharer(Activity mActivity, String mFilePath) {
        this.mActivity = mActivity;
        this.mFilePath = mFilePath;
    }

    public void saveInGallery() {
        Log.i("prepare", "file is " + mFilePath);
        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(mFilePath)));
    }

    public void shareViaApp(String mPackage) {
        Log.i("Share", "file path is " + mFilePath);
        Uri uri = Uri.parse(mFilePath);
        Intent intent = new Intent();
        intent.setPackage(mPackage);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("video/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(android.content.Intent.EXTRA_TITLE, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setPackage(mPackage);
        try {
            mActivity.startActivity(intent);
        }
        catch (android.content.ActivityNotFoundException anfe) {
            mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + mPackage)));
        }
    }

    public void shareViaWhatsApp() {
        shareViaApp("com.whatsapp");
    }

    public void shareViaFacebookMessenger() {
        shareViaApp("com.facebook.katana");
    }

    public void showAlertDialog() {
        getShareAlertDialog().show();
    }

    private AlertDialog getShareAlertDialog() {
        String[] items = mActivity.getResources()
                .getStringArray(R.array.messenger_list);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(R.string.share_dub_title);
        builder.setNegativeButton(R.string.share_alert_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case ConstantsStore.SHARE_APP_ID_MESSENGER:
                        shareViaFacebookMessenger();
                        return;
                    case ConstantsStore.SHARE_APP_ID_WHATSAPP:
                        shareViaWhatsApp();
                        return;
                    case ConstantsStore.SHARE_APP_ID_SAVE_GALLERY:
                        saveInGallery();
                }
            }
        });
        return builder.create();
    }
}
