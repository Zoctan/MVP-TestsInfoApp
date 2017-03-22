package com.zoctan.solar.post.model;

import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.beans.PostDetailBean;

import java.util.List;

/**
 * Created by root on 3/7/17.
 */

public interface PostModel {
    void loadPost(String url,int type,OnLoadPostListListener listener);

    interface OnLoadPostListListener{
        void onSuccess(List<PostBean> list);
        void onFailure(String msg,Exception e);
    }

    void loadPostDetail(String id, OnLoadPostDetailListener listener);

    interface OnLoadPostDetailListener{
        void onSuccess(PostDetailBean postDetailBean);
        void onFailure(String msg,Exception e);
    }
}
