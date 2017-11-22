package com.kenzz.reviewapp.okHttp;

import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by ken.huang on 11/21/2017.
 *  自定义OkHttp GlideLoader
 */

public class OkHttpGlideUrlLoader implements ModelLoader<GlideUrl,InputStream> {
    private OkHttpClient client;

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(GlideUrl glideUrl, int width, int height, Options options) {
        return new LoadData<InputStream>(glideUrl,new OkHttpUrlFetcher(glideUrl,client));
    }

    public OkHttpGlideUrlLoader(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public boolean handles(GlideUrl glideUrl) {
        return true;
    }

    /**
     * The default factory for {@link OkHttpGlideUrlLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {

        private OkHttpClient client;
        public Factory() {
        }

        public Factory(OkHttpClient client){
            this.client = client;
        }

        private synchronized OkHttpClient getOkHttpClient(){
            if(this.client==null){
                this.client = new OkHttpClient();
            }
            return client;
        }

        @Override
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new OkHttpGlideUrlLoader(getOkHttpClient());
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }
}
