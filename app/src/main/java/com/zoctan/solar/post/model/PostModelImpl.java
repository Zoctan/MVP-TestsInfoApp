package com.zoctan.solar.post.model;

import com.zoctan.solar.api.PostUrls;
import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.beans.PostDetailBean;
import com.zoctan.solar.post.PostJsonUtils;
import com.zoctan.solar.utils.OkHttpUtils;
import java.util.List;

/**
 * Post信息接口实现
 */

public class PostModelImpl implements PostModel{

    @Override
    public void loadPost(final String url,final int groupId,final OnLoadPostListListener listener){
        OkHttpUtils.ResultCallback<String> loadPostCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                List<PostBean> postBeanList = PostJsonUtils.readJsonPostBeans(response,groupId+"");
                listener.onSuccess(postBeanList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load post list failed",e);
            }
        };
        OkHttpUtils.get(url,loadPostCallback);
    }
    @Override
    public void loadPostDetail(final String id,final OnLoadPostDetailListener listener){
        String url =getDetailUrl(id);
        OkHttpUtils.ResultCallback<String> loadPostCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                PostDetailBean postDetailBean  = PostJsonUtils.readJsonPostDetailBeans(response,id);
                listener.onSuccess(postDetailBean);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("Post detail loading failed",e);
            }
        };
        OkHttpUtils.get(url,loadPostCallback);
    }

    private String getDetailUrl(String id){
        return PostUrls.POST_DETAIL+"?post="+id;
    }
}
