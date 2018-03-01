package com.kenzz.reviewapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by ken.huang on 10/9/2017.
 *  Activity 基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected void setFullScreen(){
        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    protected int getStatusBarHeight(){
        int statusBarHeight=0;
        int resId=getResources().getIdentifier("status_bar_height","dimen","android");
        if(resId!=0){
           statusBarHeight = (int) getResources().getDimension(resId);
        }
        return statusBarHeight;
    }

    protected boolean hasIntent(Intent intent){
        PackageManager packageManager = getApplicationContext().getPackageManager();
        //获取最佳匹配的Activity
        ResolveInfo resolveInfo = packageManager.resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY);
        //获取所有匹配的Activity,packageManager.MATCH_DEFAULT_ONLY标记着含有category为Default的Activity
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, packageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo!=null;
    }

    /**
     * 使组件不可用，配合activity-alias 可以进行应用图标的更改
     * @param componentName
     */
    protected void disableComponent(ComponentName componentName){
        getPackageManager().setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
    }

    /**
     * 使组件可用
     * @param componentName
     */
    protected void enableComponent(ComponentName componentName){
        getPackageManager().setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
    }

}
