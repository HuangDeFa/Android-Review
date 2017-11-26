package com.kenzz.reader;

import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import com.kenzz.reader.activity.BaseActivity;
import com.kenzz.reader.adapter.MyFragmentAdapter;
import com.kenzz.reader.fragment.DouFragment;
import com.kenzz.reader.fragment.GankFragment;
import com.kenzz.reader.fragment.OneFragment;
import com.kenzz.reader.utils.StatusBarUtil;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.nav)
    NavigationView mNavigationView;
    @BindView(R.id.dl_root)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.iv_title_one)
    ImageView ivOne;
    @BindView(R.id.iv_title_gank)
    ImageView ivGank;
    @BindView(R.id.iv_title_dou)
    ImageView ivDou;
    @BindView(R.id.tb_head)
    Toolbar mToolbar;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.vp_content)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(mToolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        initFragment();
        initView();
    }

    private void initView() {
        mNavigationView.inflateHeaderView(R.layout.nav_page);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(),mFragments));

        onClickEvent(ivGank);
    }

    List<Fragment> mFragments = new ArrayList<>(3);
    private void initFragment(){
        GankFragment gankFragment = new GankFragment();
        DouFragment douFragment=new DouFragment();
        OneFragment oneFragment = new OneFragment();
        mFragments.add(gankFragment);
        mFragments.add(douFragment);
        mFragments.add(oneFragment);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(mNavigationView)){
            mDrawerLayout.closeDrawer(mNavigationView);
            return;
        }
        //将任务栈移到后台，不退出应用
        moveTaskToBack(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.iv_title_dou,R.id.iv_title_gank,R.id.iv_title_one,R.id.iv_menu})
    public void onClickEvent(View view){
        if(view.getId()==R.id.iv_menu){
            mDrawerLayout.openDrawer(mNavigationView,true);
            return;
        }
        switchPage(view.getId());
        switchTab(view.getId());
    }

    private void switchPage(int id) {
        if(currentTabId==id)return;
        switch (id){
            case R.id.iv_title_dou:
                mViewPager.setCurrentItem(1,true);
                break;
            case R.id.iv_title_gank:
                mViewPager.setCurrentItem(0,true);
                break;
            case R.id.iv_title_one:
                mViewPager.setCurrentItem(2,true);
                break;
        }
    }

    int currentTabId;

    private void switchTab(int tabId ){
        if(currentTabId==tabId)return;
        switch (tabId){
            case R.id.iv_title_dou:
                ivDou.setSelected(true);
                ivGank.setSelected(false);
                ivOne.setSelected(false);
                break;
            case R.id.iv_title_gank:
                ivDou.setSelected(false);
                ivGank.setSelected(true);
                ivOne.setSelected(false);
                break;
            case R.id.iv_title_one:
                ivDou.setSelected(false);
                ivGank.setSelected(false);
                ivOne.setSelected(true);
                break;
        }
        currentTabId = tabId;
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    switchTab(R.id.iv_title_gank);
                    break;
                case 1:
                    switchTab(R.id.iv_title_dou);
                    break;
                case 2:
                    switchTab(R.id.iv_title_one);
                    break;
            }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
