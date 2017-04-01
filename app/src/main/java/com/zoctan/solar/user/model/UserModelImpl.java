package com.zoctan.solar.user.model;

import android.graphics.Bitmap;
import android.os.Environment;

import com.zoctan.solar.api.UserUrls;
import com.zoctan.solar.beans.UserBean;
import com.zoctan.solar.user.UserJsonUtils;
import com.zoctan.solar.utils.ImageUtils;
import com.zoctan.solar.utils.LogUtils;
import com.zoctan.solar.utils.OkHttpUtils;
import com.zoctan.solar.utils.OkHttpUtils.Param;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户处理类
 */
public class UserModelImpl implements UserModel {

    // 用户登录/注册/修改密码的操作
    @Override
    public void userAction(final String type, final String name, final String password, final OnLoadUserListener listener) {
        String url = getUserUrl();
        // 组成post表单
        List<Param> userData = new ArrayList<>();
        Param actionType = new Param("type", type);
        Param userName = new Param("name", name);
        Param userPassword = new Param("password", password);
        userData.add(actionType);
        userData.add(userName);
        userData.add(userPassword);
        // 接收响应数据
        OkHttpUtils.ResultCallback<String> loadUserCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                //LogUtils.d("用户操作响应数据", response);
                UserBean userBean = UserJsonUtils.readJsonUserBeans(response);
                listener.onSuccess(userBean);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("用户登录失败", e);
            }
        };
        // 调用OkHttp的post方法
        OkHttpUtils.post(url, userData, loadUserCallback);
    }

    // 修改头像操作
    @Override
    public void modifyImg(final String type, final String name, final Bitmap bitmap, final OnLoadUserListener listener) {
        // 图片存储卡绝对路径
        String imageAbsolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        // 图片名:当前系统时间
        String imageName = String.valueOf(System.currentTimeMillis());
        // 图片本地路径
        String imagePath = ImageUtils.savePhoto(bitmap, imageAbsolutePath, imageName);
        //LogUtils.d("图片路径", imagePath);
        //LogUtils.d("图片名字", imageName);

        String url = getUserUrl();
        // 组成post表单
        List<Param> userData = new ArrayList<>();
        Param actionType = new Param("type", type);
        Param userName = new Param("name", name);
        Param userImage = new Param("image", imageName);
        userData.add(actionType);
        userData.add(userName);
        userData.add(userImage);

        // 接收响应数据
        OkHttpUtils.ResultCallback<String> uploadCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                //LogUtils.d("修改头像响应数据", response);
                UserBean userBean = UserJsonUtils.readJsonUserBeans(response);
                listener.onSuccess(userBean);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("头像修改失败", e);
            }
        };

        if(imagePath != null){
            // 拿着imagePath上传了
            File image=new File(imagePath);
            // 调用OkHttp的uploadFile方法
            OkHttpUtils.uploadFile(url, image, userData, uploadCallback);
        }
    }

    // User的api网址
    private static String getUserUrl() {
        return UserUrls.USER_DATA;
    }
}
