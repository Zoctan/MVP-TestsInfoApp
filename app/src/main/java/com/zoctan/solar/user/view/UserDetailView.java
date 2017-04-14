package com.zoctan.solar.user.view;

import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.beans.UserBean;

import java.util.List;

/**
 * 用户详情视图接口
 */
public interface UserDetailView {
    // 加载数据的过程中需要提示“正在加载”的反馈信息给用户
    void showLoading();
    // 加载成功后，需要将“正在加载”反馈信息取消掉
    void hideLoading();
    // 加载成功后，将加载得到的数据填充到RecyclerView展示给用户
    void addPost(List<PostBean> postList);
    // 显示成功信息
    void showSuccessMsg(UserBean userBean);
    // 若失败, 则需要给用户提示信息
    void showFailMsg();
    // 显示成功退出小组信息
    void showOutGroupSuccess();
}
