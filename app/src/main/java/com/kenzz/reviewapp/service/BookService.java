package com.kenzz.reviewapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.util.Log;

import com.kenzz.reviewapp.aidl.Book;
import com.kenzz.reviewapp.aidl.IBookManager;
import com.kenzz.reviewapp.aidl.IUpdateBookListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookService extends Service {
    private final static String TAG = BookService.class.getSimpleName();
    private CopyOnWriteArrayList<Book> mBooks = new CopyOnWriteArrayList<>();
    //保存远程回调接口
    private RemoteCallbackList<IUpdateBookListener> mRemoteCallbackList = new RemoteCallbackList<>();

    public BookService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }

    private void onUpdateBook(Book book) throws RemoteException {
        int count = mRemoteCallbackList.beginBroadcast();
        for(int i=0;i<count;i++){
            mRemoteCallbackList.getBroadcastItem(i).onUpdateBook(book);
        }
        mRemoteCallbackList.finishBroadcast();
    }

    private IBookManager.Stub mStub = new IBookManager.Stub() {
        @Override
        public void addBook(Book book) throws RemoteException {
            if (mBooks.contains(book)) {
                Log.d(TAG, "already exist!!");
            }else {
                mBooks.add(book);
            }
           onUpdateBook(book);
        }

        @Override
        public List<Book> getBooks() throws RemoteException {
            return mBooks;
        }

        @Override
        public void registerUpdateBookListener(IUpdateBookListener listener) throws RemoteException {
          mRemoteCallbackList.register(listener);
        }

        @Override
        public void unregisterUpdateBookListener(IUpdateBookListener listener) throws RemoteException {
          mRemoteCallbackList.unregister(listener);
        }
    };
}
