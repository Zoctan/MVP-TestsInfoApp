package com.zoctan.solar.utils;

import android.util.Log;

/**
 * 打印日志的工具类，方便查看log
 */
public class LogUtils {

    // 测试时是true，上线时改为false
    private static final boolean DEBUG = true;

    /**
     * verbose Log print
     * 冗余级别
     * @param tag :print tag
     * @param msg :print message
     */
    public static void v(String tag, String msg) {
        if(DEBUG) {
            Log.v(tag, msg);
        }
    }

    /**
     * debug Log print
     * 调试级别
     * @param tag :print tag
     * @param msg :print message
     */
    public static void d(String tag, String msg) {
        if(DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     * info Log print
     * 信息级别
     * @param tag :print tag
     * @param msg :print message
     */
    public static void i(String tag, String msg) {
        if(DEBUG) {
            Log.i(tag, msg);
        }
    }

    /**
     * warn Log print
     * 警告级别
     * @param tag :print tag
     * @param msg :print message
     */
    public static void w(String tag, String msg) {
        if(DEBUG) {
            Log.w(tag, msg);
        }
    }

    /**
     * err Log print
     * 错误级别
     * @param tag :print tag
     * @param msg :print message
     */
    public static void e(String tag, String msg) {
        if(DEBUG) {
            Log.e(tag, msg);
        }
    }

    /**
     * err Log print
     * 错误级别
     * @param tag :print tag
     * @param msg :print message
     * @param e :print Exception
     */
    public static void e(String tag, String msg, Exception e) {
        if(DEBUG) {
            Log.e(tag, msg, e);
        }
    }
}
