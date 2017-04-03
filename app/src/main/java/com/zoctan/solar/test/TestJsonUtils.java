package com.zoctan.solar.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zoctan.solar.beans.TestBean;
import com.zoctan.solar.beans.TestDetailBean;
import com.zoctan.solar.utils.JsonUtils;
import com.zoctan.solar.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 将JSON转换为对象
 */
public class TestJsonUtils {

    private final static String TAG = "TestJsonUtils";
    private static JsonParser mJsonParser;
    private static JsonObject mJsonObject;
    private static JsonElement mJsonElement;

    public static List<TestBean> readJsonTestBeans(String res, String id) {
        // 将Test实例化成列表List
        List<TestBean> beans = new ArrayList<>();
        try {
            // 创建一个JsonParser
            mJsonParser = new JsonParser();
            // 将res转换成Json对象
            mJsonObject = mJsonParser.parse(res).getAsJsonObject();
            // id节点
            mJsonElement = mJsonObject.get(id);
            if(mJsonElement == null) {
                // 如果节点为空
                return null;
            }
            // 将id节点下的内容转为JsonArray数组
            JsonArray jsonArray = mJsonElement.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                // 获取第i个数组元素并转换成Json对象
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                // 将Json对象转换为Test实体对象
                TestBean test = JsonUtils.deserialize(jo, TestBean.class);
                // 添加Test到列表中
                beans.add(test);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "将JSON转换为Test列表对象发生错误" , e);
        }
        return beans;
    }

    public static TestDetailBean readJsonTestDetailBeans(String res, String id) {
        TestDetailBean testDetailBean = null;
        try {
            // 创建一个JsonParser
            mJsonParser = new JsonParser();
            // 将res转换成Json对象
            mJsonObject = mJsonParser.parse(res).getAsJsonObject();
            // id节点
            mJsonElement = mJsonObject.get(id);
            if(mJsonElement == null) {
                // 如果节点为空
                return null;
            }
            // 将Json对象转换为Test实体对象
            testDetailBean = JsonUtils.deserialize(mJsonElement.getAsJsonObject(), TestDetailBean.class);
        } catch (Exception e) {
            LogUtils.e(TAG, "将JSON转换为Test对象发生错误" , e);
        }
        return testDetailBean;
    }

}
