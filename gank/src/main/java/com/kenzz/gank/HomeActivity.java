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
import com.kenzz.gank.fragment.GirlFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @InjectView(R.id.home_bottom_nav)
    BottomNavigationView mBottomNavigationView;
    @InjectView(R.id.home_head_bar)
    RelativeLayout mHeadContainer;
    @InjectView(R.id.home_head_title)
    TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setFullScreen();
        ButterKnife.inject(this);
        initView();
        initFragment();
    }

    private void initView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        int statusBarHeight = getStatusBarHeight();
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)
                mHeadContainer.getLayoutParams();
        lp.topMargin+=statusBarHeight;
        mHeadContainer.setLayoutParams(lp);
        mTextView.setText("妹子");
    }

    private int mCurrentIndex=-1;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int id=item.getItemId();
        if(id==mCurrentIndex) return true;
        switch (id){
            case R.id.home_bottom_nav_1:
                break;
            case R.id.home_bottom_nav_2:
                break;
            case R.id.home_bottom_nav_3:
                break;
        }
        mCurrentIndex=id;
        changeFragment(mCurrentIndex);
        return true;
    }

    private GirlFragment mGirlFragment;
    private void initFragment(){
        mGirlFragment=new GirlFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.home_main_content,mGirlFragment,"Girl")
                .commit();
    }

    private void changeFragment(int index){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.commit();
    }

}
