package com.zoctan.solar.user.view;

import com.zoctan.solar.beans.UserBean;

/**
 * 用户登录或注册视图接口
 */
public interface UserLoginView {

    // 正在登录时, 显示圈圈
    void showLoading();

    // 成功后, 隐藏圈圈
    void hideLoading();

    // 显示成功信息
    void showSuccessMsg(UserBean userBean);

    // 若失败, 则需要给用户提示信息
    void showFailMsg();
}
