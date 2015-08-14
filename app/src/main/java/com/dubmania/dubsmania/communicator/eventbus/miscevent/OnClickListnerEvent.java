package com.dubmania.dubsmania.communicator.eventbus.miscevent;

import android.view.View;

import com.dubmania.dubsmania.communicator.eventbus.BusProvider;

/**
 * Created by rat on 8/2/2015.
 */
public class OnClickListnerEvent<EventType> implements View.OnClickListener {
    public EventType event;

    public OnClickListnerEvent(EventType e) {
        event = e;
    }

    @Override
    public void onClick(View v) {
        BusProvider.getInstance().post(event);
    }
}
