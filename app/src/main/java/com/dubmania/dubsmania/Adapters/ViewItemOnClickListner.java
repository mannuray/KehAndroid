package com.dubmania.dubsmania.Adapters;

import android.view.View;

import com.dubmania.dubsmania.communicator.BusProvider;

/**
 * Created by rat on 8/2/2015.
 */
public class ViewItemOnClickListner<EventType> implements View.OnClickListener {
    public EventType event;

    public ViewItemOnClickListner(EventType e) {
        event = e;
    }

    @Override
    public void onClick(View v) {
        BusProvider.getInstance().post(event);
    }
}
