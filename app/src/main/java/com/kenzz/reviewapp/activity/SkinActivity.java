package com.kenzz.reviewapp.activity;

import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kenzz.reviewapp.R;
import com.kenzz.reviewapp.skinDemo.SkinCompatInflaterFactory;

public class SkinActivity extends AppCompatActivity {

    private SkinCompatInflaterFactory mInflaterFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInflaterFactory = new SkinCompatInflaterFactory();
        //必须在调用super.onCreate方法前设置，因为在该方法中会设置LayoutInflater的Factory，
        //而Factory只能设置一次。
        LayoutInflaterCompat.setFactory2(getLayoutInflater(),mInflaterFactory);
        super.onCreate(savedInstanceState);
        //必须调用SkinCompatInflaterFactory.installDelegate方法
        mInflaterFactory.installDelegate(this,getDelegate());
        setContentView(R.layout.activity_skin);
    }
}
