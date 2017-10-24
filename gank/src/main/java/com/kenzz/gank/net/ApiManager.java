package com.kenzz.gank.net;

import android.content.Context;
import android.support.annotation.StringDef;

import com.kenzz.gank.bean.GankEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ken.huang on 10/24/2017.
 * Api的实现类
 */

public class ApiManager {

    public static final String ANDROID_TYPE = "Android";
    public static final String IOS_TYPE = "iOS";
    public static final String FRONT_END = "前端";
    public static final String ALL_TYPE = "all";
    public static final String VIDEO_TYPE = "休息视频";
    public static final String EXPAND_RESOURCE_TYPE = "拓展资源";
    public static final String WELFARE_TYPE = "福利";
    //搜索
    public static final String APP_TYPE = "App";
    public static final String RECOMMEND_TYPE = "瞎推荐";

    @StringDef({ANDROID_TYPE,IOS_TYPE,FRONT_END,ALL_TYPE,
            VIDEO_TYPE, EXPAND_RESOURCE_TYPE, WELFARE_TYPE,APP_TYPE,RECOMMEND_TYPE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface CategoryMode{}

    private ApiManager() {
    }

    private static final ApiManager sManager = new ApiManager();

    public static ApiManager getInstance() {
        return sManager;
    }

    private Context mContext;
    private Retrofit mRetrofit;
    private OkHttpClient mHttpClient;
    private Api mApi;

    /**
     * 在Application中进行初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.mContext = context;
        mHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new BaseInterceptor())
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Api.BASEURL)
                .client(mHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApi = mRetrofit.create(Api.class);
    }

    public void getDataByCategory(@CategoryMode String category, int pageNum,IApiCallBack<GankEntity> callBack) {
       subscribe(mApi.getDataByCategory(category,String.valueOf(pageNum)),callBack);
    }

    private <T> void  subscribe(Observable<T> observable, final IApiCallBack<T> callBack){
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull T t) {
              if(callBack!=null){
                  callBack.onSuccess(t);
              }
            }

            @Override
            public void onError(@NonNull Throwable e) {
              if(callBack!=null){
                  callBack.onError(e);
              }
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
