package com.kenzz.reviewapp.okHttp;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by ken.huang on 11/21/2017.
 * 自定义okHttp 网络加载
 */

public class OkHttpUrlFetcher implements DataFetcher<InputStream> {

    private volatile boolean isCancelled;
    private final GlideUrl glideUrl;
    private OkHttpClient client;
    private InputStream inputStream;
    private ResponseBody responseBody;

    public OkHttpUrlFetcher(GlideUrl glideUrl, OkHttpClient client) {
        this.glideUrl = glideUrl;
        this.client = client;
    }

    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(glideUrl.toStringUrl());
        Map<String, String> headers = glideUrl.getHeaders();
        for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
            builder.addHeader(headerEntry.getKey(), headerEntry.getValue());
        }
        if (isCancelled) {
            callback.onLoadFailed(new Exception("The task has be cancel"));
            return;
        }

        try {
            Response response = client.newCall(builder.build()).execute();
            responseBody = response.body();
            if (!response.isSuccessful() || responseBody == null) {
                callback.onLoadFailed(new Exception("Load data failed"));
                return;
            }
            inputStream = ContentLengthInputStream.obtain(responseBody.byteStream(), responseBody.contentLength());
            callback.onDataReady(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            callback.onLoadFailed(e);
        }
    }

    @Override
    public void cleanup() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(responseBody!=null){
            responseBody.close();
        }
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
