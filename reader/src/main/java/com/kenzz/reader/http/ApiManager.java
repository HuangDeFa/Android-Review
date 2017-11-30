package com.kenzz.reader.http;

import android.text.TextUtils;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ken.huang on 11/24/2017.
 * ApiManager
 */

public class ApiManager {
    private ApiManager (){}
    private static ApiManager instance;
    public static ApiManager getInstance(){
        if(instance==null){
            synchronized (ApiManager.class){
                if(instance==null){
                    instance=new ApiManager();
                }
            }
        }
        return instance;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    private OkHttpClient client;

    private static Map<Class,String> baseUrlCache=new HashMap<Class,String>(){{
        put(GankService.class,"http://gank.io/api/");
        put(DownloadService.class,"http://gank.io/api/");
        put(DouService.class," https://api.douban.com/v2/");
    }};
    private Map<Class,Object> serviceCache=new HashMap<>();

    public <T> T getService(Class<T> clazz){
        Object o = serviceCache.get(clazz);
        if(o==null){
            if(clazz==DownloadService.class)
                o=createDownloadService(clazz);
             else
            o=createService(clazz);
            serviceCache.put(clazz,o);
        }
        return (T)o;
    }
    private <T> Object createService(Class<T> clazz) {
        Retrofit.Builder builder=new Retrofit.Builder();
        String baseUrl = baseUrlCache.get(clazz);
        if(TextUtils.isEmpty(baseUrl)){
            throw new IllegalArgumentException("can not find the base url for this class");
        }
        builder.baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getokHttpClient());

        return builder.build().create(clazz);
    }

    private OkHttpClient getokHttpClient() {
        if(client==null){
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10,TimeUnit.SECONDS)
                    .addInterceptor(new ProcessInterceptor())
                    .build();
        }
        return client;
    }

    private <T> Object createDownloadService(Class<T> clazz) {
        Retrofit.Builder builder=new Retrofit.Builder();
        String baseUrl =baseUrlCache.get(clazz);
        builder.baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getokHttpClient());

        return builder.build().create(clazz);
    }

}
