package com.zoctan.solar.post.presenter;

import android.content.Context;

import com.zoctan.solar.beans.PostDetailBean;
import com.zoctan.solar.post.model.PostModel;
import com.zoctan.solar.post.model.PostModelImpl;
import com.zoctan.solar.post.view.PostDetailView;
import com.zoctan.solar.utils.LogUtils;

/**
 * Post详情回调实现类
 */

public class PostDetailPresenter {
    private String TAG = "PostDetailPresenter";
    private Context mContext;
    private PostDetailView mPostDetailView;
    private PostModel mPostModel;

    public PostDetailPresenter(Context context,PostDetailView postDetailView){
        this.mPostDetailView=postDetailView;
        this.mContext=context;
        mPostModel=new PostModelImpl();
    }
    public void loadPostDetail(final String id){
        // show Loading spin
        LogUtils.d(TAG,"loading postDetail");
        mPostDetailView.showLoading();
        mPostModel.loadPostDetail(id,new OnLoadPostDetailListener());
    }
    private class OnLoadPostDetailListener implements PostModel.OnLoadPostDetailListener{
        @Override
        public void onSuccess(PostDetailBean postDetailBean){
            if(postDetailBean!=null){
                LogUtils.d(TAG,"get PostDetailBean Success, starting show content");
                mPostDetailView.showPostDetailContent(postDetailBean.getPost_content());
                if(Integer.parseInt(postDetailBean.getPost_comments())>0){
                    mPostDetailView.addComment(postDetailBean.postCommentBeen);
                }
            }
            mPostDetailView.hideLoading();
        }
        @Override
        public void onFailure(String msg,Exception e){
            LogUtils.d(TAG, "get post detail failed");
            // hide Loading spin
            mPostDetailView.hideLoading();
        }
    }

}
