package com.kenzz.reviewapp.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;

/**
 * Created by ken.huang on 3/8/2018.
 * 监测主线程Looper
 */

public class MainLooperMonitor {
    private static final String TAG = MainLooperMonitor.class.getSimpleName();
    private static MainLooperMonitor sLooperMonitor;

    private HandlerThread mHandlerThread;
    private Handler mHandler;

    //监测主线程绘制流程的时间片
    private static final long TRACK_TIMEOUT=1000L;

    private MainLooperMonitor(){
        mHandlerThread=new HandlerThread("Main Looper Monitor");
        mHandlerThread.start();
        mHandler=new Handler(mHandlerThread.getLooper());
    }

    public static MainLooperMonitor getLooperMonitor(){
        if(sLooperMonitor==null){
            synchronized (MainLooperMonitor.class){
                if(sLooperMonitor==null){
                    sLooperMonitor=new MainLooperMonitor();
                }
            }
        }
        return sLooperMonitor;
    }
    private StringBuilder mStringBuilder;
    private Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            mStringBuilder=new StringBuilder();
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement sk: stackTrace) {
               mStringBuilder.append(sk.toString());
               mStringBuilder.append("\n");
            }
            Log.d(TAG,"Render Monitor: \n "+mStringBuilder.toString());
        }
    };


    private static final String RENDERSTART=">>>>> Dispatching";
    private static final String RENDEREND="<<<<< Finished";
    public void start(){
        Looper.getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String x) {
                        if(x.startsWith(RENDERSTART)){
                            startMonitor();
                        }
                        if(x.startsWith(RENDEREND)){
                            removeMonitor();
                        }
            }
        });
    }

    public void startMonitor(){
        mHandler.postDelayed(mRunnable,TRACK_TIMEOUT);
    }

    public void removeMonitor(){
        mHandler.removeCallbacks(mRunnable);
    }
}
