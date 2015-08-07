package com.dubmania.dubsmania.communicator.networkcommunicator;

import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.EmailCheckEvent;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hardik.parekh on 8/2/2015.
 */
public class EmailCheckClient {

    public void checkEmail(final String email) {
        DubsmaniaHttpClient.get("ip.jsontest.com", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    final EmailCheckEvent event = new EmailCheckEvent(email);
                    //event.setIsExist(email.equals((String) obj.get("ip")));
                    BusProvider.getInstance().post(event);
                } catch (JSONException jsonException) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
