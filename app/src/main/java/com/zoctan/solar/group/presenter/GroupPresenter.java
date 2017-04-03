package com.zoctan.solar.group.presenter;

import com.zoctan.solar.api.GroupUrls;
import com.zoctan.solar.beans.GroupBean;
import com.zoctan.solar.group.model.GroupModel;
import com.zoctan.solar.group.model.GroupModelImpl;
import com.zoctan.solar.group.view.GroupView;
import com.zoctan.solar.utils.LogUtils;

import java.util.List;

public class GroupPresenter {
    private static final String TAG = "GroupPresenterImpl";

    private GroupView mGroupView;
    private GroupModel mGroupModel;

    public GroupPresenter(GroupView groupView){
        this.mGroupModel=new GroupModelImpl();
        this.mGroupView=groupView;
    }

    public void loadGroup() {
        String url = GroupUrls.GROUP_LIST;
        LogUtils.d(TAG, url);
        mGroupModel.loadGroup(url, new OnLoadGroupListListener());
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
