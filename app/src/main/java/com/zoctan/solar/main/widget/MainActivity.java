package com.zoctan.solar.main.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.zoctan.solar.R;
import com.zoctan.solar.about.AboutFragment;
import com.zoctan.solar.group.widget.GroupFragment;
import com.zoctan.solar.main.presenter.MainPresenter;
import com.zoctan.solar.main.view.MainView;
import com.zoctan.solar.test.widget.TestFragment;
import com.zoctan.solar.utils.ActivityCollector;
import com.zoctan.solar.utils.ImageLoaderUtils;
import com.zoctan.solar.utils.SPUtils;
import com.zoctan.solar.utils.ToastUtils;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 主视图可视化界面
 */
public class MainActivity extends AppCompatActivity implements MainView,View.OnClickListener {

    private String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private MainPresenter mMainPresenter;
    private boolean mIsLogin;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mHeaderView;
    private NavigationView mNavigationView;
    private TextView mHeaderText;
    private CircleImageView mHeaderImage;
    private SPUtils mSPUtils;
    private String userImg, userName;
    private IntentFilter mIntentFilter;
    private Switch mSwitch;
    // 保存点击的时间
    private long exitTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置主界面要显示的视图
        setContentView(R.layout.activity_main);

        // 初始化控件
        initView();

        // 主界面业务处理实体化
        mMainPresenter = new MainPresenter(this);

        // 显示Test列表
        switch2Test();

        // 将该Activity添加到ActivityCollector管理器中
        ActivityCollector.addActivity(this);
    }

    // 初始化控件
    private void initView() {
        // 设置Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // 设置DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // 创建侧滑键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        // 自动和actionBar关联, 将开关的图片显示在了action上
        // 如果不设置，也可以有抽屉的效果，不过是默认的图标
        mDrawerToggle.syncState();
        // 给抽屉Layout绑定切换器监听
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // 设置NavigationView点击事件
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(mNavigationView);

        // 获取头布局文件
        mHeaderView = mNavigationView.getHeaderView(0);
        mSwitch = (Switch) mHeaderView.findViewById(R.id.switch_day_night);
        mHeaderText = (TextView) mHeaderView.findViewById(R.id.header_text);
        mHeaderText.setOnClickListener(this);
        mHeaderImage = (CircleImageView) mHeaderView.findViewById(R.id.header_image);
        mHeaderImage.setOnClickListener(this);

        // 判断是否登陆过
        mSPUtils = new SPUtils(this);
        mIsLogin = mSPUtils.getBoolean("Login");
        if(mIsLogin) {
            //ToastUtils.showShort(this, "已登录");
            userImg = mSPUtils.getString("userImg");
            userName = mSPUtils.getString("userName");
            // 显示头像
            ImageLoaderUtils.displayUserImg(this, mHeaderImage, userImg);
            // 显示用户名
            mHeaderText.setText(userName);
        }else {
            mHeaderText.setText("未登录");
        }

        // 如果为日间模式
        if (Objects.equals(mSPUtils.getString("toggle"), "day")) {
            // 将toggle置为day
            mSPUtils.putString("toggle", "day");
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            mSwitch.setChecked(false);
        } else {
            mSPUtils.putString("toggle", "night");
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            mSwitch.setChecked(true);
        }

        // 接收来自用户登录/退出/修改头像广播
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("userLogin");
        mIntentFilter.addAction("userLogout");
        mIntentFilter.addAction("modifyImg");
        // 动态注册广播
        registerReceiver(mBroadcastReceiver, mIntentFilter);
    }

    // 广播重写
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Objects.equals(intent.getAction(), "userLogin")
                    || Objects.equals(intent.getAction(), "userLogout")
                    || Objects.equals(intent.getAction(), "modifyImg")) {
                // 只更新侧滑栏用户
                //Bundle user = intent.getExtras();
                // 更新用户名和头像
                //mHeaderText.setText(user.getString("name"));
                //ImageLoaderUtils.displayUserImg(MainActivity.this, mHeaderImage, user.getString("image"));

                // 重新初始化界面
                initView();
            }
        }
    };

    // 初始化菜单，其中menu参数就是即将要显示的Menu实例。
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // 返回true则显示该menu,false 则不显示
        return true;
    }

    // 菜单选项点击的事件处理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    // 设置NavigationView点击事件
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mMainPresenter.switchNavigation(menuItem.getItemId());
                menuItem.setChecked(true);
                // 关闭侧滑栏
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    // 侧滑栏两个选项
    @Override
    public void switch2Test() {
        // 测评栏
        replaceFragment(new TestFragment(),"testFrame");
        mToolbar.setTitle(R.string.navigation_test);
    }

    @Override
    public void switch2Group() {
        // 小组栏
        replaceFragment(new GroupFragment(),"groupFrame");
        mToolbar.setTitle(R.string.navigation_group);
    }

    @Override
    public void switch2About() {
        // 关于栏
        replaceFragment(new AboutFragment(),"aboutFrame");
        mToolbar.setTitle(R.string.navigation_about);
    }
    
    private void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_content,fragment, tag)
                // 显示fragment动画
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("replace")
                .commit();
    }

    // 侧滑栏用户
    public void onClick(View view) {
        Log.d(TAG,"go in userClick method");
        Intent intent = new Intent();
        if(mIsLogin) {
            Bundle user = new Bundle();
            user.putString("name", userName);
            user.putString("image", userImg);
            intent.putExtras(user);
            intent.setAction("userDetail");
            intent.addCategory("user");
        }else {
            intent.setAction("toLogin");
            intent.addCategory("user");
        }
        Log.d(TAG,"start userClick to another activity");
        startActivity(intent);
    }

    // 切换日夜间模式
    public void toggleClick(View view) {
        // 如果为日间模式
        if (Objects.equals(mSPUtils.getString("toggle"), "day")) {
            // 将toggle置为night
            mSPUtils.putString("toggle", "night");
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // 将toggle置为day
            mSPUtils.putString("toggle", "day");
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        this.recreate();
    }

    // 双击退出
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 当按下返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 如果按下的时间超过2秒
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showShort(this, "再按一次退出程序");
                // 获取当前系统时间的毫秒数
                exitTime = System.currentTimeMillis();
            } else {
                // 关闭所有Activity
                ActivityCollector.finishAll();
                // 正常退出程序
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 从管理器中移除该Activity
        ActivityCollector.removeActivity(this);
        // 注销广播
        unregisterReceiver(mBroadcastReceiver);
    }
}
