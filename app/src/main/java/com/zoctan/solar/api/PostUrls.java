package com.zoctan.solar.api;

public class PostUrls {
    // http://www.stormsaber.com/solar/api/postlist.php?group=
    private static final String HOST = "http://www.stormsaber.com/solar/api/";

    public static final String POST_LIST = HOST + "postlist.php";

    public static final int PAGE_SIZE = 7;

    // group:1
    public static final String GROUP_ID = "1";
    // group:2
    public static final String LAPTOP_ID = "2";
    // group:3
    public static final String EARPHONE_ID = "3";

    // 详情
    public static final String POST_DETAIL = HOST + "post.php";
}
