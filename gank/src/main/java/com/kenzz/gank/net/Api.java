package com.kenzz.gank.net;

import com.kenzz.gank.bean.GankDailyEntity;
import com.kenzz.gank.bean.GankEntity;
import com.kenzz.gank.bean.GankSearchEntity;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ken.huang on 10/24/2017.
 * 网络请求
 */

public interface Api {
    //http://gank.io/api/data/Android/10/1
    static final String BASEURL="http://gank.io/api/";

    @GET("data/{category}/25/{pageNum}")
    Observable<GankEntity> getDataByCategory(@Path("category") String category, @Path("pageNum")int pageNum);

    @GET("day/{year}/{month}/{day}")
    Observable<GankDailyEntity> getDailyData(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    @GET("search/query/{query}/category/{type}/count/20/page/{pageNum}")
    Observable<GankSearchEntity> getSearchDataByCategory(@Path("query")String query, @Path("type")String type, @Path("pageNum")int pageNum);
}
