package com.kenzz.reviewapp.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TableLayout;

import com.kenzz.reviewapp.R;
import com.kenzz.reviewapp.widget.AnimationView;
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
            public CharSequence getPageTitle(int position) {
                String title=null;
               switch (position){
                   case 0:
                       title = "绘制方形图";
                       break;
                   case 1:
                       title = "绘制圆饼图";
                       break;
                   case 2:
                       title = "绘制折线图";
                       break;
                   case 3:
                       title = "绘制基础图";
                       break;
                   case 4:
                       title = "属性动画";
                       break;
                   case 5:
                       title = "绘制方形图2";
                       break;
               }
               return title;
            }

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
                View view=mViewList.get(position);
                if(position==3){
                    ((PizzaView)view).setDrawType(PizzaView.DRAWBASE);
                }else if(position==4){
                    ((AnimationView)view).startAnimator();
                }
                container.addView(mViewList.get(position));
                return view;
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
         View view =i==4?new AnimationView(this):new PizzaView(this);
         mViewList.add(view);
     }
    }
}
