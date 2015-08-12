package com.dubmania.dubsmania.utils;

/**
 * Created by rat on 8/9/2015.
 */
public class ConstantsStore {

    public static final String BASE_URL = "http://dubsmaniadataserver.appspot.com/dubsmaniadataserver/";
    private final static String USER_SERVICE = "userservice/";
    private final static String SEARCH_SERVIEC = "searchservice/";

    public final static String USER_NAME_KEY = "com.dubmania.dubsmania.username";
    public final static String USER_EMAL_KEY = "com.dubmania.dubsmania.useremail";
    public final static String USER_LOGIN_KEY = "com.dubmania.dubsmania.login";

    public final static String SHARE_FILE_PATH = "com.dubmania.dubsmania.share.uri";
    public final static String VIDEO_ID = "com.dubmania.dubsmania.video.id";

    public final static String INTENT_BOARD_NAME = "com.dubmania.dubsmania.board.name";
    public final static String INTENT_BOARD_ID = "com.dubmania.dubsmania.board.id";
    public final static String INTENT_BOARD_USER_NAME = "com.dubmania.dubsmania.board.username";
    public final static String INTENT_USER_NAME = "com.dubmania.dubsmania.username";
    public final static String INTENT_VIDEO_TITLE = "com.dubmania.dubsmania.video.title";

    // URL's
    public final static String USER_LOGOUT_URL = "userservice/logout";
    public final static String DOWNLOAD_VIDEO_URL = "searchservice/getvideo";
    public final static String GET_FAV_URL = "searchservice/getfav";
    public final static String GET_ICON_URL = "searchservice/geticon";
    public final static String GET_TRENDING_VIDEOS_URL = "searchservice/gettrendingvideos";
    public final static String URL_MARK_FAVORIT = "userservice/markfavorit";
    public final static String URL_ADD_BOARD = USER_SERVICE + "addboard";
    public final static String URL_ADD_VIDEO_TO_BOARD = USER_SERVICE + "addvideotoboard";
    public final static String URL_ADD_VIDEO = USER_SERVICE + "addvideo";
    public final static String URL_GET_TRENDING_BOARDS = SEARCH_SERVIEC + "gettrendingboards";
    public final static String URL_GET_DISCOVER_VIDEOS = SEARCH_SERVIEC + "getdiscovervideos";
    public final static String URL_SEARCH_VIDEOS = SEARCH_SERVIEC + "searchvideo";
    public final static String URL_GET_BOARD_VIDEOS = SEARCH_SERVIEC + "getboardvideos";

    // HTTP params
    public final static String PARAM_USER = "user";
    public final static String PARAM_BOARD_NAME = "boardname";
    public final static String PARAM_FAVORIT = "favorit";
    public final static String PARAM_VIDEO_ID = "videoid";
    public final static String PARAM_BOARD_ID = "boardid";
    public final static String PARAM_ICON_ID = "iconid";
    public final static String PARAM_VIDEO_TITLE = "videotitle";
    public final static String PARAM_VIDEO_DESC = "desc";
    public final static String PARAM_TAGS = "tags";
    public final static String PARAM_VIDEO_FILE = "video";
    public final static String PARAM_ICON_FILE = "icon";
    public final static String PARAM_START = "start";
    public final static String PARAM_END = "end";
    public final static String PARAM_REGION = "region";
    public final static String PARAM_SEARCH_TAG = "tag";

}