package com.kenzz.reader.http;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by ken.huang on 11/27/2017.
 *  用于下载
 */

public interface DownloadService {
    @GET
    Observable<ResponseBody> downloadFile(@Url String url, String filename);
}
