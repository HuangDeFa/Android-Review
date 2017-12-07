package com.kenzz.reader.http;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by ken.huang on 12/7/2017.
 * ProcessRequestBody
 */

public class ProcessRequestBody extends RequestBody {

    private RequestBody originalBody;
    private BufferedSink mBufferedSink;

    public ProcessRequestBody(RequestBody body) {
        originalBody = body;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return originalBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return originalBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
      if(mBufferedSink==null){
          mBufferedSink= Okio.buffer(new ProcessSink(sink));
      }
      originalBody.writeTo(mBufferedSink);
      mBufferedSink.flush();
    }

    class ProcessSink extends ForwardingSink{
        private long totalWrite;
        private long fullLength;
        public ProcessSink(Sink delegate) {
            super(delegate);
            try {
                fullLength = contentLength();
            }catch (Exception e){
                fullLength =-1;
            }
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            totalWrite+=byteCount;
            int progress=(int)(100f*totalWrite/fullLength);
            Log.d("ProcessRequest","uploadProgress-->"+progress+"%");
        }
    }
}
