package com.zoctan.solar.group.model;

import com.zoctan.solar.beans.GroupBean;

import java.util.List;

public interface GroupModel {
    // load groupItem interface
    void loadGroup(String url,OnLoadGroupListListener listener);

    interface OnLoadGroupListListener {
        void onSuccess(List<GroupBean>list);
        void onFailure(String msg,Exception e);
    }
}
