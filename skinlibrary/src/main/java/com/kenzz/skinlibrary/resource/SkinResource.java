package com.kenzz.skinlibrary.resource;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Method;

/**
 * Created by ken.huang on 11/3/2017.
 * version:1.0
 * author:ken
 * description:加载皮肤资源类
 */

public class SkinResource {
    private Context mContext;
    private Resources mResources;
    private String mSkipPackName;
    private Resources supResource;

    /**
     * @param context      上下文对象
     * @param resourcePath 皮肤包的路径 eg:sdcard0/skin/Sample.skin
     */
    public SkinResource(Context context, @NonNull String resourcePath) {
        mContext = context.getApplicationContext();
        try {
            if (TextUtils.isEmpty(resourcePath) || resourcePath.endsWith(".skin")) {
                throw new IllegalArgumentException("Skin package path can not be null " +
                        "and must be extension .skin");
            }
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(assetManager, resourcePath);
            supResource = mContext.getResources();
            mResources = new Resources(assetManager, supResource.getDisplayMetrics(), supResource.getConfiguration());
            //获取皮肤包名
            PackageInfo packageArchiveInfo =
                    mContext.getPackageManager().getPackageArchiveInfo(resourcePath, PackageManager.GET_ACTIVITIES);
            mSkipPackName = packageArchiveInfo.packageName;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getResourceId(String resName, int resourceType) {
        int resourceId = 0;
        switch (resourceType) {
            case 0:
                resourceId = mResources.getIdentifier(resName, "drawable", mSkipPackName);
                break;
            case 1:
                resourceId = mResources.getIdentifier(resName, "background", mSkipPackName);
                break;
            case 2:
                resourceId = mResources.getIdentifier(resName, "textColor", mSkipPackName);
                break;
        }
        return resourceId;
    }

    public String getResourceValueName(View view, AttributeSet attrs){
        int count = attrs.getAttributeCount();
        return null;
    }
}
