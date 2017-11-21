package com.kenzz.reviewapp.skinDemo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.lang.reflect.Method;

/**
 * Created by huangdefa on 16/11/2017.
 * Version 1.0
 * 加载指定资源
 */

public class ResourceCompat {
    private Context mContext;
    private Resources mResources;
    private String mpackageName;
    public ResourceCompat (Context context, @NonNull String skinPath){
        mContext = context.getApplicationContext();
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
           Method method =  AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
           method.setAccessible(true);
           method.invoke(assetManager,skinPath);
            Resources superResource = mContext.getResources();
            mResources = new Resources(assetManager,superResource.getDisplayMetrics(),
                    superResource.getConfiguration());
            PackageInfo packageArchiveInfo = context.getPackageManager()
                    .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
            if(packageArchiveInfo!=null){
                mpackageName = packageArchiveInfo.packageName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getColor(@NonNull  String resourceName){
        int identifier = mResources.getIdentifier(resourceName, "color", mpackageName);
        if(identifier>0){
            return mResources.getColor(identifier);
        }
        return 0;
    }

    public Drawable getDrawable(@NonNull String resourceName){
        int identifier = mResources.getIdentifier(resourceName, "drawable", mpackageName);
        if(identifier>0){
            return mResources.getDrawable(identifier);
        }
        return null;
    }

}
