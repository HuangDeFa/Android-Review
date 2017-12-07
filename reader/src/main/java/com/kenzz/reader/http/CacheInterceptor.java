package com.kenzz.reader.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by ken.huang on 12/7/2017.
 * CacheInterceptor
 */

public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return  originalResponse.newBuilder()
                .removeHeader("pragma")
                .addHeader("Cache-Control","max-age=180") //60s缓存
                 .build();
    }
}
