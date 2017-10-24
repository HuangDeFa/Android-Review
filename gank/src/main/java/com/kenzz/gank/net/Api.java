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
    static final String BASEURL="https://gank.io/api/";

    @GET("data/{category}/25/{pageNum}")
    Observable<GankEntity> getDataByCategory(@Path("category") String category, @Path("pageNum")String pageNum);

    @GET("day/{year}/{month}/{day}")
    Observable<GankDailyEntity> getDailyData(@Path("year") String year, @Path("month") String month, @Path("day") String day);

    @GET("search/query/{query}/category/{type}/count/20/page/{pageNum}")
    Observable<GankSearchEntity> getSearchDataByCategory(@Path("query")String query, @Path("type")String type, @Path("pageNum")int pageNum);
}
