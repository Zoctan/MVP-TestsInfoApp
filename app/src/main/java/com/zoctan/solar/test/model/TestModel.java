package com.zoctan.solar.test.model;

import com.zoctan.solar.beans.TestBean;
import com.zoctan.solar.beans.TestDetailBean;

import java.util.List;

/**
 * 评测信息接口
 */
public interface TestModel {

    // 载入评测列表接口
    void loadTest(String url, int type, OnLoadTestListListener listener);
    // 监听列表
    interface OnLoadTestListListener {
        void onSuccess(List<TestBean> list);
        void onFailure(String msg, Exception e);
    }

    // 载入详情接口
    void loadTestDetail(String id, OnLoadTestDetailListener listener);
    // 监听详情
    interface OnLoadTestDetailListener {
        void onSuccess(TestDetailBean testDetailBean);
        void onFailure(String msg, Exception e);
    }
}
