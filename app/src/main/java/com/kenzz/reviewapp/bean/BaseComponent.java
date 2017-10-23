package com.kenzz.reviewapp.bean;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ken.huang on 10/23/2017.
 * 作为全局单例的注入，比如一下工具类的注入。子组件可以指定依赖这个父组件
 *
 */

@Singleton
@Component(modules = BaseModule.class)
public interface BaseComponent {
    Person getPerson();
}
