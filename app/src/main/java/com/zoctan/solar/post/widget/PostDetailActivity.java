package com.zoctan.solar.post.widget;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoctan.solar.R;
import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.beans.PostCommentBean;
import com.zoctan.solar.post.PostCommentAdapter;
import com.zoctan.solar.post.view.PostDetailView;
import com.zoctan.solar.post.presenter.PostDetailPresenter;
import com.zoctan.solar.utils.ActivityCollector;
import com.zoctan.solar.utils.ImageLoaderUtils;
import com.zoctan.solar.utils.SPUtils;
import com.zoctan.solar.utils.SwipeBackActivity;
import com.zoctan.solar.utils.ToastUtils;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class PostDetailActivity extends SwipeBackActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener,PostDetailView{

    private PostBean mPost;
    private HtmlTextView mTVPostContent;
    private PostDetailPresenter mPostDetailPresenter;
    private PostCommentAdapter mAdapter;
    private List<PostCommentBean> mData;
    private SPUtils mSPUtils;
    private SwipeRefreshLayout mSwipeRefreshWidget;

    @Override
    public void onCreate(Bundle savedInstanceState){
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
        // 设置Post详情要显示的视图
        setContentView(R.layout.activity_post_detail);

        // 初始化控件
        initView();
        // initialize ImageView
        initImageView();
        // initialize Swipe
        initSwipe();
        // initialize Button
        initButton();

        mPostDetailPresenter = new PostDetailPresenter(this);
        onRefresh();

        // 将该Activity添加到ActivityCollector管理器中
        ActivityCollector.addActivity(this);
    }

    // initialize Button
    void initButton(){
        Button submitCommentBtn = (Button) findViewById(R.id.btn_add);
        submitCommentBtn.setOnClickListener(this);
    }

    // initialize ImageView()
    void initImageView(){
        CircleImageView mCircleImageView = (CircleImageView) findViewById(R.id.user_image_postDetail);
        ImageLoaderUtils.displayUserImg(this, mCircleImageView,mPost.getUser_img());
    }
    void initSwipe(){
        // 下拉刷新组件SwipeRefreshLayout
        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.SwipeContainer);
        // 设置刷新时动画的颜色，可以设置4个
        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary, R.color.divider, R.color.primary_light, R.color.accent);
        // 下拉刷新监听
        mSwipeRefreshWidget.setOnRefreshListener(this);
    }

    // 初始化控件
    private void initView() {

        // 找到Loading图标位置
        //mPbLoading = (ProgressBar) findViewById(R.id.progress_postDetail);
        // 找到post内容位置
        mTVPostContent = (HtmlTextView) findViewById(R.id.htPostContent);
        TextView mTVPostUser = (TextView) findViewById(R.id.tvUser_postDetail);
        TextView mTVPostTime = (TextView) findViewById(R.id.tvTime_postDetail);
        Toolbar mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
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
        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        // setup RecyclerView
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.RV_comment);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new PostCommentAdapter(this.getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);


        mTVPostUser.setText(mPost.getUser());
        mTVPostTime.setText(mPost.getPost_time());

        RelativeLayout mCommentLayout = (RelativeLayout) findViewById(R.id.add_comment_layout);
        // 如果登录了就显示可发表评论
        boolean mIsLogin = mSPUtils.getBoolean("Login");
        if(mIsLogin) {
            mCommentLayout.setVisibility(View.VISIBLE);
        } else {
            mCommentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPostDetailContent(String postDetailContent) {
        // 使用HtmlTextView来显示Post文章详情, 并且调用HtmlHttpImageGetter输出文章中图片
        mTVPostContent.setHtml(postDetailContent, new HtmlHttpImageGetter(mTVPostContent));
    }

    @Override
    public void showLoading() {
        mSwipeRefreshWidget.setRefreshing(true);
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
        mSwipeRefreshWidget.setRefreshing(false);
    }

    public void onClick(View view){
        EditText comment = (EditText)findViewById(R.id.add_comment);
        String text = comment.getText().toString();
        if(text.equals("")){
            ToastUtils.showShort(this, "评论内容不能为空哦~");
            return;
        }
        String user_id = mSPUtils.getString("userID");
        mPostDetailPresenter.sendPostComment(mPost.getId()+"",text,user_id);
    }
    public void queryAction(){
        ToastUtils.showShort(this, "成功评论");
        EditText comment = (EditText)findViewById(R.id.add_comment);
        comment.setText("");
        onRefresh();
    }

    public void showFailMessage(){
        ToastUtils.showShort(this, "评论失败，请再试试？");
    }

    @Override
    public void onRefresh(){
        if(mData != null){
            mData.clear();
        }
        mPostDetailPresenter.loadPostDetail(mPost.getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 从管理器中移除该Activity
        ActivityCollector.removeActivity(this);
    }
}
