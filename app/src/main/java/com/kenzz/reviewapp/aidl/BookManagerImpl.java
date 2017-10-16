package com.kenzz.reviewapp.aidl;

import android.os.RemoteCallbackList;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by huangdefa on 16/10/2017.
 * Version 1.0
 *
 */

public class BookManagerImpl extends IBookManager.Stub {
    public static final int QUERYCODE=100;
    private CopyOnWriteArrayList<Book> mBooks=new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IUpdateBookListener> mRemoteCallbackList=new RemoteCallbackList<>();
    @Override
    public void addBook(Book book) throws RemoteException {
        if(!mBooks.contains(book)){
            int N = mRemoteCallbackList.beginBroadcast();
            for(int i=0;i<N;i++){
                IUpdateBookListener listener = mRemoteCallbackList.getBroadcastItem(i);
                if(listener!=null){
                    listener.onUpdateBook(book);
                }
            }
            mRemoteCallbackList.finishBroadcast();
        }
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
}
