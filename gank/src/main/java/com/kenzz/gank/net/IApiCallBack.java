package com.kenzz.gank.net;

/**
 * Created by huangdefa on 24/10/2017.
 * Version 1.0
 */

public interface IApiCallBack<T> {

    void onError(Throwable throwable);

    void onSuccess(T data);
}
