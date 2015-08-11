package com.dubmania.dubsmania.communicator.networkcommunicator;

import com.dubmania.dubsmania.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by rat on 8/11/2015.
 */
public class VideoBoardCreator {

    public void addVideoBoard(String boardName, int iconId) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_BOARD_NAME, boardName);
        params.add(ConstantsStore.PARAM_ICON_ID, String.valueOf(iconId));
        DubsmaniaHttpClient.get(ConstantsStore.URL_ADD_BOARD, params, new JsonHttpResponseHandler());
    }
}
