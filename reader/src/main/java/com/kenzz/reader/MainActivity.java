package com.kenzz.reader;

import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kenzz.reader.activity.BaseActivity;
import com.kenzz.reader.utils.StatusBarUtil;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.nav)
    NavigationView mNavigationView;
    @BindView(R.id.dl_root)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setStatusBarColor(Color.BLUE /*getResources().getColor(R.color.colorPrimary)*/);
    }

    private void initView() {
        mNavigationView.inflateHeaderView(R.layout.nav_page);
        //StatusBarUtil.setColorNoTranslucentForDrawerLayout(this,mDrawerLayout,getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_main;
    }
}
