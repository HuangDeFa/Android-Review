package com.kenzz.reader.http;

import com.kenzz.reader.bean.OneBookEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ken.huang on 11/29/2017.
 *
 */

public interface DouService {
 @GET("book/search")
 Observable<OneBookEntity> getBooksByPage(@Query("tag")String tag,@Query("start")int offset);
}
