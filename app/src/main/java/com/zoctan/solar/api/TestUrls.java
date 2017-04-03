package com.zoctan.solar.api;

/**
 * 评测API网址Urls类
 */
public class TestUrls {

    // 评测列表
    // http://www.stormsaber.com/solar/api/testlist.php?type=1&start=0&size=5

    // 评测详细内容
    // http://www.stormsaber.com/solar/api/test.php?id=1

    // API网址
    private static final String HOST = "http://www.stormsaber.com/solar/api/";

    public static final String TEST_LIST = HOST + "testlist.php";

    public static final int PAGE_SIZE = 7;

    // 手机:1
    public static final String PHONE_ID = "1";
    // 笔记本:2
    public static final String LAPTOP_ID = "2";
    // 耳机:3
    public static final String EARPHONE_ID = "3";

    // 测评详情
    public static final String TEST_DETAIL = HOST + "test.php";
}
