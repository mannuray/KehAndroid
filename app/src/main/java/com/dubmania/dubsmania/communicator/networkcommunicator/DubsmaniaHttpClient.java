package com.dubmania.dubsmania.communicator.networkcommunicator;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by hardik.parekh on 8/1/2015.
 */
public class DubsmaniaHttpClient {
    private static AsyncHttpClient mClient = new AsyncHttpClient();
    //private static PersistentCookieStore mCookieStore = new PersistentCookieStore(DubsmaniaApplication.getContext());
    //mClient.setCookieStore(mCookieStore);

    private static final String BASE_URL = "http://dubsmaniadataserver.appspot.com/dubsmaniadataserver/";

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}



