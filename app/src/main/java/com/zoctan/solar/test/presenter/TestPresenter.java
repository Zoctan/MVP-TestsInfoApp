package com.zoctan.solar.test.presenter;

import com.zoctan.solar.beans.TestBean;
import com.zoctan.solar.api.TestUrls;
import com.zoctan.solar.test.model.TestModel;
import com.zoctan.solar.test.model.TestModelImpl;
import com.zoctan.solar.test.view.TestView;
import com.zoctan.solar.test.widget.TestFragment;
import com.zoctan.solar.utils.LogUtils;

import java.util.List;

/**
 * 评测列表回调实现类
 */
public class TestPresenter {

    private static final String TAG = "TestPresenterImpl";

    private TestView mTestView;
    private TestModel mTestModel;

    public TestPresenter(TestView testView) {
        this.mTestView = testView;
        this.mTestModel = new TestModelImpl();
    }

    public void loadTest(final int type, final int pageIndex) {
        String url = getUrl(type, pageIndex);
        LogUtils.d(TAG, url);
        if(pageIndex == 0) {
            // 只有第一页的或者刷新的时候才显示刷新圈圈
            mTestView.showLoading();
        }
        mTestModel.loadTest(url, type, new OnLoadTestListListener());
    }

    // 根据类别和页面索引创建URL
    private String getUrl(int type, int pageIndex) {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case TestFragment.TEST_TYPE_PHONE:
                sb.append(TestUrls.TEST_LIST).append("?type=").append(TestUrls.PHONE_ID);
                break;
            case TestFragment.TEST_TYPE_LAPTOP:
                sb.append(TestUrls.TEST_LIST).append("?type=").append(TestUrls.LAPTOP_ID);
                break;
            case TestFragment.TEST_TYPE_EARPHONE:
                sb.append(TestUrls.TEST_LIST).append("?type=").append(TestUrls.EARPHONE_ID);
                break;
            default:
                sb.append(TestUrls.TEST_LIST).append("?type=").append(TestUrls.PHONE_ID);
                break;
        }
        sb.append("&start=").append(pageIndex).append("&size=").append(TestUrls.PAGE_SIZE);
        return sb.toString();
    }

    private class OnLoadTestListListener implements TestModel.OnLoadTestListListener {
        @Override
        public void onSuccess(List<TestBean> list) {
            mTestView.hideLoading();
            // 添加评测列表
            mTestView.addTest(list);
        }

        @Override
        public void onFailure(String msg, Exception e) {
            mTestView.hideLoading();
            // 显示加载失败信息
            mTestView.showLoadFailMsg();
        }
    }
}
