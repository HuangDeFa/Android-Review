package com.kenzz.reviewapp.service;

import android.content.ClipData;
import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
                 return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),new Class[]{iInterface}
                         ,new ServiceProxyHandler(mBase,mStub,mInvocationHandler));
            }
            return method.invoke(mBase,args);
        }
    }

    static class ServiceProxyHandler implements InvocationHandler{

        IBinder mBase;
        InvocationHandler mInvocationHandler;
        ServiceProxyHandler(IBinder binder,Class stub,InvocationHandler invocationHandler){
            this.mInvocationHandler = invocationHandler;
            try {
                Method asInterface = stub.getDeclaredMethod("asInterface", IBinder.class);
                asInterface.setAccessible(true);
               mBase = (IBinder)asInterface.invoke(null,binder);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if(mInvocationHandler!=null){
               return mInvocationHandler.invoke(mBase,method,args);
            }
            return method.invoke(mBase,args);
        }
    }

    //具体的需要执行的Hook
    static class ClipboardHookHandler implements InvocationHandler{

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            int argsLength = args.length;
            //每次从本应用复制的文本，后面都加上分享的出处
            if ("setPrimaryClip".equals(methodName)) {
                if (argsLength >= 2 && args[0] instanceof ClipData) {
                    ClipData data = (ClipData) args[0];
                    String text = data.getItemAt(0).getText().toString();
                    text += "this is shared from ServiceHook-----by ken_zz";
                    args[0] = ClipData.newPlainText(data.getDescription().getLabel(), text);
                }
            }
            return method.invoke(proxy, args);
        }
    }

    public static void hookService(Context context) {
        IBinder clipboardService = ServiceManger.getService(Context.CLIPBOARD_SERVICE);
        String IClipboard = "android.content.IClipboard";

        if (clipboardService != null) {
            IBinder hookClipboardService =
                    (IBinder) Proxy.newProxyInstance(IBinder.class.getClassLoader(),
                            new Class[]{IBinder.class},
                            new ServiceHookHandler(clipboardService, IClipboard, true, new ClipboardHookHandler()));
            ServiceManger.setService(Context.CLIPBOARD_SERVICE, hookClipboardService);
        } else {
            Log.e("hookService", "ClipboardService hook failed!");
        }
    }


}
