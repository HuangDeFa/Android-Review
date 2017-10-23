package com.kenzz.reviewapp.bean;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by huangdefa on 15/10/2017.
 * Version 1.0
 * 实现序列化
 */

public class User implements Serializable {
    //系列号ID用于标记当前类，当反序列化时进行校验
    //如果不设置，当类结构改变时系统自动生成的系列号ID进行校验肯定失败并报错
    public static final long serialVersionUID=1L;

    public static final int GENDER_MALE=1;
    public static final int GENDER_FElMALE =2;

    @IntDef({GENDER_MALE, GENDER_FElMALE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GenderMode{}

    private String name;
    private int gender;

    public User(String name,@GenderMode int gender) {
        this.name = name;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(@GenderMode int gender) {
        this.gender = gender;
    }
}
