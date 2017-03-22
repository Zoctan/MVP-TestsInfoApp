package com.zoctan.solar.group.model;

import com.zoctan.solar.api.GroupUrls;
import com.zoctan.solar.beans.GroupBean;
import com.zoctan.solar.group.GroupJsonUtils;
import com.zoctan.solar.utils.OkHttpUtils;

import java.util.List;

/**
 * Group信息接口实现
 */

public class GroupModelImpl implements GroupModel {
    @Override
    public void loadGroup(String url,final int type,final OnLoadGroupListListener listener){
        OkHttpUtils.ResultCallback<String>loadGroupCallback=new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                List<GroupBean>groupBeanList= GroupJsonUtils.readJsonGroupBeans(response,getID(type));
                listener.onSuccess(groupBeanList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("Loading Group List failed",e);
            }
        };
        OkHttpUtils.get(url,loadGroupCallback);
    }
    private String getID(int type){
        String id;
        switch (type) {
            default:
                id = GroupUrls.GRUOP_ID;
        }
        return id;
    }
}
