package com.kenzz.gank;

import android.app.Application;

import com.kenzz.gank.net.ApiManager;

/**
 * Created by ken.huang on 10/24/2017.
 *
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ApiManager.getInstance().init(this);
        StatusBarHeight = getStatusBarHeight();
    }

    private static  MyApplication sInstance;
    public static MyApplication getInstance(){
        return sInstance;
    }

    public int StatusBarHeight=0;

    protected int getStatusBarHeight(){
        int statusBarHeight=0;
        int resId=getResources().getIdentifier("status_bar_height","dimen","android");
        if(resId!=0){
            statusBarHeight = (int) getResources().getDimension(resId);
        }
        return statusBarHeight;
    }
}
