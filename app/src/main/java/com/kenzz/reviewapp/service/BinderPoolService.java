package com.kenzz.reviewapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;

import com.kenzz.reviewapp.aidl.BinderPoolImpl;

public class BinderPoolService extends Service {

    private IBinder mBinder;

    public BinderPoolService() {

    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
       super.onStartCommand(intent, flags, startId);
       return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        mBinder = new BinderPoolImpl();
        return mBinder;
    }
}
