package com.zoctan.solar.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.internal.$Gson$Types;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp网络连接封装工具类
 */
public class OkHttpUtils {
    // 获得类名
    private static final String TAG = OkHttpUtils.class.getSimpleName();

    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private OkHttpUtils() {
        // 构建OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                // 请求的超时时间
                .readTimeout(30, TimeUnit.SECONDS)
                // 设置连接的超时时间
                .connectTimeout(10, TimeUnit.SECONDS)
                // 设置响应的超时时间
                .writeTimeout(20, TimeUnit.SECONDS).build();
        // 获取主线程的handler
        mDelivery = new Handler(Looper.getMainLooper());
    }

    /**
     * 通过单例模式构造对象
     * @return OkHttpUtils
     */
    private synchronized static OkHttpUtils getmInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpUtils();
        }
        return mInstance;
    }

    /**
     * 构造GET请求
     * @param url  请求的url
     * @param callback  结果回调的方法
     */
    private void getRequest(String url, final ResultCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        deliveryResult(request, callback);
    }

    /**
     * 构造POST请求
     * @param url 请求的url
     * @param params 请求参数
     * @param callback 结果回调的方法
     */
    private void postRequest(String url, List<Param> params, final ResultCallback callback) {
        final Request request = buildPostRequest(url, params);
        deliveryResult(request, callback);
    }

    /**
     * 构造POST请求
     * @param url  请求url
     * @param params 请求的参数
     * @return 返回 Request
     */
    private Request buildPostRequest(String url, List<Param> params) {
        // 表单对象，包含以input开始的对象，以html表单为主
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * 构造上传图片请求, 带参数
     * @param url  接口地址
     * @param file 上传的文件
     * @param params 参数
     * @param callback 回调方法,在子线程,更新UI要post到主线程
     * @return
     */
    private void uploadRequest(String url, File file, List<Param> params, final ResultCallback callback) {
        final Request request = buildUploadRequest(url, file, params);
        deliveryResult(request, callback);
    }

    /**
     * 构造上传请求
     * @param url  请求url
     * @param file 上传的图片
     * @param params 请求的参数
     * @return 返回 Request
     */
    private Request buildUploadRequest(String url, File file, List<Param> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MediaType.parse("multipart/form-data"));
        builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        for (Param param : params) {
            builder.addFormDataPart(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * 处理请求结果的回调
     * @param request
     * @param callback
     */
    private void deliveryResult(Request request, final ResultCallback callback) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callback, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String str = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccessCallBack(callback, str);
                    } else {
                        Object object = JsonUtils.deserialize(str, callback.mType);
                        sendSuccessCallBack(callback, object);
                    }
                } catch (final Exception e) {
                    LogUtils.e(TAG, "Json转换失败", e);
                    sendFailCallback(callback, e);
                }
            }
        });
    }

    /**
     * 发送失败的回调
     * @param callback
     * @param e
     */
    private void sendFailCallback(final ResultCallback callback, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
    }

    /**
     * 发送成功的回调
     * @param callback
     * @param obj
     */
    private void sendSuccessCallBack(final ResultCallback callback, final Object obj) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(obj);
                }
            }
        });
    }


    /**********************对外接口************************/

    /**
     * 构造上传图片请求, 带参数
     * @param url  接口地址
     * @param file 上传的文件
     * @param params 参数
     * @param callback 回调方法,在子线程,更新UI要post到主线程
     */
    public static void uploadFile(String url, File file,List<Param> params, final ResultCallback callback) {
        getmInstance().uploadRequest(url, file, params, callback);
    }

    /**
     * GET请求
     * @param url  请求URL
     * @param callback  请求回调
     */
    public static void get(String url, ResultCallback callback) {
        getmInstance().getRequest(url, callback);
    }

    /**
     * POST请求
     * @param url       请求URL
     * @param params    请求参数
     * @param callback  请求回调
     */
    public static void post(String url, List<Param> params, final ResultCallback callback) {
        getmInstance().postRequest(url, params, callback);
    }

    // POST请求参数类
    public static class Param {
        String key;//请求的参数
        String value;//参数的值
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * HTTP请求回调类,回调方法在UI线程中执行
     * @param <T>
     */
    public static abstract class ResultCallback<T> {

        Type mType;

        protected ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            // 返回父类的类型
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("缺少类型参数");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        /**
         * 请求成功回调
         * @param response
         */
        public abstract void onSuccess(T response);

        /**
         * 请求失败回调
         * @param e
         */
        public abstract void onFailure(Exception e);
    }

}