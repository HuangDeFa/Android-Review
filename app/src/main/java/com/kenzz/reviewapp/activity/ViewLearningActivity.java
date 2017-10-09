package com.kenzz.reviewapp.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.kenzz.reviewapp.R;
import com.kenzz.reviewapp.widget.PizzaView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ViewLearningActivity extends BaseActivity {

    @InjectView(R.id.view_my_viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.view_my_tabLayout)
    TabLayout mTabLayout;

    private List<View> mViewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_learning);
        ButterKnife.inject(this);
        initData();
        initView();
    }

    private void initView() {
        mTabLayout.addTab(mTabLayout.newTab().setText("绘制方形图"));
        mTabLayout.addTab(mTabLayout.newTab().setText("绘制圆饼图"));
        mTabLayout.addTab(mTabLayout.newTab().setText("绘制折线图"));
        mTabLayout.addTab(mTabLayout.newTab().setText("绘制基础图"));
        mTabLayout.addTab(mTabLayout.newTab().setText("绘制方形图1"));
        mTabLayout.addTab(mTabLayout.newTab().setText("绘制方形图2"));
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mViewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return mViewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }



    private void initData() {
     mViewList = new ArrayList<>();
     for(int i=0;i<6;i++){
         View view = new PizzaView(this);
         mViewList.add(view);
     }
    }
}
