package com.zoctan.solar.post.model;

import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.beans.PostDetailBean;

import java.util.List;

public interface PostModel {
    void loadPost(String url,int groupId,OnLoadPostListListener listener);

    interface OnLoadPostListListener{
        void onSuccess(List<PostBean> list);
        void onFailure(String msg,Exception e);
    }

    void loadPostDetail(String id, OnLoadPostDetailListener listener);

    interface OnLoadPostDetailListener{
        void onSuccess(PostDetailBean postDetailBean);
        void onFailure(String msg,Exception e);
    }

    interface OnSendPostListener{
        void onSuccess();
        void onFailure(String msg,Exception e);
    }
    void sendPost(String title,String content,String user_id,OnSendPostListener listener);

    interface OnSendPostCommentListener{
        void onSuccess();
        void onFailure(String msg,Exception e);
    }
    void sendPostComment(String post_id,String comment,String user_id,OnSendPostCommentListener listener);
}
