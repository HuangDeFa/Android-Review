package com.kenzz.reviewapp.okHttp;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by ken.huang on 11/21/2017.
 * 自定义GlideModule 进行自定义Glide的配置
 */

@GlideModule
public class MyGlideModule implements com.bumptech.glide.module.GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //配置硬盘缓存
       builder.setDiskCache(new ExternalCacheDiskCacheFactory(context,"glideDemo",500));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        //自定义组件
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new ProgressInterceptor())
                .build();
       registry.replace(GlideUrl.class, InputStream.class, new OkHttpGlideUrlLoader.Factory(client));
    }
}
