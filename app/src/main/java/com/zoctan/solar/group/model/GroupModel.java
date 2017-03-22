package com.zoctan.solar.group.model;

import com.zoctan.solar.beans.GroupBean;

import java.util.List;

/**
 * Created by root on 3/8/17.
 */

public interface GroupModel {
    // load groupItem interface
    void loadGroup(String url,int type,OnLoadGroupListListener listener);

    interface OnLoadGroupListListener {
        void onSuccess(List<GroupBean>list);
        void onFailure(String msg,Exception e);
    }

}
