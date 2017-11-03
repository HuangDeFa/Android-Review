package com.kenzz.skinlibrary.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ken.huang on 11/3/2017.
 * version: 1.0
 * author:ken
 * description: 换肤的Activity基类
 */

public abstract class SkinBaseActivity extends AppCompatActivity {
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }
}
