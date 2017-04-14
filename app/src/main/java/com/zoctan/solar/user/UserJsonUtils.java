package com.zoctan.solar.user;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zoctan.solar.beans.UserBean;
import com.zoctan.solar.utils.JsonUtils;
import com.zoctan.solar.utils.LogUtils;

/**
 * 将JSON转换为User对象
 */
public class UserJsonUtils {

    private final static String TAG = "UserJsonUtils";

    public static UserBean readJsonUserBeans(String res) {
        UserBean userBean = null;
        try {
            // 创建一个JsonParser
            JsonParser parser = new JsonParser();
            // 将res转换成Json对象
            JsonObject jsonObj = parser.parse(res).getAsJsonObject();
            // 将Json对象转换为User实体
            userBean = JsonUtils.deserialize(jsonObj, UserBean.class);
        } catch (Exception e) {
            LogUtils.e(TAG, "将JSON转换为User对象发生错误" , e);
        }
        return userBean;
    }
}
