package com.kenzz.gank;

import android.app.Application;

import com.kenzz.gank.net.ApiManager;

/**
 * Created by ken.huang on 10/24/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiManager.getInstance().init(this);
    }
}
