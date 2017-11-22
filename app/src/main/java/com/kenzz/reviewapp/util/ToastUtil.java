package com.kenzz.reviewapp.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ken.huang on 11/20/2017.
 * ToastUtil
 */

public class ToastUtil {

    public  static void showShortToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public  static void showLongToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

}
