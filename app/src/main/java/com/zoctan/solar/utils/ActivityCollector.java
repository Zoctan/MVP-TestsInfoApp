package com.zoctan.solar.utils;

import android.app.Activity;

import java.util.LinkedList;

/**
 * Activity管理器
 */
public class ActivityCollector {
    private static LinkedList<Activity> activities = new LinkedList<>();

    // 往集合添加Activity对象
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    // 移除集合中Activity对象
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    // 关闭集合中所有Activity对象
    public static void finishAll() {
        for(Activity activity:activities) {
            if(!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
