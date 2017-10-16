package com.kenzz.reviewapp.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.kenzz.reviewapp.aidl.IBinderPool;

/**
 * Created by huangdefa on 16/10/2017.
 * Version 1.0
 * 实现绑定远程服务，提供远程服务查询IBinder
 */

public class BinderPoolManager {
    private Context mContext;
    private IBinderPool mBinderPool;
    private static volatile BinderPoolManager sPoolManager;

    public BinderPoolManager getInstance(Context context){
        if(sPoolManager==null){
            synchronized (BinderPoolManager.class){
                if(sPoolManager==null){
                   sPoolManager = new BinderPoolManager(context);
                }
            }
        }
        return sPoolManager;
    }

    //Just do connection to remote service
    public synchronized void init(){
        Intent intent = new Intent(mContext,BinderPoolService.class);
        mContext.startService(intent);
        mContext.bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
    }

    private BinderPoolManager(Context context){
        this.mContext = context;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
          init();
        }
    };

    /**
     *  根据queryCode 获取相应的IBinder
     * @param queryCode {@link com.kenzz.reviewapp.aidl.BookManagerImpl#QUERYCODE }
     * @param <B>
     * @return
     */
    public <B extends IBinder> B queryBinder(int queryCode){
        IBinder binder  = null;
        try {
            if(mBinderPool!=null) {
               binder = mBinderPool.queryBinder(queryCode);
                if (binder != null) {
                    return (B) binder;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
