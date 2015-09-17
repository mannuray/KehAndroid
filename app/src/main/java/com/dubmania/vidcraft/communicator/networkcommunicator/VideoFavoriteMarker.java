package com.dubmania.vidcraft.communicator.networkcommunicator;

import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by rat on 8/11/2015.
 */
public class VideoFavoriteMarker {

    public void markVavrioute(Long mId, boolean value) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_VIDEO_ID, String.valueOf(mId));
        params.add(ConstantsStore.PARAM_FAVORIT,String.valueOf(value));
        DubsmaniaHttpClient.get(ConstantsStore.URL_MARK_FAVORIT, params, new JsonHttpResponseHandler());
    }

}
