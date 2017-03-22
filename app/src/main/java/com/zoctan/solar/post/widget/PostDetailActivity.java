package com.zoctan.solar.post.widget;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoctan.solar.R;
import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.beans.PostCommentBean;
import com.zoctan.solar.post.PostCommentAdapter;
import com.zoctan.solar.post.view.PostDetailView;
import com.zoctan.solar.post.presenter.PostDetailPresenter;
import com.zoctan.solar.utils.ActivityCollector;
import com.zoctan.solar.utils.LogUtils;
import com.zoctan.solar.utils.SPUtils;
import com.zoctan.solar.utils.SwipeBackActivity;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * Created by root on 3/6/17.
 */

public class PostDetailActivity extends SwipeBackActivity implements PostDetailView{
    // 默认根据时间调节日夜间模式
    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }
    private PostBean mPost;
    private HtmlTextView mTVPostContent;
    private PostDetailPresenter mPostDetailPresenter;
    private ProgressBar mPbLoading;
    private SwipeBackLayout mSwipeBackLayout;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PostCommentAdapter mAdapter;
    private List<PostCommentBean> mData;
    private SPUtils mSPUtils;
    private RelativeLayout mCommentLayout;
    private TextView mTVPostUser;
    private TextView mTVPostTime;
    private boolean mIsLogin;

    private String TAG = "PostDetailActivity";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState){
        LogUtils.d(TAG,"PostDetail onCreate");
        super.onCreate(savedInstanceState);
        // 设置Post详情要显示的视图
        setContentView(R.layout.activity_post_detail);

        // 初始化控件
        initView();

        mPostDetailPresenter = new PostDetailPresenter(getApplication(), this);
        mPostDetailPresenter.loadPostDetail(mPost.getId());

        // 将该Activity添加到ActivityCollector管理器中
        ActivityCollector.addActivity(this);


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

        // 找到Loading图标位置
        mPbLoading = (ProgressBar) findViewById(R.id.progress_postDetail);
        // 找到post内容位置
        mTVPostContent = (HtmlTextView) findViewById(R.id.htPostContent);
        mTVPostUser = (TextView) findViewById(R.id.tvUser_postDetail) ;
        mTVPostTime = (TextView) findViewById(R.id.tvTime_postDetail) ;
        mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        // 从PostFragment获得post对象实例
        mPost = (PostBean) getIntent().getSerializableExtra("post");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(mPost.getTitle());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // 获得SwipeBackLayout对象
        mSwipeBackLayout = getSwipeBackLayout();
        // 滑动删除的效果只能从边界滑动才有效果，如果要扩大touch的范围，可以调用
        //mSwipeBackLayout.setEdgeSize(this.getResources().getDisplayMetrics().widthPixels);
        //mSwipeBackLayout.setEdgeSize(int size);
        // 设定从左边可以滑动,EDGE_ALL表示向下、左、右滑动均可EDGE_LEFT，EDGE_RIGHT，EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        // setup RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.RV_comment);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new PostCommentAdapter();
        mRecyclerView.setAdapter(mAdapter);


        mTVPostUser.setText(mPost.getUser());
        mTVPostTime.setText(mPost.getPost_time());

        mCommentLayout = (RelativeLayout) findViewById(R.id.add_comment_layout);
        // 如果登录了就显示可发表评论
        mIsLogin = mSPUtils.getBoolean("Login");
        if(mIsLogin) {
            mCommentLayout.setVisibility(View.VISIBLE);
        } else {
            mCommentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPostDetailContent(String postDetailContent) {
        LogUtils.d(TAG,"output post detail");
        // 使用HtmlTextView来显示Post文章详情, 并且调用HtmlHttpImageGetter输出文章中图片
        mTVPostContent.setHtml(postDetailContent, new HtmlHttpImageGetter(mTVPostContent));
    }

    @Override
    public void showLoading() {
        LogUtils.d(TAG,"show loading");
        // Loading圈圈设置成可见
        mPbLoading.setVisibility(View.VISIBLE);
    }


    public void addComment(List<PostCommentBean> postCommentBeen){
        if(mData==null){
            mData = new ArrayList<>();
        }
        mData.addAll(postCommentBeen);
        mAdapter.setmData(mData);
    }

    @Override
    public void hideLoading() {
        LogUtils.d(TAG,"hide loading");
        // 移除Loading圈圈
        mPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG,"postDetail onDestroy");
        super.onDestroy();
        // 从管理器中移除该Activity
        ActivityCollector.removeActivity(this);
    }
}
