package com.kenzz.reviewapp;

import android.app.Application;

import com.kenzz.reviewapp.bean.BaseComponent;
import com.kenzz.reviewapp.bean.BaseModule;
import com.kenzz.reviewapp.bean.DaggerBaseComponent;

/**
 * Created by ken.huang on 10/23/2017.
 *
 */

public class MyApplication extends Application {

    //全局单例的 mBaseComponent
    BaseComponent mBaseComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseComponent = DaggerBaseComponent.builder().baseModule(new BaseModule())
                .build();
    }

    public BaseComponent getBaseComponent(){
        return mBaseComponent;
    }
}
