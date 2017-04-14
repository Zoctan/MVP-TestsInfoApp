package com.zoctan.solar.user.widget;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoctan.solar.R;
import com.zoctan.solar.api.PostUrls;
import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.beans.UserBean;
import com.zoctan.solar.post.PostAdapter;
import com.zoctan.solar.post.widget.PostAddFragment;
import com.zoctan.solar.post.widget.PostDetailActivity;
import com.zoctan.solar.user.presenter.UserDetailPresenter;
import com.zoctan.solar.user.view.UserDetailView;
import com.zoctan.solar.utils.ActivityCollector;
import com.zoctan.solar.utils.ImageLoaderUtils;
import com.zoctan.solar.utils.ImageUtils;
import com.zoctan.solar.utils.LogUtils;
import com.zoctan.solar.utils.SPUtils;
import com.zoctan.solar.utils.SwipeBackActivity;
import com.zoctan.solar.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class UserDetailActivity extends SwipeBackActivity implements UserDetailView, View.OnClickListener {

    private Button mBtnSure, mBtnEye;
    private EditText mEtOldPassword, mEtNewPassword;
    private RelativeLayout mPwdLayout;
    private ProgressBar mPbLoading;
    private UserDetailPresenter mUserDetailPresenter;
    private SPUtils mSPUtils;
    private RecyclerView mRecyclerView;
    private String mHasGroup;
    private LinearLayoutManager mLayoutManager;
    private PostAdapter mAdapter;
    private Toolbar mToolbar;
    private int pageIndex = 0;
    private List<PostBean> mData;
    private CircleImageView userImg;
    private static String userName, userPassword;
    private SwipeBackLayout mSwipeBackLayout;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private String mGroupId;
    private TextView tvGroup;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        // 设置登录要显示的视图
        setContentView(R.layout.activity_user);
        mUserDetailPresenter = new UserDetailPresenter(this);
        // 初始化控件
        initView();

        // 将该Activity添加到ActivityCollector管理器中
        ActivityCollector.addActivity(this);
    }

    // 初始化控件
    private void initView() {

        // 接收传来的数据
        Intent intent = getIntent();
        Bundle user = intent.getExtras();
        // 更新头像
        userImg = (CircleImageView) findViewById(R.id.header_image) ;
        ImageLoaderUtils.displayUserImg(this, userImg, user.getString("image"));

        mSPUtils = new SPUtils(this);
        userName = mSPUtils.getString("userName");
        userPassword = mSPUtils.getString("userPassword");

        mFrameLayout = (FrameLayout) findViewById(R.id.frame_group);
        tvGroup = (TextView) findViewById(R.id.tvGroup);
        // 判断是否加入了小组
        mHasGroup = mSPUtils.getString("userGroup");
        if(!Objects.equals(mHasGroup, "null")) {
            tvGroup.setText(R.string.group_new);
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
            mUserDetailPresenter.loadPost(mHasGroup);
        }else{
            tvGroup.setText(R.string.no_group);
        }

        // 初始化toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(user.getString("name"));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // 初始化密码输入框和层
        mEtOldPassword = (EditText) findViewById(R.id.old_password);
        mEtNewPassword = (EditText) findViewById(R.id.new_password);
        mPwdLayout = (RelativeLayout) findViewById(R.id.pwd_layout);
        mPbLoading = (ProgressBar) findViewById(R.id.progress);

        // 获得SwipeBackLayout对象
        mSwipeBackLayout = getSwipeBackLayout();
        // 设定可从左滑动退出
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        // 初始化按钮
        mBtnSure = (Button) findViewById(R.id.btn_sure);
        mBtnSure.setVisibility(View.INVISIBLE);
        mBtnEye = (Button) findViewById(R.id.bt_pwd_eye);
        // 监听界面上的按钮
        mBtnSure.setOnClickListener(this);
        mBtnEye.setOnClickListener(this);

        // 浮动按钮
        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                getMenuInflater().inflate(R.menu.menu_user,navigationMenu);
                // 对菜单项目初始化
                // 如果不初始化就返回false
                return true;
            }
        });

        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    // 修改头像
                    case R.id.action_img:
                        modifyImg();
                        break;
                    // 修改密码
                    case R.id.action_pwd:
                        modifyPwd();
                        break;
                    // 退出小组
                    case R.id.action_out_group:
                        outGroup();
                        break;
                    // 退出
                    case R.id.action_logout:
                        userLogout();
                        break;
                }
                return false;
            }});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                clickSure();
                break;
            case R.id.bt_pwd_eye:
                showPwd();
                break;
            default:
                break;
        }
    }

    // 退出小组
    public void outGroup(){
        String user_id = mSPUtils.getString("userID");
        mUserDetailPresenter.outGroup(user_id);
    }

    // 修改密码操作
    public void modifyPwd() {
        mFrameLayout.setVisibility(View.GONE);
        mPwdLayout.setVisibility(View.VISIBLE);
        mBtnSure.setVisibility(View.VISIBLE);
    }

    // 修改密码确认点击
    private void clickSure() {
        String oldPassword = mEtOldPassword.getText().toString();
        String newPassword = mEtNewPassword.getText().toString();
        // 确认原密码正确
        if(Objects.equals(oldPassword, userPassword)) {
            mUserDetailPresenter.modifyPwd("modifyPwd", userName, newPassword);
        }else {
            ToastUtils.showShort(this, "原密码不正确");
        }
        mFrameLayout.setVisibility(View.VISIBLE);
    }

    // 显示密码
    private void showPwd() {
        mEtOldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        mEtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        // 设置光标位置到行尾
        mEtOldPassword.setSelection(mEtOldPassword.getText().length());
        mEtNewPassword.setSelection(mEtNewPassword.getText().length());
    }

    // 显示修改头像的对话框
    public void modifyImg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    // 选择本地照片
                    case CHOOSE_PICTURE:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    // 拍照
                    case TAKE_PICTURE:
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }    }
        });
        builder.create().show();
    }

    // 修改头像对话框操作处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 如果返回码是可以用的
            switch (requestCode) {
                // 选择本地照片
                case CHOOSE_PICTURE:
                    // 开始对图片进行裁剪处理
                    startPhotoZoom(data.getData());
                    break;
                // 拍照
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri);
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        // 让刚才选择裁剪得到的图片显示在界面上
                        setImageToView(data);
                    }
                    break;
            }
        }
    }

    // 裁剪图片方法实现
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            LogUtils.i("用户详情", "图片路径不存在");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 打开图片类文件
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    // 上传裁剪图片
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            // 图片已被处理成圆形
            photo = ImageUtils.toRoundBitmap(photo, tempUri);
            // 上传图片
            mUserDetailPresenter.uploadPic("modifyImg", userName, photo);
        }
    }

    // 用户退出操作
    public void userLogout() {
        // 将login置为false
        mSPUtils.putBoolean("Login", false);
        // 更新主界面侧滑栏的用户UI
        Intent intent = new Intent("userLogout");
        sendBroadcast(intent);
        ToastUtils.showShort(this, "已退出");
        finish();
    }

    @Override
    public void showSuccessMsg(UserBean userBean) {
        // 更新头像链接和密码
        mSPUtils.putString("userImg", userBean.getImgsrc());
        mSPUtils.putString("userPassword", userBean.getPassword());
        // 更新头像显示
        ImageLoaderUtils.displayUserImg(this, userImg, userBean.getImgsrc());
        // 更新主界面侧滑栏的用户UI
        Intent intent = new Intent("modifyImg");
        sendBroadcast(intent);
        ToastUtils.showShort(this, "修改成功");
    }

    @Override
    public void addPost(List<PostBean>postList){
        mAdapter.isShowFooter(true);
        if(mData == null){
            mData = new ArrayList<>();
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

    private PostAdapter.OnItemClickListener mOnItemClickListener = new PostAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mData.size() <= 0) {
                return;
            }
            PostBean post = mAdapter.getItem(position);
            Intent intent = new Intent(getApplicationContext(), PostDetailActivity.class);
            intent.putExtra("post", post);
            startActivity(intent);
        }
    };

    @Override
    public void showFailMsg() {
        ToastUtils.showShort(this, "修改失败");
    }

    @Override
    public void showLoading() {
        mPbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mPbLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showOutGroupSuccess() {
        ToastUtils.showLong(this, "已退出小组！");
        mSPUtils.putString("userGroup", "null");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 从管理器中移除该Activity
        ActivityCollector.removeActivity(this);
    }
}
