package com.zoctan.solar.user.presenter;

import com.zoctan.solar.beans.UserBean;
import com.zoctan.solar.user.model.UserModel;
import com.zoctan.solar.user.model.UserModelImpl;
import com.zoctan.solar.user.view.UserLoginView;

import java.util.Objects;

/**
 * 用户登录回调实现类
 */
public class UserLoginPresenter {

    private UserLoginView mUserLoginView;
    private UserModel mUserModel;

    public UserLoginPresenter(UserLoginView userLoginView) {
        this.mUserLoginView = userLoginView;
        this.mUserModel = new UserModelImpl();
    }

    // 登录
    public void userAction(final String type, final String name, final String password) {
        mUserLoginView.showLoading();
        mUserModel.userAction(type, name, password, new OnLoadUserListener());
    }

    private class OnLoadUserListener implements UserModel.OnLoadUserListener {
        @Override
        public void onSuccess(UserBean userBean) {
            // 如果UserBean实体不为空
            if (userBean != null) {
                // 如果返回状态为true
                if(Objects.equals(userBean.getStatus(), "true")) {
                    mUserLoginView.showSuccessMsg(userBean);
                }else {
                    // 显示失败信息
                    mUserLoginView.showFailMsg();
                }
            }else {
                // 显示失败信息
                mUserLoginView.showFailMsg();
            }
            // 隐藏Loading圈圈
            mUserLoginView.hideLoading();
        }

        @Override
        public void onFailure(String msg, Exception e) {
            // 显示失败信息
            mUserLoginView.showFailMsg();
            // 隐藏Loading圈圈
            mUserLoginView.hideLoading();
        }
    }
}
