package com.kenzz.reader.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huangdefa on 30/11/2017.
 * Version 1.0
 * ProcessInterceptor 进度拦截器
 */

public class ProcessInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                           .newBuilder()
                           .addHeader("Accept-Encoding", "identity")
                           .build();
        Response response = chain.proceed(request);
        Response newResponse = response.newBuilder()
                .body(new ProcessResponseBody(response.body()))
                .build();
        return newResponse;
    }
}
