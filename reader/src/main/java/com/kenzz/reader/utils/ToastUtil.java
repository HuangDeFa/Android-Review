package com.kenzz.reader.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ken.huang on 11/27/2017.
 * ToastUtil
 */

public class ToastUtil {
    public static void showShortToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
