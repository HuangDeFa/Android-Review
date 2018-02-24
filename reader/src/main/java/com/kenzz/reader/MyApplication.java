package com.kenzz.reader;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;

import com.kenzz.reader.utils.ACache;
import com.kenzz.reader.utils.NetWorkUtil;
import com.kenzz.reader.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangdefa on 26/11/2017.
 * Version 1.0
 */

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static MyApplication sInstance;

    public static MyApplication getInstance() {
        return sInstance;
    }

    private List<Activity> mActivityList;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mActivityList = new ArrayList<>();
        registerActivityLifecycleCallbacks(this);
        String date = SPUtil.getString(this, Constant.LAST_DAILY_DATE);
        String gank_ioType=SPUtil.getString(this,Constant.LAST_GANK_IO_TYPE);
        if(TextUtils.isEmpty(date)){
            SPUtil.putString(this,"2017_11_24",Constant.LAST_DAILY_DATE);
        }
        if(TextUtils.isEmpty(gank_ioType)){
            SPUtil.putString(this,"iOS",Constant.LAST_GANK_IO_TYPE);
        }
        defaultExceptionHandler=Thread.getDefaultUncaughtExceptionHandler();
        mExceptionHandler = new MyExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(mExceptionHandler);
        NetWorkUtil.initNetWorkListener(this);
    }

    public synchronized void exitApp(){
        for (Activity activity : mActivityList) {
            activity.finish();
        }
        mActivityList.clear();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityList.remove(activity);
    }

    private class MyExceptionHandler implements Thread.UncaughtExceptionHandler{

        @Override
        public void uncaughtException(Thread t, Throwable e) {
           if(!handlerException(t,e)){
               if(defaultExceptionHandler!=null){
                   defaultExceptionHandler.uncaughtException(t,e);
               }
           }
        }
    }

    /**
     * TODO 处理异常
     * @param t 抛出异常的线程
     * @param e 异常对象
     * @return 返回值，true表示已处理 false未处理将交由系统处理
     */
    private boolean handlerException(Thread t,Throwable e){
        e.printStackTrace();
        return true;
    }

    private MyExceptionHandler mExceptionHandler;
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
}
