package com.kenzz.skinlibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.kenzz.skinlibrary.SkinLayoutFactory;

/**
 * Created by ken.huang on 11/3/2017.
 * version: 1.0
 * author:ken
 * description: 换肤的Activity基类
 */

public abstract class SkinBaseActivity extends AppCompatActivity {

    private SkinLayoutFactory mSkinLayoutFactory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSkinLayoutFactory = new SkinLayoutFactory();
        LayoutInflaterCompat.setFactory2(getLayoutInflater(),mSkinLayoutFactory);
        super.onCreate(savedInstanceState);
        mSkinLayoutFactory.setDelegate(getDelegate());
    }

}
