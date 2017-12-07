package com.kenzz.reader.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ken.huang on 12/7/2017.
 * NetWorkUtil
 */

public class NetWorkUtil {
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
        ConnectivityManager manager= (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NetworkInfo networkInfo =
                        intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(networkInfo!=null&& networkInfo.isConnected()){
                   // ToastUtil.showShortToast(context,"网络恢复！");
                }else {
                    ToastUtil.showShortToast(context,"网络不可用！");
                }
            }
        },new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}
