package com.kenzz.reviewapp.service;

import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by huangdefa on 03/11/2017.
 * Version 1.0
 * 一个利用动态代理进行简单Hook的例子
 */

public class ServiceHook {

    //反射获取ServiceManger相应的方法和属性
    static class ServiceManger{
        static Class sServiceManagerClazz;
        static Method sGetServiceMethod;
        static Map<String,IBinder> sCache;
        static {
            try {
                sServiceManagerClazz = Class.forName("android.os.ServiceManager");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        static IBinder getService(String serviceName){
            if(sServiceManagerClazz==null){
                return null;
            }
            if(sGetServiceMethod==null){
                try {
                    sGetServiceMethod = sServiceManagerClazz.getDeclaredMethod("getService",String.class);
                    sGetServiceMethod.setAccessible(true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            if(sGetServiceMethod!=null) {
                try {
                   return (IBinder) sGetServiceMethod.invoke(null,serviceName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        static void setService(String serviceName,IBinder service){
            if(sServiceManagerClazz!=null){
                if(sCache==null){
                    try {
                        Field cache = sServiceManagerClazz.getDeclaredField("sCache");
                        cache.setAccessible(true);
                        sCache = (Map<String, IBinder>) cache.get(null);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if(sCache!=null){
                    sCache.remove(serviceName);
                    sCache.put(serviceName,service);
                }
            }
        }
    }

    static class ServiceHookHandler implements InvocationHandler{
        IBinder mBase;
        Class iInterface;
        Class mStub;
        InvocationHandler mInvocationHandler;
        ServiceHookHandler(IBinder binder,String iInterfaceName,boolean isStub,InvocationHandler invocationHandler){
            this.mBase = binder;
            this.mInvocationHandler = invocationHandler;
            try {
                iInterface = Class.forName(iInterfaceName);
                mStub = Class.forName(String.format("%s%s",iInterfaceName,isStub?"$Stub":""));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if(method.getName().equals("queryLocalInterface")){

            }
            return method.invoke(mBase,args);
        }
    }
}
