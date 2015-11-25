package com.dubmania.vidcraft.communicator.networkcommunicator;


import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

/**
 * Created by hardik.parekh on 8/1/2015.
 */
public class VidsCraftHttpClient {
    private static AsyncHttpClient mClient = new AsyncHttpClient();

    public static void setCookieStore(PersistentCookieStore cookieStore){
        mClient.setCookieStore(cookieStore);
    }

    public static RequestHandle get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        return mClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static RequestHandle post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        return mClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static RequestHandle postAbsolute(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        return mClient.post(url, params, responseHandler);
    }

    public static void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.delete(url, params, responseHandler);
    }

    public static RequestHandle head(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        return mClient.head(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return ConstantsStore.BASE_URL + relativeUrl;
    }

}



