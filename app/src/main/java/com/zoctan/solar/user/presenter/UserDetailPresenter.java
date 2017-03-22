package com.zoctan.solar.user.presenter;

import android.graphics.Bitmap;

import com.zoctan.solar.beans.UserBean;
import com.zoctan.solar.user.model.UserModel;
import com.zoctan.solar.user.model.UserModelImpl;
import com.zoctan.solar.user.view.UserDetailView;

import java.util.Objects;

public class UserDetailPresenter {
    private UserDetailView mUserDetailView;
    private UserModel mUserModel;

    public UserDetailPresenter(UserDetailView userDetailView) {
        this.mUserDetailView = userDetailView;
        this.mUserModel = new UserModelImpl();
    }

    // 修改密码
    public void modifyPwd(final String type, final String name, final String password) {
        mUserDetailView.showLoading();
        mUserModel.userAction(type, name, password, new OnLoadUserListener());
    }

    // 图片上传至服务器
    public void uploadPic(final String type, final String name, Bitmap bitmap) {
        mUserDetailView.showLoading();
        mUserModel.modifyImg(type, name, bitmap, new OnLoadUserListener());
    }

    private class OnLoadUserListener implements UserModel.OnLoadUserListener {
        @Override
        public void onSuccess(UserBean userBean) {
            // 如果UserBean实体不为空
            if (userBean != null) {
                // 如果返回状态为true
                if(Objects.equals(userBean.getStatus(), "true")) {
                    mUserDetailView.showSuccessMsg(userBean);
                }else {
                    // 显示失败信息
                    mUserDetailView.showFailMsg();
                }
            }else {
                // 显示失败信息
                mUserDetailView.showFailMsg();
            }
            // 隐藏Loading圈圈
            mUserDetailView.hideLoading();
        }

        @Override
        public void onFailure(String msg, Exception e) {
            // 显示失败信息
            mUserDetailView.showFailMsg();
            // 隐藏Loading圈圈
            mUserDetailView.hideLoading();
        }
    }
}
