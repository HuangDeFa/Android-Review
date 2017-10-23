package com.kenzz.reviewapp.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by ken.huang on 10/23/2017.
 * Dagger 全局注解
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface PerApp {
}
