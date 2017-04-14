package com.zoctan.solar.main.presenter;

import com.zoctan.solar.main.view.MainView;
import com.zoctan.solar.R;

/**
 * 主界面侧滑栏回调类
 */
public class MainPresenter {

    private MainView mMainView;

    public MainPresenter(MainView mainView) {
        this.mMainView = mainView;
    }

    // 侧滑栏选择
    public void switchNavigation(int id) {
        switch (id) {
            // 众评
            case R.id.navigation_item_test:
                mMainView.switch2Test();
                break;
            // 小组
            case R.id.navigation_item_group:
                mMainView.switch2Group();
                break;
            // 关于
            case R.id.navigation_item_about:
                mMainView.switch2About();
                break;
            // 清理缓存
            case R.id.navigation_item_clean:
                mMainView.switch2Clean();
                break;
            // 默认众评
            default:
                mMainView.switch2Test();
                break;
        }
    }
}
