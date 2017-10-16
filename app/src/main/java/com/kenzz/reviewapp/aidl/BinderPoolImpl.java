package com.kenzz.reviewapp.aidl;

import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created by huangdefa on 16/10/2017.
 * Version 1.0
 * BinderPool 根据请求码返回对应的Binder对象给客户端。在多个Binder的时候
 * 只需要一个Service就可以承载多个不同业务的Binder请求
 */

public class BinderPoolImpl extends IBinderPool.Stub {
    private IBinder mBinder=null;
    @Override
    public IBinder queryBinder(int queryCode) throws RemoteException {
        switch (queryCode){
            case BookManagerImpl.QUERYCODE:
                mBinder = new BookManagerImpl();
                break;
        }
        return mBinder;
    }
}
