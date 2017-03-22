package com.zoctan.solar.user.model;

import android.graphics.Bitmap;

import com.zoctan.solar.beans.UserBean;

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
}
