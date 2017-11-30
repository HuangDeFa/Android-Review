package com.kenzz.reader.http;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by huangdefa on 30/11/2017.
 * Version 1.0
 * ProcessResponseBody
 * 计算响应进度
 */

public class ProcessResponseBody extends ResponseBody {

    private ResponseBody mResponseBody;
    private BufferedSource mBufferedSource;
    public ProcessResponseBody(ResponseBody body) {
        mResponseBody=body;
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
        if(mBufferedSource==null) {
            mBufferedSource = Okio.buffer(new ProgressSource(mResponseBody.source()
                    ,mResponseBody.contentLength()));
        }
        return mBufferedSource;
    }

    static class ProgressSource extends ForwardingSource{
        private long totalLength;
        private long totalRead;

        public ProgressSource(Source delegate,long totalLength) {
            super(delegate);
            this.totalLength=totalLength;
        }

        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            long readCount = delegate().read(sink,byteCount);
            if(readCount==-1){
                totalRead=totalLength;
            }else {
                totalRead+=readCount;
            }
            float process = totalRead/totalLength;
            Log.d("ProcessInterceptor","DownloadProcess--> "+process+ "%");
            return readCount;
        }
    }
}
