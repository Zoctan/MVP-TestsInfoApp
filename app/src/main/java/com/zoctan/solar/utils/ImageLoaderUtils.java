package com.zoctan.solar.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zoctan.solar.R;

/**
 * Glide图片加载工具类
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url) {
        if(imageView == null) {
            throw new IllegalArgumentException("ImageView为空");
        }
        Glide.with(context)
                // 加载图片的地址
                .load(url)
                // 跳过缓存至内存, 默认缓存在磁盘
                .skipMemoryCache(true)
                // 设置加载之前的默认图片
                .placeholder(R.drawable.ic_image_loading)
                // 设置加载失败的默认图片
                .error(R.drawable.ic_image_loadfail)
                // 设置淡入淡出效果，默认300ms
                .crossFade()
                // 填充至view中
                .into(imageView);
    }

    public static void displayUserImg(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url).skipMemoryCache(true).into(imageView);
    }
}
