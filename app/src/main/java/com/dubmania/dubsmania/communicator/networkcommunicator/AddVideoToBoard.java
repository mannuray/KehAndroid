package com.dubmania.dubsmania.communicator.networkcommunicator;

import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by rat on 8/11/2015.
 */
public class AddVideoToBoard {

    public void addVideoToBoard(String boardName, Long id) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_BOARD_NAME, boardName);
        params.add(ConstantsStore.PARAM_VIDEO_ID, String.valueOf(id));
        DubsmaniaHttpClient.get(ConstantsStore.URL_ADD_VIDEO_TO_BOARD, params, new JsonHttpResponseHandler());
    }
}
