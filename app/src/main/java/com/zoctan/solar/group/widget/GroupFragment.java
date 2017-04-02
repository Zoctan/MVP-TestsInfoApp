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

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment implements GroupView,SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView mRecyclerView;
    private GroupAdapter mAdapter;
    private GroupPresenter mGroupPresenter;
    private List<GroupBean>mData;
    private SwipeRefreshLayout mSwipeRefreshWidget;

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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // adapter relative
        mAdapter = new GroupAdapter(getActivity().getApplicationContext());
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        onRefresh();
        return view;
    }

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

            ActivityOptionsCompat options = ActivityOptionsCompat. makeSceneTransitionAnimation(getActivity(), transitionView, getString(R.string.transition_test_img));

            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        }
    };

    @Override
    public void onRefresh() {
        if (mData != null) {
            mData.clear();
        }
        mGroupPresenter.loadGroup();
    }

    @Override
    public void addGroup(List<GroupBean>groupList){
        if(mData==null){
            mData=new ArrayList<>();
        }
        mData.addAll(groupList);
        mAdapter.setmData(mData);
        mAdapter.notifyDataSetChanged();
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
