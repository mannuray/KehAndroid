package com.dubmania.dubsmania.utils;

/**
 * Created by rat on 8/9/2015.
 */
public class ConstantsStore {

    //public static final String BASE_URL = "http://dubsmaniadataserver.appspot.com/dubsmaniadataserver/";
    public static final String BASE_URL = "http://kehbackend.appspot.com/";

    private final static String SEARCH_SERVICE = "searchservice/";

    public final static String SHARED_KEY_USER_NAME = "com.dubmania.dubsmania.username";
    public final static String SHARED_KEY_USER_EMAIL = "com.dubmania.dubsmania.useremail";
    public final static String SHARED_KEY_USER_LOGIN = "com.dubmania.dubsmania.login";

    public final static String INTENT_FILE_PATH = "com.dubmania.dubsmania.share.filepath";
    public final static String VIDEO_ID = "com.dubmania.dubsmania.video.id";

    public final static String INTENT_BOARD_NAME = "com.dubmania.dubsmania.board.name";
    public final static String INTENT_BOARD_ID = "com.dubmania.dubsmania.board.id";
    public final static String INTENT_BOARD_USER_NAME = "com.dubmania.dubsmania.board.username";

    public final static String INTENT_USER_NAME = "com.dubmania.dubsmania.username";

    public final static String INTENT_VIDEO_ID = "com.dubmania.dubsmania.video.id";
    public final static String INTENT_VIDEO_TITLE = "com.dubmania.dubsmania.video.title";
    public final static String INTENT_ADD_VIDEO_ACTION = "com.dubmania.dubsmania.addvideo.action";
    public final static int INTENT_ADD_VIDEO_IMPORT = 1;
    public final static int INTENT_ADD_VIDEO_RECORD = 0;


    // URL's
    /*
    public final static String URL_DOWNLOAD_VIDEO = SEARCH_SERVICE + "getvideo";
    public final static String URL_GET_TRENDING_VIDEOS = SEARCH_SERVICE + "gettrendingvideos";
    public final static String URL_GET_DISCOVER_VIDEOS = SEARCH_SERVICE + "getdiscovervideos";
    public final static String URL_GET_FAVORIT_VIDEOS = SEARCH_SERVICE + "getfavoritvideos";
    public final static String URL_MARK_FAVORIT = USER_SERVICE + "markfavorite";
    public final static String URL_ADD_VIDEO = USER_SERVICE + "addvideo";
    public final static String URL_SEARCH_VIDEOS = SEARCH_SERVICE + "seachbytag";
    public final static String URL_GET_BOARD_VIDEOS = SEARCH_SERVICE + "getboardvideos";
    public final static String URL_GET_ICON = "searchservice/geticon";

    public final static String URL_GET_BOARDS = USER_SERVICE + "getuserboards";
    public final static String URL_ADD_BOARD = USER_SERVICE + "addvedioboard";
    public final static String URL_ADD_VIDEO_TO_BOARD = USER_SERVICE + "addvideotoboard";
    public final static String URL_GET_TRENDING_BOARDS = SEARCH_SERVICE + "gettrendingboards";

    public final static String URL_GET_TAGS = SEARCH_SERVICE + "gettags";
    public final static String GET_FAV_URL = SEARCH_SERVICE + "getfav";

    public final static String URL_LOGIN = USER_SERVICE + "login";
    public final static String URL_USER_LOGOUT = USER_SERVICE + "logout";
    public final static String URL_REGISTER = USER_SERVICE + "register";
    public final static String URL_RESET_PASSWORD = USER_SERVICE + "resetpassword";
    public final static String URL_VERIFY_USER = USER_SERVICE + "verifyuser";
    public final static String URL_VERIFY_EMAIL = USER_SERVICE + "verifyuseremail";

    public final static String URL_GET_LANGUAGES = SEARCH_SERVICE + "getlanguages"; */

    private final static String ACCOUNT = "account/";
    private final static String VIDEO = "video/";
    private final static String BOARD = "board/";

    public final static String URL_SEARCH_VIDEOS = "search";


    public final static String URL_ADD_VIDEO =              VIDEO + "add";
    public final static String URL_DOWNLOAD_VIDEO =         VIDEO + "download";
    public final static String URL_MARK_FAVORIT =           VIDEO + "favorite";
    public final static String URL_GET_TAGS =               VIDEO + "tags";
    public final static String URL_GET_FAVORIT_VIDEOS = SEARCH_SERVICE + "getfavoritvideos";

    public final static String URL_GET_BOARDS =         BOARD + "get";
    public final static String URL_ADD_BOARD =          BOARD + "add";
    public final static String URL_ADD_VIDEO_TO_BOARD = BOARD + "addvideo";
    public final static String URL_GET_BOARD_VIDEOS =   BOARD + "getvideos";

    public final static String URL_GET_TRENDING_BOARDS = BOARD + "gettrendingboards";
    public final static String URL_GET_TRENDING_VIDEOS = SEARCH_SERVICE + "gettrendingvideos";
    public final static String URL_GET_DISCOVER_VIDEOS = SEARCH_SERVICE + "getdiscovervideos";

    public final static String URL_LOGIN =           ACCOUNT + "login";
    public final static String URL_USER_LOGOUT =     ACCOUNT + "logout";
    public final static String URL_REGISTER =        ACCOUNT + "signup";
    public final static String URL_RESET_PASSWORD =  ACCOUNT + "forgot";
    public final static String URL_VERIFY_USER =     ACCOUNT + "verify/user";
    public final static String URL_VERIFY_EMAIL =    ACCOUNT + "verify/email";

    public final static String URL_GET_LANGUAGES = SEARCH_SERVICE + "getlanguages";


    // HTTP params
    public final static String PARAM_USER = "user";
    public final static String PARAM_USER_ID = "id";
    public final static String PARAM_USER_NAME = "user_name";
    public final static String PARAM_USER_EMAIL = "user_email";
    public final static String PARAM_PASSWORD = "password";
    public final static String PARAM_DOB = "dob";

    public final static String PARAM_BOARD = "board";
    public final static String PARAM_BOARD_NAME = "name";
    public final static String PARAM_BOARD_LIST = "board_list";
    public final static String PARAM_BOARD_ID = "id";
    public final static String PARAM_BOARD_ICON = "icon";


    public final static String PARAM_FAVORIT = "favorite";
    public final static String PARAM_VIDEO = "video";
    public final static String PARAM_VIDEO_ID = "id";
    public final static String PARAM_VIDEO_TITLE = "title";
    public final static String PARAM_VIDEO_FILE = "video";
    public final static String PARAM_VIDEO_FAV = "favorite";
    public final static String PARAM_VIDEO_THUMBNAIL = "thumbnail";
    public final static String PARAM_VIDEO_COUNTRY = "country";
    public final static String PARAM_VIDEO_LANGUAGE = "language";
    public final static String PARAM_VIDEO_LIST = "video_list";
    public final static String PARAM_VIDEO_UPLOAD_URL = "upload_url";

    public final static String PARAM_TAGS = "tags";
    public final static String PARAM_TAG_ID = "id";
    public final static String PARAM_TAG_NAME = "tag";

    public final static String PARAM_START = "start";
    public final static String PARAM_END = "end";
    public final static String PARAM_REGION = "region";
    public final static String PARAM_RESULT = "result";

    public final static String PARAM_LANGUAGE_ID = "id";
    public final static String PARAM_LANGUAGE_TEXT = "language";
    public final static String PARAM_LANGUAGE_LIST = "language_list";
    public final static String PARAM_COUNTRY_ID = "id";
    public final static String PARAM_COUNTRY_TEXT = "country";
    public final static String PARAM_COUNTRY_LIST = "country_list";

    public final static int PARAM_FAILUERE = 0;
    public final static int PARAM_SUCCESS = 1;
    public final static int PARAM_REGISTER_USER_EXIST = 1;
    public final static int PARAM_REGISTER_EMAIL_EXIST = 2;

    public final static int SHARE_APP_ID_MESSENGER = 0;
    public final static int SHARE_APP_ID_WHATSAPP = 1;
    public final static int SHARE_APP_ID_SAVE_GALLERY = 2;


}

