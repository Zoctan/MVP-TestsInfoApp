package com.zoctan.solar.group.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoctan.solar.R;
import com.zoctan.solar.beans.GroupBean;
import com.zoctan.solar.group.GroupAdapter;
import com.zoctan.solar.group.presenter.GroupPresenter;
import com.zoctan.solar.group.view.GroupView;
import com.zoctan.solar.post.widget.PostListActivity;
import com.zoctan.solar.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 3/5/17.
 */

public class GroupFragment extends Fragment implements GroupView,SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "GroupListFragment";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private GroupAdapter mAdapter;
    private GroupPresenter mGroupPresenter;
    private List<GroupBean>mData;
    private int mType=1;
    private int pageIndex=0;
    private SwipeRefreshLayout mSwipeRefreshWidget;

    public static final int POST_TYPE_GROUP=0;

    @Override
    public void  onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mGroupPresenter = new GroupPresenter(this);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_grouplist, null);


        // 下拉刷新组件SwipeRefreshLayout
        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget_group);
        // 设置刷新时动画的颜色，可以设置4个
        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary, R.color.divider, R.color.primary_light, R.color.accent);
        // 下拉刷新监听
        mSwipeRefreshWidget.setOnRefreshListener(this);


        // RecyclerView setup
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycle_view_group);
        // 固定RecyclerView大小
        mRecyclerView.setHasFixedSize(true);
        // 设置布局管理器
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // adapter relative
        mAdapter = new GroupAdapter(getActivity().getApplicationContext());
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        /** The group list don't need the scroll loading
         mRecyclerView.addOnScrollListener(mOnScrollListener);
         */

        onRefresh();
        return view;

    }
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        // 最后一个可见的item
        private int lastVisibleItem;

        // 监听屏幕滚动的item的数量
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            // SCROLL_STATE_FLING:表示手指做了抛的动作(手指离开屏幕前,用力滑了一下,屏幕产生惯性滑动)
            // SCROLL_STATE_IDLE:表示屏幕已停止,屏幕停止滚动时为0
            // SCROLL_STATE_TOUCH_SCROLL:表示正在滚动,当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()
                    && mAdapter.isShowFooter()) {
                LogUtils.d(TAG, "加载更多");
                mGroupPresenter.loadGroup(mType, pageIndex);
            }
        }
    };
    private GroupAdapter.OnItemClickListener mOnItemClickListener = new GroupAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mData.size() <= 0) {
                return;
            }
            GroupBean group = mAdapter.getItem(position);

            Intent intent = new Intent(getActivity(), PostListActivity.class);
            intent.putExtra("group", group);

            View transitionView = view.findViewById(R.id.ivGroup);

            ActivityOptionsCompat options =
                    ActivityOptionsCompat.
                            makeSceneTransitionAnimation(
                            getActivity(), transitionView, getString(R.string.transition_test_img));

            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

        }
    };

    @Override
    public void onRefresh() {
        pageIndex = 0;
        if (mData != null) {
            mData.clear();
        }
        mGroupPresenter.loadGroup(mType, pageIndex);
    }
    @Override
    public void addGroup(List<GroupBean>groupList){
        mAdapter.isShowFooter();
        if(mData==null){
            mData=new ArrayList<>();
        }
        mData.addAll(groupList);
        if(pageIndex==0){
            mAdapter.setmData(mData);
        }else{
            if(groupList.size()==0){
                mAdapter.isShowFooter(false);
            }
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.notifyDataSetChanged();
        pageIndex+=0;
    }
    @Override
    public void hideLoading() {
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshWidget.setRefreshing(true);
    }

    @Override
    public void showLoadFailMsg() {
        View view = getActivity() == null ? mRecyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        Snackbar.make(view, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
    }
}


