package com.kenzz.gank;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void apiTest() {
        Response response=null;
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();
            response= (client.newCall(new Request.Builder()
                    .get()
                    .url("http://gank.io/api/data/Android/20/1")
                    .build())).execute();
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            System.out.println(response==null);
        }
    }
}