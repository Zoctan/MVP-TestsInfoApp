package com.zoctan.solar.user.widget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zoctan.solar.R;
import com.zoctan.solar.beans.UserBean;
import com.zoctan.solar.user.presenter.UserLoginPresenter;
import com.zoctan.solar.user.view.UserLoginView;
import com.zoctan.solar.utils.ActivityCollector;
import com.zoctan.solar.utils.CodeUtils;
import com.zoctan.solar.utils.SPUtils;
import com.zoctan.solar.utils.SwipeBackActivity;
import com.zoctan.solar.utils.ToastUtils;

import java.util.Objects;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class UserLoginActivity extends SwipeBackActivity implements UserLoginView, View.OnClickListener {

    private EditText mEtName, mEtPassword, mEtPassword2, mEtProve;
    private Button mBtnLogin, mBtnRegister, mBtnBack, mBtnEye;
    private ProgressBar mPbLoading;
    private UserLoginPresenter mUserLoginPresenter;
    private SPUtils mSPUtils;
    private CodeUtils mCodeUtils;
    private Toolbar mToolbar;
    private FrameLayout mRegisterLayout;
    private ImageView mImgProve;

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
        setContentView(R.layout.activity_login);

        // 初始化控件
        initView();

        mUserLoginPresenter = new UserLoginPresenter(this);

        // 将该Activity添加到ActivityCollector管理器中
        ActivityCollector.addActivity(this);
    }

    // 初始化控件
    private void initView() {

        // 初始化toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.user_login);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // 初始化输入框和层
        mEtName = (EditText) findViewById(R.id.login_name);
        mEtPassword = (EditText) findViewById(R.id.login_password);
        mRegisterLayout = (FrameLayout) findViewById(R.id.register_layout);
        mRegisterLayout.setVisibility(View.GONE);
        mEtPassword2 = (EditText) findViewById(R.id.register_password);
        mEtProve = (EditText) findViewById(R.id.et_prove);
        mPbLoading = (ProgressBar) findViewById(R.id.progressBar);

        // 获得SwipeBackLayout对象
        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
        // 设定可从上下左右滑动退出
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);

        // 初始化验证码图片
        mImgProve = (ImageView) findViewById(R.id.prove_img);
        refresh(mImgProve);

        // 初始化按钮
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnEye = (Button) findViewById(R.id.bt_pwd_eye);
        // 监听界面上的按钮
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
        mBtnEye.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_login:
                clickButton();
                break;
            case R.id.btn_register:
                switchClick();
                break;
            case R.id.bt_pwd_eye:
                showPwd();
                break;
            default:
                break;
        }
    }

    // 显示密码
    private void showPwd() {
        mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        mEtPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        // 设置光标位置到行尾
        mEtPassword.setSelection(mEtPassword.getText().length());
        mEtPassword2.setSelection(mEtPassword2.getText().length());
    }

    // 通过交换按钮的text来交换位置
    private void switchClick() {
        if(mBtnLogin.getText() == "注册") {
            initView();
            mBtnLogin.setText("登录");
            mBtnRegister.setText("注册");
        }else {
            mBtnLogin.setText("注册");
            mBtnRegister.setText("登录");
            mRegisterLayout.setVisibility(View.VISIBLE);
            mToolbar.setTitle(R.string.user_register);
        }
    }

    // 登录或注册点击
    private void clickButton() {
        String name = mEtName.getText().toString();
        String password = mEtPassword.getText().toString();
        String password2 = mEtPassword2.getText().toString();
        String codeStr = mEtProve.getText().toString().trim();
        if(mBtnLogin.getText() == "注册") {
            if(checkInput(name, password, password2) && checkProve(codeStr)) {
                mUserLoginPresenter.userAction("register", name, password);
            }
        }else {
            if(checkInput(name, password) && checkProve(codeStr)) {
                mUserLoginPresenter.userAction("login", name, password);
            }
        }
    }

    public void refresh(View view) {
        mCodeUtils = CodeUtils.getInstance();
        Bitmap bitmap = mCodeUtils.createBitmap();
        mImgProve.setImageBitmap(bitmap);
    }

    // 检查验证码
    public boolean checkProve(String codeStr) {
        if (codeStr == null || TextUtils.isEmpty(codeStr)) {
            ToastUtils.showShort(this, "请输入验证码");
            return false;
        }
        String code = mCodeUtils.getCode();
        if (code.equalsIgnoreCase(codeStr)) {
            return true;
        } else {
            ToastUtils.showShort(this, "验证码错误");
            return false;
        }
    }

    // 检查输入
    public boolean checkInput(String name, String password) {
        // 用户名为空时提示
        if (name == null || TextUtils.isEmpty(name)) {
            ToastUtils.showShort(this, "用户名不能为空");
        }else {
            // 密码为空时提示
            if (password == null || TextUtils.isEmpty(password)) {
                ToastUtils.showShort(this, "密码不能为空");
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean checkInput(String name, String password, String password2) {
        if(checkInput(name, password)) {
            if (!Objects.equals(password, password2)) {
                ToastUtils.showShort(this, "两次密码不一致");
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void showLoading() {
        mPbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showSuccessMsg(UserBean userBean) {
        if(mBtnLogin.getText() == "注册") {
            ToastUtils.showShort(this, "注册成功");
        }else {
            ToastUtils.showShort(this, "登录成功");
        }
        mSPUtils = new SPUtils(this);
        // 将login置为true
        mSPUtils.putBoolean("Login", true);
        // 将用户信息存在本地
        mSPUtils.putString("userID", userBean.getId());
        mSPUtils.putString("userImg", userBean.getImgsrc());
        mSPUtils.putString("userName", userBean.getName());
        mSPUtils.putString("userPassword", userBean.getPassword());
        mSPUtils.putString("userGroup", userBean.getGroup_id());
        // 更新主界面侧滑栏的用户UI
        Intent intent = new Intent("userLogin");
        Bundle user = new Bundle();
        user.putString("name", userBean.getName());
        user.putString("image", userBean.getImgsrc());
        intent.putExtras(user);
        sendBroadcast(intent);
        finish();
    }

    @Override
    public void showFailMsg() {
        if(mBtnLogin.getText() == "注册") {
            ToastUtils.showLong(this, "注册失败, 用户名已存在");
        }else {
            ToastUtils.showShort(this, "登录失败");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 从管理器中移除该Activity
        ActivityCollector.removeActivity(this);
    }
}
