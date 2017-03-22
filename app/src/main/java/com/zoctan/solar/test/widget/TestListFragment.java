package com.zoctan.solar.test.widget;

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
import com.zoctan.solar.beans.TestBean;
import com.zoctan.solar.api.TestUrls;
import com.zoctan.solar.test.TestAdapter;
import com.zoctan.solar.test.presenter.TestPresenter;
import com.zoctan.solar.test.view.TestView;
import com.zoctan.solar.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 评测列表实现类
 */
public class TestListFragment extends Fragment implements TestView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TestListFragment";

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TestAdapter mAdapter;
    private List<TestBean> mData;
    private TestPresenter mTestPresenter;

    private int mType = TestFragment.TEST_TYPE_PHONE;
    private int pageIndex = 0;

    public static TestListFragment newInstance(int type) {
        Bundle args = new Bundle();
        TestListFragment fragment = new TestListFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTestPresenter = new TestPresenter(this);
        mType = getArguments().getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_testlist, null);

        // 下拉刷新组件SwipeRefreshLayout
        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        // 设置刷新时动画的颜色，可以设置4个
        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary, R.color.divider,
                                                    R.color.primary_light, R.color.accent);
        // 下拉刷新监听
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycle_view);
        // 固定RecyclerView大小
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());

        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new TestAdapter(getActivity().getApplicationContext());
        // item点击监听
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        // 设置适配器
        mRecyclerView.setAdapter(mAdapter);
        // 滚动监听
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        // 刷新
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

        // 监听RecyclerView滑动状态
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            // SCROLL_STATE_FLING:表示手指做了抛的动作(手指离开屏幕前,用力滑了一下,屏幕产生惯性滑动)
            // SCROLL_STATE_IDLE:表示屏幕已停止,屏幕停止滚动时为0
            // SCROLL_STATE_TOUCH_SCROLL:表示正在滚动,当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()
                    && mAdapter.isShowFooter()) {
                // 如果列表滑到最底部, 则加载更多
                LogUtils.d(TAG, "加载更多");
                mTestPresenter.loadTest(mType, pageIndex);
            }
        }
    };

    private TestAdapter.OnItemClickListener mOnItemClickListener = new TestAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mData.size() <= 0) {
                return;
            }
            TestBean test = mAdapter.getItem(position);
            Intent intent = new Intent(getActivity(), TestDetailActivity.class);
            intent.putExtra("test",test);

            View transitionView = view.findViewById(R.id.ivTest);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.
                            makeSceneTransitionAnimation(
                            getActivity(),
                                    transitionView,
                                    getString(R.string.transition_test_img)
                            );

            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        }
    };

    @Override
    public void showLoading() {
        mAdapter.isShowFooter(false);
        mSwipeRefreshWidget.setRefreshing(true);
    }

    @Override
    public void addTest(List<TestBean> testList) {
        mAdapter.isShowFooter(true);
        if(mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(testList);
        if(pageIndex == 0) {
            mAdapter.setmDate(mData);
        } else {
            // 如果没有更多数据,则隐藏footer布局
            if(testList.size() == 0) {
                mAdapter.isShowFooter(false);
            }
            // 处理的数据发生变化,通知View作出改变
            mAdapter.notifyDataSetChanged();
        }
        // 改变API网址start参数
        pageIndex += TestUrls.PAGE_SIZE;
    }


    @Override
    public void hideLoading() {
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {
        if(pageIndex == 0) {
            mAdapter.isShowFooter(false);
            // 处理的数据发生变化，通知View作出改变
            mAdapter.notifyDataSetChanged();
        }
        View view = getActivity() == null ? mRecyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        Snackbar.make(view, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
    }

    // 刷新
    @Override
    public void onRefresh() {
        // 当在第一页时
        pageIndex = 0;
        if(mData != null) {
            // 如果数据存在,则清空
            mData.clear();
        }
        // 加载评测列表
        mTestPresenter.loadTest(mType, pageIndex);
    }
}
