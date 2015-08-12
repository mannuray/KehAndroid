package com.dubmania.dubsmania.communicator.eventbus;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rat on 8/12/2015.
 */
public class OnTouchListnerEvent<EventType> implements View.OnTouchListener{
    public EventType event;

    public OnTouchListnerEvent(EventType e) {
        event = e;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        BusProvider.getInstance().post(event);
        return false;
    }
}
