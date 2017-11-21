package com.kenzz.reviewapp.skinDemo;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by huangdefa on 16/11/2017.
 * Version 1.0
 * 利用LayoutInflater.Factory 的方式进行View的收集和管理。
 * 原理：LayoutInflater在进行View的创建时会判断是否设置了Factory，设置了
 * 就调用Factory的onCreateView方法。因此可以在onCreateView方式中进行拦截操作。
 * 系统的AppCompatActivity 中使用的Delegate的方式，委托给不同的Delegate，其中Delegate实现咯Factory
 * 方法，并将自身设置到LayoutInflater去。
 */

public class SkinCompatInflaterFactory implements LayoutInflater.Factory2 {
    private static final String TAG=SkinCompatInflaterFactory.class.getSimpleName();
    private AppCompatDelegate mDelegate;
    private Context mContext;
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
         View  view = mDelegate.createView(parent,name,context,attrs);
        int count = attrs.getAttributeCount();
        for (int i=0;i<count;i++){
            String attrName = attrs.getAttributeName(i);
            String attrValue =  attrs.getAttributeValue(i);
            if(attrName.equals("textColor")){

            }
            Log.d(TAG,"Name: "+attrName+" Value: "+attrValue);
        }
        return view;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return onCreateView(null,name,context,attrs);
    }

    /**
     * 必须调用，用以调用系统的Delegate进行View的创建
     * @param context
     * @param delegate
     */
    public void installDelegate(Activity context, AppCompatDelegate delegate){
        this.mDelegate = delegate;
        mContext = context.getApplicationContext();
    }
}
