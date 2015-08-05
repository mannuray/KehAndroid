package com.dubmania.dubsmania.restclient;

import org.apache.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by hardik.parekh on 8/1/2015.
 */
public interface ResultsListener<T> {
    public void onSuccess(T result);
    public void onFailure(Throwable e);
}


