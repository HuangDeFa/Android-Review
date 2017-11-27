package com.kenzz.reader.http;

/**
 * Created by huangdefa on 27/11/2017.
 * Version 1.0
 * DownloadListener
 */

public interface DownloadListener {
    void onError(Throwable error);
    void onSuccess();
    void onComplete();
}
