package com.kenzz.reviewapp.bean;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ken.huang on 10/23/2017.
 * 提供一些单例对象，一般可以用于工具类和管理类的初始化并返回
 */

@Module
public class BaseModule {

    @Singleton
    @Provides
    public Person getPerson(){
        return new Person();
    }
}
