package com.kenzz.reviewapp.okHttp;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by ken.huang on 11/20/2017.
 * 获取下载进度的OkHttp 拦截器
 */

public class ProgressInterceptor implements Interceptor {

    public static interface ProgressListener{
        void onProgress(float progress);
    }

    static Map<String,ProgressListener> LISTENER_MAP=new HashMap<>();

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        String url = request.url().toString();
        Response response = chain.proceed(request);
        Response newResponse = response.newBuilder()
                .body(new ProgressResponseBody(response.body(),url)).build();
        return newResponse;
    }

    public void addListener(String url,ProgressListener listener){
        LISTENER_MAP.put(url,listener);
    }

    public void removeListener(String url){
        LISTENER_MAP.remove(url);
    }

    static class ProgressResponseBody extends ResponseBody{

        private static final String TAG = ProgressResponseBody.class.getSimpleName();
        private ResponseBody mResponseBody;
        private ProgressListener listener;
        private BufferedSource bufferSource;
        ProgressResponseBody(ResponseBody body,String url) {
            mResponseBody = body;
            listener = LISTENER_MAP.get(url);
        }

        @Nullable
        @Override
        public MediaType contentType() {
            return mResponseBody.contentType();
        }

        @Override
        public long contentLength() {
            return mResponseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
             if(bufferSource==null){
                 bufferSource = Okio.buffer(new ProgressSource(mResponseBody.source()));
             }
             return bufferSource;
        }

        /**
         * 自定义ProgressSource
         */
        class ProgressSource extends ForwardingSource{
            long totalBytesRead = 0;

            int currentProgress;

            ProgressSource(Source delegate) {
                super(delegate);
            }

            /**
             *  读取并计算百分比
             * @param sink
             * @param byteCount
             * @return
             * @throws IOException
             */
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                long fullLength = mResponseBody.contentLength();
                if (bytesRead == -1) {
                    totalBytesRead = fullLength;
                } else {
                    totalBytesRead += bytesRead;
                }
                int progress = (int) (100f * totalBytesRead / fullLength);
                Log.d(TAG, "download progress is " + progress);
                if (listener != null && progress != currentProgress) {
                    listener.onProgress(progress);
                }
                if (listener != null && totalBytesRead == fullLength) {
                    listener = null;
                }
                currentProgress = progress;
                return bytesRead;
            }
        }
    }
}
