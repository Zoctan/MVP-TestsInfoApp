package com.zoctan.solar.post.presenter;

import com.zoctan.solar.api.PostUrls;
import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.post.model.PostModel;
import com.zoctan.solar.post.model.PostModelImpl;
import com.zoctan.solar.post.view.PostView;
import com.zoctan.solar.post.widget.PostAddFragment;


import java.util.List;

/**
 * Post列表回调实现类
 */

public class PostPresenter {

    private PostView mPostView;
    private PostModel mPostModel;
    private PostAddFragment mPostAddFragment;

    public PostPresenter(PostView postView){
        this.mPostView=postView;
        this.mPostModel=new PostModelImpl();
    }
    public PostPresenter(PostAddFragment mPostAddFragment){
        this.mPostAddFragment=mPostAddFragment;
        this.mPostModel=new PostModelImpl();
    }
    public void sendPost(String group_id, String title,String content,String user_id){
        OnSendPostListener listener= new OnSendPostListener();
        mPostModel.sendPost(group_id, title,content,user_id,listener);
    }
    private class OnSendPostListener implements PostModel.OnSendPostListener{
        @Override
        public void onSuccess(){
            mPostAddFragment.queryAction();
        }
        @Override
        public void onFailure(String msg,Exception e){
            mPostAddFragment.showFailedMessage();
        }
    }
    public void loadPost(final int groupId,final int pageIndex){
        String url = getUrl(groupId,pageIndex);
        if(pageIndex==0){
            mPostView.showLoading();
        }
        mPostModel.loadPost(url, groupId, new OnLoadPostListListener());
    }

    private String getUrl(int groupId,int pageIndex){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PostUrls.POST_LIST).append("?group=").append(""+groupId);
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
