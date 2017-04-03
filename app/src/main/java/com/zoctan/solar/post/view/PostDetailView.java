package com.zoctan.solar.post.view;

import com.zoctan.solar.beans.PostCommentBean;

import java.util.List;

public interface PostDetailView {
    // 显示Post内容
    void showPostDetailContent(String testDetailContent);
    // 加载数据的过程中需要提示“正在加载”的反馈信息给用户
    void showLoading();
    // 加载成功后，需要将“正在加载”反馈信息取消掉
    void hideLoading();

    void addComment(List<PostCommentBean> postCommentBeen);

    void queryAction();

    void showFailMessage();
}
