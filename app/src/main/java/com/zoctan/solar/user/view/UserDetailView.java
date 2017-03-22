package com.zoctan.solar.user.view;

import com.zoctan.solar.beans.UserBean;

/**
 * 用户详情视图接口
 */
public interface UserDetailView {
    // 加载数据的过程中需要提示“正在加载”的反馈信息给用户
    void showLoading();
    // 加载成功后，需要将“正在加载”反馈信息取消掉
    void hideLoading();

    // 显示成功信息
    void showSuccessMsg(UserBean userBean);

    // 若失败, 则需要给用户提示信息
    void showFailMsg();
}
