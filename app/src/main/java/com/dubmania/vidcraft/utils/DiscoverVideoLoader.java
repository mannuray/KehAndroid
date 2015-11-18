package com.dubmania.vidcraft.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dubmania.vidcraft.Adapters.ListItem;
import com.dubmania.vidcraft.Adapters.VideoBoardListItem;
import com.dubmania.vidcraft.Adapters.VideoListItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mannuk on 11/18/15.
 */
public class DiscoverVideoLoader {
    public static ArrayList<ListItem> mItemList;
    private static boolean mItemListAvailable = false;

    public static void loadListFromSharedPreference() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(VidCraftApplication.getContext());
        String savedValue = prefs.getString("discover_list", "");

        Gson gson = new GsonBuilder().create();
        try {
            JSONArray contacts = new JSONArray(savedValue);
            mItemList = new ArrayList<>();

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);
                if (c.getString("mType").equals("video")) {
                    mItemList.add(gson.fromJson(c.toString(), VideoListItem.class));
                } else if (c.getString("mType").equals("board")) {
                    mItemList.add(gson.fromJson(c.toString(), VideoBoardListItem.class));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mItemListAvailable = true;
    }

    public static ArrayList<ListItem> getListItem() {
        while (!mItemListAvailable) {
            ;
        }
        return mItemList;
    }
}
