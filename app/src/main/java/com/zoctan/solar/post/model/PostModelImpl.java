package com.zoctan.solar.post.model;

import com.zoctan.solar.api.PostUrls;
import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.beans.PostDetailBean;
import com.zoctan.solar.post.PostJsonUtils;
import com.zoctan.solar.utils.LogUtils;
import com.zoctan.solar.utils.OkHttpUtils;
import com.zoctan.solar.utils.OkHttpUtils.Param;

import java.util.ArrayList;
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
                listener.onFailure("贴子列表加载失败",e);
            }
        };
        OkHttpUtils.get(url,loadPostCallback);
    }

    @Override
    public void loadPostDetail(final String id,final OnLoadPostDetailListener listener){
        String url = PostUrls.POST_DETAIL + "?post=" + id;
        OkHttpUtils.ResultCallback<String> loadPostCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                PostDetailBean postDetailBean  = PostJsonUtils.readJsonPostDetailBeans(response,id);
                listener.onSuccess(postDetailBean);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("贴子加载失败",e);
            }
        };
        OkHttpUtils.get(url,loadPostCallback);
    }

    @Override
    public void sendPost(String group_id, String title,String content,String user_id,final OnSendPostListener listener){
        String url = PostUrls.POST_LIST;
        OkHttpUtils.ResultCallback<String> sendPostCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                listener.onSuccess();
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFailure("贴子发表失败",e);
            }
        };
        // 组成post表单
        List<Param> post_params = new ArrayList<>();
        Param post_group = new Param("group", group_id);
        Param post_title = new Param("title", title);
        Param post_content = new Param("content", content);
        Param post_user_id = new Param("user", user_id);
        post_params.add(post_group);
        post_params.add(post_title);
        post_params.add(post_content);
        post_params.add(post_user_id);
        OkHttpUtils.post(url,post_params,sendPostCallback);
    }

    @Override
    public void sendPostComment(String post_id,String comment,String user_id,final OnSendPostCommentListener listener){
        String url = PostUrls.POST_DETAIL;
        OkHttpUtils.ResultCallback<String> sendPostCommentCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                listener.onSuccess();
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFailure("评论发表失败",e);
            }
        };
        // 组成post表单
        List<Param> comment_params = new ArrayList<>();
        Param comment_post = new Param("post", post_id);
        Param comment_content = new Param("comment", comment);
        Param comment_user_id = new Param("user", user_id);
        comment_params.add(comment_post);
        comment_params.add(comment_content);
        comment_params.add(comment_user_id);
        OkHttpUtils.post(url,comment_params,sendPostCommentCallback);
    }
}
