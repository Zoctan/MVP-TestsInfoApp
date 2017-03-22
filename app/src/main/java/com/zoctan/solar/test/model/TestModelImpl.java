package com.zoctan.solar.test.model;

import com.zoctan.solar.beans.TestBean;
import com.zoctan.solar.beans.TestDetailBean;
import com.zoctan.solar.api.TestUrls;
import com.zoctan.solar.test.TestJsonUtils;
import com.zoctan.solar.test.widget.TestFragment;
import com.zoctan.solar.utils.OkHttpUtils;

import java.util.List;

/**
 * 评测信息接口实现
 */
public class TestModelImpl implements TestModel {

    // 由API地址加载评测列表
    @Override
    public void loadTest(String url, final int type, final OnLoadTestListListener listener) {
        OkHttpUtils.ResultCallback<String> loadTestCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                List<TestBean> testBeanList = TestJsonUtils.readJsonTestBeans(response, getID(type));
                listener.onSuccess(testBeanList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("加载评测列表失败", e);
            }
        };
        // 调用OkHttp的get方法
        OkHttpUtils.get(url, loadTestCallback);
    }

    // 由文章ID加载评测详情
    @Override
    public void loadTestDetail(final String id, final OnLoadTestDetailListener listener) {
        String url = getDetailUrl(id);
        OkHttpUtils.ResultCallback<String> loadTestCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                TestDetailBean testDetailBean = TestJsonUtils.readJsonTestDetailBeans(response, id);
                listener.onSuccess(testDetailBean);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("测评详情加载失败", e);
            }
        };
        // 调用OkHttp的get方法
        OkHttpUtils.get(url, loadTestCallback);
    }

    // 获取分类ID
    private String getID(int type) {
        String id;
        switch (type) {
            case TestFragment.TEST_TYPE_PHONE:
                id = TestUrls.PHONE_ID;
                break;
            case TestFragment.TEST_TYPE_LAPTOP:
                id = TestUrls.LAPTOP_ID;
                break;
            case TestFragment.TEST_TYPE_EARPHONE:
                id = TestUrls.EARPHONE_ID;
                break;
            default:
                id = TestUrls.PHONE_ID;
                break;
        }
        return id;
    }

    // 由评测ID获取评测详情URL
    private String getDetailUrl(String id) {
        return TestUrls.TEST_DETAIL + "?id=" + id;
    }
}
