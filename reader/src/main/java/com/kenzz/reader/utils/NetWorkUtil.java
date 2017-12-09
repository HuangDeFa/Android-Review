package com.kenzz.reader.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by ken.huang on 12/7/2017.
 * NetWorkUtil
 */

public class NetWorkUtil {
    static List<WeakReference<onNetWorkChangeListener>> networkChangeListeners=new ArrayList<>();
    public interface onNetWorkChangeListener{
        void onConnected();
        void onDisConnected();
    }
    public static boolean isNetworkAvailable(Context context){
       ConnectivityManager manager= (ConnectivityManager)
               context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo!=null){
           return networkInfo.isConnected();
        }
        return false;
    }



    public static void initNetWorkListener(Context context){
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NetworkInfo networkInfo =
                        intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(networkInfo!=null&& networkInfo.isConnected()){
                   // ToastUtil.showShortToast(context,"网络恢复！");
                    Observable.fromIterable(networkChangeListeners)
                            .filter(x->x.get()!=null)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(x->x.get().onConnected());
                }else {
                    Observable.fromIterable(networkChangeListeners)
                            .filter(x->x.get()!=null)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(x->x.get().onDisConnected());
                    ToastUtil.showShortToast(context,"网络不可用！");
                }
            }
        },new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public static void registNetWorkListener(onNetWorkChangeListener l){
        synchronized (NetWorkUtil.class){
            for (WeakReference<onNetWorkChangeListener> networkChangeListener : networkChangeListeners) {
                if(networkChangeListener.get()!=null && networkChangeListener.get().equals(l)){
                    return;
                }
            }
            networkChangeListeners.add(new WeakReference<>(l));
        }
    }

    public static void unregistNetWorkListener(onNetWorkChangeListener l){
        synchronized (NetWorkUtil.class){
            for (WeakReference<onNetWorkChangeListener> networkChangeListener : networkChangeListeners) {
                if(networkChangeListener.get()!=null && networkChangeListener.get().equals(l)){
                    networkChangeListeners.remove(networkChangeListener);
                    return;
                }
            }
        }
    }
}
