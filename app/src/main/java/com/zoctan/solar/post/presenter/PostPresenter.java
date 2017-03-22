package com.zoctan.solar.post.presenter;

import com.zoctan.solar.api.PostUrls;
import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.post.model.PostModel;
import com.zoctan.solar.post.model.PostModelImpl;
import com.zoctan.solar.post.view.PostView;
import com.zoctan.solar.utils.LogUtils;

import java.util.List;

/**
 * Post列表回调实现类
 */

public class PostPresenter {
    private static final String TAG = "PostPresenterImpl";
    private PostView mPostView;
    private PostModel mPostModel;

    public PostPresenter(PostView postView){
        this.mPostView=postView;
        this.mPostModel=new PostModelImpl();
    }

    public void loadPost(final int type,final int pageIndex){
        String url = getUrl(type,pageIndex);
        LogUtils.d(TAG,url);
        if(pageIndex==0){
            mPostView.showLoading();
        }
        mPostModel.loadPost(url,type,new OnLoadPostListListener());
    }

    private String getUrl(int type,int pageIndex){
        StringBuilder stringBuilder = new StringBuilder();
        switch (type){

            default:
                stringBuilder.append(PostUrls.POST_LIST).append("?group=").append(""+type);
        }
        stringBuilder.append("&start=").append(pageIndex).append("&size=").append(PostUrls.PAGE_SIZE);
        return stringBuilder.toString();
    }
    private class OnLoadPostListListener implements PostModel.OnLoadPostListListener{
        @Override
        public void onSuccess(List<PostBean> list){
            mPostView.hideLoading();
            mPostView.addPost(list);
        }
        @Override
        public void onFailure(String msg,Exception e){
            mPostView.hideLoading();
            mPostView.showLoadFailMsg();
        }
    }
}
