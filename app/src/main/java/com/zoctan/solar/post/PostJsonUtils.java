package com.zoctan.solar.post;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.beans.PostCommentBean;
import com.zoctan.solar.beans.PostDetailBean;
import com.zoctan.solar.utils.JsonUtils;
import com.zoctan.solar.utils.LogUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * 将JSON转换为对象
 */

public class PostJsonUtils {
    private final static String TAG="PostJsonUtils";
    private static JsonParser mJsonParser;
    private static JsonObject mJsonObject;
    private static JsonElement mJsonElement;

    public static List<PostBean> readJsonPostBeans(String res,String id){
        List<PostBean> beans = new ArrayList<>();
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
                // 将Json对象转换为Post实体对象
                PostBean post = JsonUtils.deserialize(jo, PostBean.class);
                // 添加Test到列表中
                beans.add(post);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "将JSON转换为Post列表对象发生错误" , e);
        }
        return beans;
    }
    public static PostDetailBean readJsonPostDetailBeans(String res,String id){
        PostDetailBean postDetailBean = null;
        List<PostCommentBean> comments = new ArrayList<>();
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

            JsonArray mJsonArray = mJsonElement.getAsJsonArray();
            postDetailBean = JsonUtils.deserialize(mJsonArray.get(0).getAsJsonObject(), PostDetailBean.class);

            for(int i = 1; i< mJsonArray.size(); i++){
                JsonObject jo = mJsonArray.get(i).getAsJsonObject();
                PostCommentBean postCommentBean = JsonUtils.deserialize(jo,PostCommentBean.class);
                comments.add(postCommentBean);
            }
            postDetailBean.postCommentBeen=comments;

        } catch (Exception e) {
            LogUtils.e(TAG, "将JSON转换为post对象发生错误" , e);
        }
        return postDetailBean;
    }
}
