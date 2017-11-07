package com.kenzz.gank.activity;

import android.os.Bundle;

import com.kenzz.gank.R;

public class GankInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
    }

    @Override
    protected boolean isNeedButterKnife() {
        return false;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_gank_info;
    }
}
