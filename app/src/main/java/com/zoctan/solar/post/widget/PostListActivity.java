package com.zoctan.solar.post.widget;

import com.zoctan.solar.R;
import com.zoctan.solar.api.PostUrls;
import com.zoctan.solar.beans.GroupBean;
import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.post.PostAdapter;
import com.zoctan.solar.post.presenter.PostPresenter;
import com.zoctan.solar.post.view.PostView;
import com.zoctan.solar.utils.ImageLoaderUtils;
import com.zoctan.solar.utils.SPUtils;
import com.zoctan.solar.utils.SwipeBackActivity;
import com.zoctan.solar.utils.ToastUtils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

/**
 * Post列表实现类
 */

public class PostListActivity extends SwipeBackActivity implements PostView,SwipeRefreshLayout.OnRefreshListener,android.support.v4.app.FragmentManager.OnBackStackChangedListener {

    private PostAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private List<PostBean> mData;
    private GroupBean mGroup;
    private int pageIndex = 0;
    private Toolbar mToolbar;
    private PostPresenter mPostPresenter;
    private PostListActivity PostListActivitySelf = this;
    private int mGroupId;
    private SPUtils mSPUtils;
    private FabSpeedDial mFabSpeedDial;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 如果为日间模式
        mSPUtils = new SPUtils(this);
        if (Objects.equals(mSPUtils.getString("toggle"), "day")) {
            // 日间
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            // 夜间
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        setContentView(R.layout.activity_postlist);

        // 初始化控件
        initView();
        mPostPresenter = new PostPresenter(this);

        // 刷新
        onRefresh();
    }

    // 初始化控件
    private void initView() {

        mGroup = (GroupBean)getIntent().getSerializableExtra("group");
        mGroupId = Integer.parseInt(mGroup.getId());

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
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

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

        // setup the picture for cover page
        initImageView();
        // 浮动按钮
        mFabSpeedDial = (FabSpeedDial) this.findViewById(R.id.fab_speed_dial);
        // 如果登录了就显示可发帖
        boolean mIsLogin = mSPUtils.getBoolean("Login");
        if(mIsLogin) {
            mFabSpeedDial.setVisibility(View.VISIBLE);
        } else {
            mFabSpeedDial.setVisibility(View.GONE);
        }
        mFabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                getMenuInflater().inflate(R.menu.menu_group,navigationMenu);
                // 对菜单项目初始化
                // 如果不初始化就返回false
                return true;
            }
        });

        mFabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    // 发贴
                    case R.id.action_post:
                        makePost();
                        break;
                    // 如果没有小组，则显示加入小组
                    case R.id.action_add_group:
                        addGroup();
                        break;
                }
                return false;
            }});
    }

    public void makePost(){
        (findViewById(R.id.frame_content)).setVisibility(View.VISIBLE);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_content,new PostAddFragment(), "postFrame")
                // 显示fragment动画
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("add")
                .commit();
    }

    // 加入小组
    public void addGroup(){
        String user_id = mSPUtils.getString("userID");
        mPostPresenter.addGroup(mSPUtils.getString("group_id"), user_id);
    }

    @Override
    public void onBackStackChanged() {
        if(getSupportFragmentManager().getBackStackEntryCount()==0){
            mFabSpeedDial.setVisibility(View.VISIBLE);
            (findViewById(R.id.frame_content)).setVisibility(View.GONE);
        }
    }
    private void initImageView(){
        ImageView imageView = (ImageView) (findViewById(R.id.ivImage));
        ImageLoaderUtils.display(this, imageView,mGroup.getImgsrc());
    }

    private PostAdapter.OnItemClickListener mOnItemClickListener = new PostAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mData.size() <= 0) {
                return;
            }
            PostBean post = mAdapter.getItem(position);
            Intent intent = new Intent(PostListActivitySelf, PostDetailActivity.class);
            intent.putExtra("post", post);
            startActivity(intent);
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
        mPostPresenter.loadPost(mGroupId,pageIndex);
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

    @Override
    public void showAddGroupSuccess(){
        ToastUtils.showLong(this, "成功加入小组");
        mSPUtils.putString("userGroup", mSPUtils.getString("group_id"));
    }
}
