package com.zoctan.solar.group;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zoctan.solar.beans.GroupBean;
import com.zoctan.solar.utils.JsonUtils;
import com.zoctan.solar.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 3/8/17.
 */

public class GroupJsonUtils {
    private final static String TAG = "GroupJsonUtils";
    private static JsonParser mJsonParser;
    private static JsonElement mJsonElement;
    
    public static List<GroupBean>readJsonGroupBeans(String res,String id){
        // 将Group实例化成列表List
        List<GroupBean> beans = new ArrayList<>();
        try {
            // 创建一个JsonParser
            mJsonParser = new JsonParser();
            // 将res转换成Json对象
            mJsonElement = mJsonParser.parse(res).getAsJsonArray();
            // id节点
            if(mJsonElement == null) {
                // 如果节点为空
                return null;
            }
            // 将id节点下的内容转为JsonArray数组
            JsonArray jsonArray = mJsonElement.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                // 获取第i个数组元素并转换成Json对象
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                // 将Json对象转换为Group实体对象
                GroupBean group = JsonUtils.deserialize(jo, GroupBean.class);
                // 添加Group到列表中
                beans.add(group);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "将JSON转换为Group列表对象发生错误" , e);
        }
        return beans;
    }


}
