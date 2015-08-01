package com.dubmania.dubsmania.restclient;

import org.apache.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by hardik.parekh on 8/1/2015.
 */
public interface RequestListener {

    public void onSuccess(int statusCode, Header[] headers, JSONObject response);

    public void onSuccess(int statusCode, Header[] headers, JSONArray response);

    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse);

    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse);
}


