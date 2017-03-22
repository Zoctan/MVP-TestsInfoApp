package com.zoctan.solar.test.widget;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zoctan.solar.R;
import com.zoctan.solar.beans.TestBean;
import com.zoctan.solar.test.presenter.TestDetailPresenter;
import com.zoctan.solar.test.view.TestDetailView;
import com.zoctan.solar.utils.ActivityCollector;
import com.zoctan.solar.utils.ImageLoaderUtils;
import com.zoctan.solar.utils.SPUtils;
import com.zoctan.solar.utils.SwipeBackActivity;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.Objects;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * 评测详情可视化界面
 */
public class TestDetailActivity extends SwipeBackActivity implements TestDetailView {

    // 默认根据时间调节日夜间模式
    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    private TestBean mTest;
    private HtmlTextView mTVTestContent;
    private TestDetailPresenter mTestDetailPresenter;
    private ProgressBar mPbLoading;
    private SwipeBackLayout mSwipeBackLayout;
    private Toolbar mToolbar;
    private SPUtils mSPUtils;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置评测详情要显示的视图
        setContentView(R.layout.activity_test_detail);

        // 初始化控件
        initView();

        mTestDetailPresenter = new TestDetailPresenter(getApplication(), this);
        mTestDetailPresenter.loadTestDetail(mTest.getId());

        // 将该Activity添加到ActivityCollector管理器中
        ActivityCollector.addActivity(this);
    }

    // 初始化控件
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initView() {
        // 找到Loading图标位置
        mPbLoading = (ProgressBar) findViewById(R.id.progress);
        // 找到评测内容位置
        mTVTestContent = (HtmlTextView) findViewById(R.id.htTestContent);

        mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        // 从TestFragment获得test对象实例
        mTest = (TestBean) getIntent().getSerializableExtra("test");

        // 如果为日间模式
        mSPUtils = new SPUtils(this);
        if (Objects.equals(mSPUtils.getString("toggle"), "day")) {
            // 日间
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            // 将mSwitch置为false, 夜间
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        // 带图片伸缩工具栏
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // 设置工具栏标题
        collapsingToolbar.setTitle(mTest.getTitle());
        // 设置扩张时的标题颜色
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        // 设置收缩时的标题颜色
        collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);

        // 调用Glide加载封面图片
        ImageLoaderUtils.display(getApplicationContext(), (ImageView) findViewById(R.id.ivImage), mTest.getImgsrc());
    }

    @Override
    public void showTestDetailContent(String testDetailContent) {
        // 使用HtmlTextView来显示评测文章详情, 并且调用HtmlHttpImageGetter输出文章中图片
        mTVTestContent.setHtml(testDetailContent, new HtmlHttpImageGetter(mTVTestContent));
    }

    @Override
    public void showLoading() {
        // Loading圈圈设置成可见
        mPbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        // 移除Loading圈圈
        mPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 从管理器中移除该Activity
        ActivityCollector.removeActivity(this);
    }
}
