package com.zoctan.solar.test.view;

/**
 * 评测详情视图接口
 */
public interface TestDetailView {
    // 显示评测内容
    void showTestDetailContent(String testDetailContent);
    // 加载数据的过程中需要提示“正在加载”的反馈信息给用户
    void showLoading();
    // 加载成功后，需要将“正在加载”反馈信息取消掉
    void hideLoading();
}
