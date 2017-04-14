package com.zoctan.solar.user.model;

import android.graphics.Bitmap;

import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.beans.UserBean;

import java.util.List;

/**
 * 用户接口
 */
public interface UserModel {
    // 根据不同用户操作的共用接口
    // type: login, register, modifyPsd, modifyImg
    void userAction(String type, String name, String password, OnLoadUserListener listener);

    // 修改头像
    void modifyImg(String type, String name, Bitmap bitmap, OnLoadUserListener listener);

    interface OnLoadUserListener {
        void onSuccess(UserBean userBean);
        void onFailure(String msg, Exception e);
    }

    // 如果加入了小组，载入最新的信息
    void loadPost(String type, String groupId,UserModel.OnLoadPostListListener listener);

    interface OnLoadPostListListener{
        void onSuccess(List<PostBean> list);
        void onFailure(String msg,Exception e);
    }

    // 退出小组
    void outGroup(String type, String user_id, UserModel.OnOutGroupListener listener);

    interface OnOutGroupListener{
        void onSuccess();
        void onFailure(String msg,Exception e);
    }
}
