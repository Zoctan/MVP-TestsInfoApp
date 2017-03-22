package com.zoctan.solar.group.presenter;

import com.zoctan.solar.api.GroupUrls;
import com.zoctan.solar.beans.GroupBean;
import com.zoctan.solar.group.model.GroupModel;
import com.zoctan.solar.group.model.GroupModelImpl;
import com.zoctan.solar.group.view.GroupView;
import com.zoctan.solar.utils.LogUtils;

import java.util.List;

/**
 * Created by root on 3/8/17.
 */

public class GroupPresenter {
    private static final String TAG = "GroupPresenterImpl";

    private GroupView mGroupView;
    private GroupModel mGroupModel;

    public GroupPresenter(GroupView groupView){
        this.mGroupModel=new GroupModelImpl();
        this.mGroupView=groupView;
    }

    public void loadGroup(final int type,final int pageIndex) {
        String url = getUrl(type, pageIndex);
        LogUtils.d(TAG, url);
        if (pageIndex == 0) {
            // 只有第一页的或者刷新的时候才显示刷新圈圈
            mGroupView.showLoading();
        }
        mGroupModel.loadGroup(url, type, new OnLoadGroupListListener());
    }


    private String getUrl(int type,int pageIndex){
        StringBuilder stringBuilder=new StringBuilder();
        switch(type){
            default:
                stringBuilder.append(GroupUrls.GROUP_LIST).append("?type=").append(GroupUrls.GRUOP_ID);
        }
        return stringBuilder.toString();
    }

    private class OnLoadGroupListListener implements GroupModel.OnLoadGroupListListener{
        @Override
        public void onSuccess(List<GroupBean>list){
            mGroupView.hideLoading();
            mGroupView.addGroup(list);
        }
        @Override
        public void onFailure(String msg,Exception e){
            mGroupView.hideLoading();
            mGroupView.showLoadFailMsg();
        }
    }
}
