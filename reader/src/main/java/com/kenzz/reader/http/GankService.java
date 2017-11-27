package com.kenzz.reader.http;

import android.support.annotation.IntRange;

import com.kenzz.reader.bean.GankDailyEntity;
import com.kenzz.reader.bean.GankEntity;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by ken.huang on 11/24/2017.
 * GankService
 */

public interface GankService {

    //http://gank.io/api/day/2015/08/06
    @GET("day/{year}/{month}/{day}")
    Observable<GankDailyEntity> getDailyGank(@Path("year") String year,@Path("month")String month,@Path("day")String day);

    //http://gank.io/api/random/data/Android/20
    @GET("random/data/{chanel}/{count}")
    Observable<GankEntity> getRandomGank(@Path("chanel")String chanel,@Path("count") @IntRange(from = 1,to = 20) int pageCount);

    //http://gank.io/api/data/Android/10/2
    @GET("data/{chanel}/10/{pageIndex}")
    Observable<GankEntity> getGankDayByPage(@Path("chanel")String chanel,@Path("pageIndex")int pageIndex);
}
