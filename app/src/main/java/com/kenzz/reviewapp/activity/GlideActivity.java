package com.kenzz.reviewapp.activity;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.kenzz.reviewapp.R;
import com.kenzz.reviewapp.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Glide 学习
 * 1. 基本流程和用法
 * 2.
 * 3.下载监听
 * 4.封装(加载圆形头像等)
 */
public class GlideActivity extends AppCompatActivity {

    Unbinder mUnbinder;
    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.imageView2)
    ImageView mImageView;
    @BindView(R.id.imageView3)
    ImageView imageView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_glide);
        mUnbinder = ButterKnife.bind(this);
        initGlide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    /**
     * Glide基本使用
     */
    private void initGlide(){
        Glide.with(this)
                .asDrawable() //asBitmap()加载静态图片 asGif()指定加载动态图片(传入静态图片会发生错误)
                .load(R.drawable.meizi) //load 支持记载 资源文件R.drawable.meizi
                // /加载网络文件 http://www.baidu.com/meizi/ddd.jpg/加载本地文件new File(path)/加载二进制字节 imageBytes[]./资源符Uri
                .apply(new RequestOptions().placeholder(R.drawable.qq_contact_list_headbg).circleCrop())
                //apply(RequestOptions) RequestOptions 可以指定一些配置：placeholder 占位符，error 错误占位符，circleCrop圆形图片
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        ToastUtil.showShortToast(GlideActivity.this,"Load Drawable Failed");
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(mImageView);
    }

    @OnClick({R.id.button})
    public void loadImage(){
        Glide.with(this)
                .load("http://guolin.tech/book.png")
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(R.mipmap.ic_launcher)
                )
                .into(imageView2);
    }


}
