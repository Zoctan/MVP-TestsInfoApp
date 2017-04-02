package com.zoctan.solar.post.presenter;

import android.content.Context;

import com.zoctan.solar.beans.PostDetailBean;
import com.zoctan.solar.post.model.PostModel;
import com.zoctan.solar.post.model.PostModelImpl;
import com.zoctan.solar.post.view.PostDetailView;

/**
 * Post详情回调实现类
 */

public class PostDetailPresenter {
    private Context mContext;
    private PostDetailView mPostDetailView;
    private PostModel mPostModel;

    public PostDetailPresenter(Context context,PostDetailView postDetailView){
        this.mPostDetailView=postDetailView;
        this.mContext=context;
        mPostModel=new PostModelImpl();
    }
    public void loadPostDetail(final String id){
        mPostDetailView.showLoading();
        mPostModel.loadPostDetail(id,new OnLoadPostDetailListener());
    }
    private class OnLoadPostDetailListener implements PostModel.OnLoadPostDetailListener{
        @Override
        public void onSuccess(final PostDetailBean postDetailBean){
            if(postDetailBean!=null){
                mPostDetailView.showPostDetailContent(postDetailBean.getPost_content());
                if(Integer.parseInt(postDetailBean.getPost_comments())>0){
                    mPostDetailView.addComment(postDetailBean.postCommentBeen);
                }
            }
            mPostDetailView.hideLoading();
        }
        @Override
        public void onFailure(final String msg,Exception e){
            mPostDetailView.hideLoading();
        }
    }
}
