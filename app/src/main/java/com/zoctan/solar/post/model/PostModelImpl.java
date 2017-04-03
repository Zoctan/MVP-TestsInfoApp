package com.zoctan.solar.post.model;

import com.zoctan.solar.api.PostUrls;
import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.beans.PostDetailBean;
import com.zoctan.solar.post.PostJsonUtils;
import com.zoctan.solar.utils.OkHttpUtils;

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

    @Override
    public void sendPost(String title,String content,String user_id,final OnSendPostListener listener){
        List<OkHttpUtils.Param>params = buildPostUrlParam(title,content,user_id);
        String url = PostUrls.POST_LIST;
        OkHttpUtils.ResultCallback<String> sendPostCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                listener.onSuccess();
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFailure("send post list failed",e);
            }
        };
        OkHttpUtils.post(url,params,sendPostCallback);
    }
    private List<OkHttpUtils.Param> buildPostUrlParam(String title,String content,String user_id) {
        OkHttpUtils.Param mtitle = new OkHttpUtils.Param("title",title);
        OkHttpUtils.Param mcontent = new OkHttpUtils.Param("content",content);
        OkHttpUtils.Param muser_id = new OkHttpUtils.Param("user",user_id);
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(mtitle);
        params.add(mcontent);
        params.add(muser_id);
        return params;

    }
    @Override
    public void sendPostComment(String post_id,String comment,String user_id,final OnSendPostCommentListener listener){
        String url = PostUrls.POST_DETAIL;
        List<OkHttpUtils.Param> params = buildPosCommentUrl(post_id,comment,user_id);
        OkHttpUtils.ResultCallback<String> sendPostCommentCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                listener.onSuccess();
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFailure("send postComment list failed",e);
            }
        };
        OkHttpUtils.post(url,params,sendPostCommentCallback);
    }
    private List<OkHttpUtils.Param> buildPosCommentUrl(String post_id,String comment,String user_id){
        List<OkHttpUtils.Param> params = new ArrayList<>();
        OkHttpUtils.Param mpost_id = new OkHttpUtils.Param("post",post_id);
        OkHttpUtils.Param mcomment = new OkHttpUtils.Param("comment",comment);
        OkHttpUtils.Param muser_id = new OkHttpUtils.Param("user",user_id);
        params.add(mpost_id);
        params.add(mcomment);
        params.add(muser_id);
        return params;
    }

}
