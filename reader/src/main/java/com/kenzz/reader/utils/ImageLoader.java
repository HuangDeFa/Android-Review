package com.kenzz.reader.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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
                .apply(new RequestOptions().centerCrop().placeholder(placeholder))
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
                        .transform(new BlurTransformation(14,5)))
                .load(model)
                .into(imageView);
    }
}
