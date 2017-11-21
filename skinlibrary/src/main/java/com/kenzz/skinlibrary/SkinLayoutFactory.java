package com.kenzz.skinlibrary;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by huangdefa on 11/11/2017.
 * Version 1.0
 */

public class SkinLayoutFactory implements LayoutInflater.Factory2 {
    private AppCompatDelegate mDelegate;

    public SkinLayoutFactory(AppCompatDelegate delegate){
        this.mDelegate = delegate;
    }

    public SkinLayoutFactory(){ }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = mDelegate.createView(parent, name, context, attrs);
        if(view instanceof SkinSupportable){
            //TODO Add to the skin view
        }

        return view;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return onCreateView(null,name,context,attrs);
    }

    public void setDelegate(AppCompatDelegate delegate){
       mDelegate = delegate;
    }
}
