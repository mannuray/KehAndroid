package com.dubmania.vidcraft.utils;

import android.content.Context;
import android.content.Intent;

import com.dubmania.vidcraft.createdub.CreateDubActivity;

/**
 * Created by mannuk on 10/5/15.
 */
public class ActivityStarter {

    public static void createDub(Context mContext, Long id, String title) {
        Intent intent = new Intent(mContext, CreateDubActivity.class);
        intent.putExtra(ConstantsStore.VIDEO_ID, id);
        intent.putExtra(ConstantsStore.INTENT_VIDEO_TITLE, title);
        mContext.startActivity(intent);
    }
}
