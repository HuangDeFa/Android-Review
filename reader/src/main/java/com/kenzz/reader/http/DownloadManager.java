package com.kenzz.reader.http;

import com.kenzz.reader.MyApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by ken.huang on 11/27/2017.
 * DownloadManager
 */

public class DownloadManager {

    public static void downloadFile(String url, String fileName, DownloadListener listener) {
        ApiManager.getInstance().getService(DownloadService.class)
                .downloadFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(body ->
                        saveFile(body, fileName))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(e -> {
                    if (listener != null) {
                        listener.onError(e);
                    }
                })
                .doOnComplete(() -> {
                    if (listener != null) {
                        listener.onComplete();
                    }
                })
                .subscribe(s -> {
                    if (listener != null) {
                        listener.onSuccess();
                    }
                });
    }

    private static void saveFile(ResponseBody body, String fileName) throws Exception {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        } else {
           File dir = new File(fileName.substring(0,fileName.lastIndexOf("/")));
           dir.mkdirs();
        }
        BufferedInputStream bis = new BufferedInputStream(body.byteStream());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
        byte[] buffer = new byte[1024 * 1024];
        int len;
        while ((len = bis.read(buffer, 0, buffer.length)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.flush();
        bos.close();
        bis.close();
    }

    public static String getDefaultDirPath(@NonNull String dir,@NonNull String fileName){
       return MyApplication.getInstance()
                .getExternalFilesDir(dir)
                 .getAbsolutePath()+File.separator+fileName;
    }
}
