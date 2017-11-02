package com.kenzz.gank.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.kenzz.gank.MyApplication;
import com.kenzz.gank.R;

import butterknife.BindView;
import butterknife.Optional;

/**
 * Created by ken.huang on 11/2/2017.
 * 带有默认ToolBar的Activity
 */

public abstract class ToolbarBaseActivity extends BaseActivity {

    @BindView(R.id.title_bar_tool)
    public Toolbar mToolbar;
    @Nullable
    @BindView(R.id.home_head_container)
    public AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        initToolBar();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        if (canGoBack()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @CallSuper
    protected void initToolBar() {
        setFullScreen();
        final int statusBarHeight = MyApplication.getInstance().StatusBarHeight;
        mAppBarLayout.setPadding(0,statusBarHeight,0,0);
    }

    protected abstract boolean canGoBack();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (canGoBack()) {
            if (item.getItemId() == android.R.id.home) {
                onBackPressed();
            }
        }
        return false;
    }
}
