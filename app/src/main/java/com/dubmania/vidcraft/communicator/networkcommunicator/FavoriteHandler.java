package com.dubmania.vidcraft.communicator.networkcommunicator;

import com.dubmania.vidcraft.utils.ConstantsStore;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by rat on 8/11/2015.
 */
public class FavoriteHandler {

    public void markFavorite(Long mId) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_VIDEO_ID, String.valueOf(mId));
        VidsCraftHttpClient.put(ConstantsStore.URL_FAVORITE, params, new JsonHttpResponseHandler());
    }

    public void deleteFavorite(Long mId) {
        RequestParams params = new RequestParams();
        params.add(ConstantsStore.PARAM_VIDEO_ID, String.valueOf(mId));
        VidsCraftHttpClient.delete(ConstantsStore.URL_FAVORITE, params, new JsonHttpResponseHandler());
    }

}
