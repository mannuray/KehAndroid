package com.dubmania.dubsmania.restclient;


import org.apache.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.*;

/**
 * Created by hardik.parekh on 8/1/2015.
 */
public class RestClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, final RequestListener listener) {
        client.get(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                //Some debugging code here
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                listener.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                listener.onSuccess(statusCode, headers, response);
            }


        });
    }
    public static void get(String url, final RequestListener listener) {
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                //Some debugging code here
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                listener.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                listener.onSuccess(statusCode, headers, response);
            }


        });
    }

    public static void post(String url, RequestParams params, final RequestListener listener) {
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                //Some debugging code here
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                listener.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                listener.onSuccess(statusCode, headers, response);
            }
        });
    }

    public static void post(String url,final RequestListener listener) {
        client.post(url, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                //Some debugging code here
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                listener.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                listener.onSuccess(statusCode, headers, response);
            }
        });
    }


}


