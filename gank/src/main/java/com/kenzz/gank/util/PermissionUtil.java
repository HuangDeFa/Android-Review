package com.kenzz.gank.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangdefa on 25/10/2017.
 * Version 1.0
 */

public class PermissionUtil {
    public static List<String> checkPermission(Context context,String... permissions){
        List<String> requestPermission = new ArrayList<>();
        if(permissions!=null && permissions.length>0) {
            for (String permission : permissions) {
                if(ContextCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_DENIED){
                    requestPermission.add(permission);
                }
            }
        }
        return requestPermission;
    }
}
