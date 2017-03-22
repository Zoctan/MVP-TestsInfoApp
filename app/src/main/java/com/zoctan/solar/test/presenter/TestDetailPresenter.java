package com.zoctan.solar.test.presenter;

import android.content.Context;

import com.zoctan.solar.beans.TestDetailBean;
import com.zoctan.solar.test.model.TestModel;
import com.zoctan.solar.test.model.TestModelImpl;
import com.zoctan.solar.test.view.TestDetailView;

/**
 * 评测详情回调实现类
 */
public class TestDetailPresenter {

    private Context mContent;
    private TestDetailView mTestDetailView;
    private TestModel mTestModel;

    public TestDetailPresenter(Context content, TestDetailView testDetailView) {
        this.mContent = content;
        this.mTestDetailView = testDetailView;
        mTestModel = new TestModelImpl();
    }

    public void loadTestDetail(final String id) {
        // 显示Loading圈圈
        mTestDetailView.showLoading();
        mTestModel.loadTestDetail(id, new OnLoadTestDetailListener());
    }

    private class OnLoadTestDetailListener implements TestModel.OnLoadTestDetailListener {

        @Override
        public void onSuccess(TestDetailBean testDetailBean) {
            // 如果TestDetail实体不为空
            if (testDetailBean != null) {
                // 调用实体方法getContent显示文章内容
                mTestDetailView.showTestDetailContent(testDetailBean.getContent());
            }
            mTestDetailView.hideLoading();
        }

        @Override
        public void onFailure(String msg, Exception e) {
            // 隐藏Loading圈圈
            mTestDetailView.hideLoading();
        }
    }
}
