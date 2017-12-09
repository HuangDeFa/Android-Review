package com.kenzz.reader.utils;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kenzz.reader.MyApplication;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by huangdefa on 25/11/2017.
 * Version 1.0
 * ImageLoader
 */

public class ImageLoader {
    public static void LoadImage(ImageView imageView, Object model) {
        Glide.with(imageView)
                .load(model)
                .into(imageView);
    }

    public static void LoadImage(ImageView imageView, Object model, @DrawableRes int placeholder) {
        Glide.with(imageView)
                .asDrawable()
                .apply(new RequestOptions().centerCrop().placeholder(placeholder).dontAnimate())
                .load(model)
                .into(imageView);
    }

    /**
     *  图片高斯模糊后作为背景
     * @param imageView
     * @param model
     * @param placeholder
     */
    public static void LoadImageAsBackground(ImageView imageView, Object model,  @DrawableRes int placeholder){
        Glide.with(imageView)
                .asDrawable()
                .apply(new RequestOptions().placeholder(placeholder).error(placeholder)
                        .transform(new BlurTransformation(14,5)).dontAnimate())
                .load(model)
                .into(imageView);
    }

    /**
     * 指定记载图片的大小
     * @param imageView
     * @param model
     * @param placeholder
     * @param height
     * @param width
     */
    public static void LoadImage(ImageView imageView, Object model, @DrawableRes int placeholder,
                                 int width,int height) {
        Glide.with(imageView)
                .asDrawable()
                .thumbnail(.2f)
                .apply(new RequestOptions().centerCrop().placeholder(placeholder)
                        .override(dp2px(width),dp2px(height)))
                .load(model)
                .into(imageView);
    }

    private static int dp2px(float dp){
        float scale = MyApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dp*scale+.5f);
    }
}
