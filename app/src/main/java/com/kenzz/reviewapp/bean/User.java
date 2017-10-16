package com.kenzz.reviewapp.bean;

import java.io.Serializable;

/**
 * Created by huangdefa on 15/10/2017.
 * Version 1.0
 * 实现序列化
 */

public class User implements Serializable {
    //系列号ID用于标记当前类，当反序列化时进行校验
    //如果不设置，当类结构改变时系统自动生成的系列号ID进行校验肯定失败并报错
    public static final long serialVersionUID=1L;

}
