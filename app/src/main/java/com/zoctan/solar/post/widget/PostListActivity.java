package com.zoctan.solar.post.widget;

import com.zoctan.solar.R;
import com.zoctan.solar.api.PostUrls;
import com.zoctan.solar.beans.GroupBean;
import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.post.PostAdapter;
import com.zoctan.solar.post.presenter.PostPresenter;
import com.zoctan.solar.post.view.PostView;
import com.zoctan.solar.utils.LogUtils;
import com.zoctan.solar.utils.SPUtils;
import com.zoctan.solar.utils.SwipeBackActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.view.View;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Post列表实现类
 */

public class PostListActivity extends SwipeBackActivity implements PostView,SwipeRefreshLayout.OnRefreshListener {
    // 默认根据时间调节日夜间模式
    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    private PostAdapter mAdapter;
    private static final String TAG = "PostListFragment";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private List<PostBean> mData;
    private GroupBean mGroup;
    private int pageIndex = 0;
    private Toolbar mToolbar;
    private PostPresenter mPostPresenter;
    private PostListActivity PostListActivitySelf = this;
    private int mType;
    private SPUtils mSPUtils;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        // 初始化控件
        initView();

        mPostPresenter = new PostPresenter(this);

        // 刷新
        onRefresh();
    }

    // 初始化控件
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initView() {
        // 如果为日间模式
        mSPUtils = new SPUtils(this);
        if (Objects.equals(mSPUtils.getString("toggle"), "day")) {
            // 日间
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            // 夜间
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        mGroup = (GroupBean)getIntent().getSerializableExtra("group");
        mType = Integer.parseInt(mGroup.getId());

        mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // 带图片伸缩工具栏
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // 设置工具栏标题
        collapsingToolbar.setTitle(mGroup.getTitle());
        // 设置扩张时的标题颜色
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        // 设置收缩时的标题颜色
        collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);

        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget_post_list);
        // 设置刷新时动画的颜色，可以设置4个
        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary, R.color.divider,
                R.color.primary_light, R.color.accent);
        // 下拉刷新监听
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_postList);
        // 固定RecyclerView大小
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PostAdapter(this);
        // item点击监听
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        // 设置适配器
        mRecyclerView.setAdapter(mAdapter);

        // 滚动监听 this is no need for now.
        //mRecyclerView.addOnScrollListener(mOnScrollListener);
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
                mPostPresenter.loadPost(mType, pageIndex);
            }
        }
    };


    private PostAdapter.OnItemClickListener mOnItemClickListener = new PostAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mData.size() <= 0) {
                return;
            }
            PostBean post = mAdapter.getItem(position);
            Intent intent = new Intent(PostListActivitySelf, PostDetailActivity.class);
            intent.putExtra("post", post);
            View transitionView = findViewById(R.id.ivImage);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.
                            makeSceneTransitionAnimation(
                                    PostListActivitySelf,
                                    transitionView,
                                    getString(R.string.transition_test_img)
                            );

            ActivityCompat.startActivity(PostListActivitySelf, intent, options.toBundle());
        }
    };


    @Override
    public void addPost(List<PostBean>postList){
        mAdapter.isShowFooter(true);
        if(mData==null){
            mData=new ArrayList<>();
        }
        mData.addAll(postList);
        if(pageIndex == 0) {
            mAdapter.setmDate(mData);
        } else {
            // 如果没有更多数据,则隐藏footer布局
            if(postList.size() == 0) {
                mAdapter.isShowFooter(false);
            }
            // 处理的数据发生变化,通知View作出改变
            mAdapter.notifyDataSetChanged();
        }
        // 改变API网址start参数
        pageIndex += PostUrls.PAGE_SIZE;
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void showLoading() {
        mAdapter.isShowFooter(false);
        mSwipeRefreshWidget.setRefreshing(true);
    }
    @Override
    public void hideLoading(){
        mSwipeRefreshWidget.setRefreshing((false));
    }
    @Override
    public void onRefresh(){
        pageIndex=0;
        if(mData!=null){
            mData.clear();
        }
        mPostPresenter.loadPost(mType,pageIndex);
    }
    @Override
    public void showLoadFailMsg(){
        if(pageIndex == 0) {
            mAdapter.isShowFooter(false);
            // 处理的数据发生变化，通知View作出改变
            mAdapter.notifyDataSetChanged();
        }
        View view = this == null ? mRecyclerView.getRootView() : this.findViewById(R.id.drawer_layout);
        Snackbar.make(view, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
    }
}
