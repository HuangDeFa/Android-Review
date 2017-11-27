package com.kenzz.reader;

import com.kenzz.reader.http.DownloadManager;
import com.kenzz.reader.http.DownloadService;

import org.junit.Test;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

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

    @Test public void testDownload(){
        OkHttpClient client = new  OkHttpClient.Builder()
                .build();
        Retrofit.Builder builder=new Retrofit.Builder();
       DownloadService service =
                 builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://aa.example.com/api/")
                .client(client)
                .build()
                .create(DownloadService.class);
       service.downloadFile("http://ww2.sinaimg.cn/large/7a8aed7bjw1ewym3nctp0j20i60qon23.jpg")
               .subscribeOn(Schedulers.io())
               .observeOn(Schedulers.io())
               .subscribe(new Observer<ResponseBody>() {
                   @Override
                   public void onSubscribe(Disposable d) {
                       System.out.println(d);
                   }

                   @Override
                   public void onNext(ResponseBody responseBody) {
                       System.out.println(responseBody);
                   }

                   @Override
                   public void onError(Throwable e) {
                       System.out.println(e);
                   }

                   @Override
                   public void onComplete() {
                       System.out.println("complete!!");
                   }
               });
    }
}