package com.kenzz.gank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kenzz.gank.activity.BaseActivity;
import com.kenzz.gank.activity.ToolbarBaseActivity;
import com.kenzz.gank.fragment.AboutFragment;
import com.kenzz.gank.fragment.GankFragment;
import com.kenzz.gank.fragment.GirlFragment;

import butterknife.ButterKnife;
import butterknife.BindView;

public class HomeActivity extends ToolbarBaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.home_bottom_nav)
    BottomNavigationView mBottomNavigationView;
    //@BindView(R.id.home_head_bar)
   //  RelativeLayout mHeadContainer;
   // @BindView(R.id.home_head_title)
   // TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* setContentView(R.layout.activity_home);
        setFullScreen();
        ButterKnife.bind(this);*/
        initView();
        initFragment();
    }

    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    protected boolean isNeedButterKnife() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_home;
    }

    private void initView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
       /* int statusBarHeight = getStatusBarHeight();
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)
                mHeadContainer.getLayoutParams();
        lp.topMargin+=statusBarHeight;
        mHeadContainer.setLayoutParams(lp);*/
        mToolbar.setTitle("妹子");
    }

    private int mCurrentIndex=-1;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int id=item.getItemId();
        if(id==mCurrentIndex) return true;
        switch (id){
            case R.id.home_bottom_nav_1:
                mToolbar.setTitle("妹子");
                mCurrentIndex = 0;
                break;
            case R.id.home_bottom_nav_2:
                mToolbar.setTitle("GitHub");
                mCurrentIndex = 1;
                break;
            case R.id.home_bottom_nav_3:
                mToolbar.setTitle("其他");
                mCurrentIndex = 2;
                break;
        }
        changeFragment(mCurrentIndex);
        return true;
    }

    private GirlFragment mGirlFragment;
    private GankFragment mGankFragment;
    private AboutFragment mAboutFragment;

    private void initFragment(){
        mGirlFragment=new GirlFragment();
        mGankFragment=new GankFragment();
        mAboutFragment=new AboutFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.home_main_content,mGirlFragment,"Girl")
                .commit();
    }

    private void changeFragment(int index){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(!mGankFragment.isAdded())transaction.add(R.id.home_main_content,mGankFragment,"Gank");
        if(!mAboutFragment.isAdded()) transaction.add(R.id.home_main_content,mAboutFragment,"about");
        transaction.hide(mGankFragment).hide(mGirlFragment).hide(mAboutFragment);
        switch (index)
        {
            case 0:
                transaction.show(mGirlFragment);
                break;
            case 1:
                transaction.show(mGankFragment);
                break;
            case 2:
                transaction.show(mAboutFragment);
                break;
        }
        transaction.commit();
    }

}
