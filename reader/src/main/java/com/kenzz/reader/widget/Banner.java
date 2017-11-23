package com.kenzz.reader.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangdefa on 24/11/2017.
 * Version 1.0
 * 显示图片的通用Banner
 * 1.添加ViewPager
 * 2.使用handler自动切换
 * 3.使用接口引入图片加载器 eg:Glide/Picasso
 */

public class Banner extends FrameLayout {
    static final String TAG=Banner.class.getSimpleName();
    private static final int AUTODURATION=900;
    private Handler mHandler;

    private ViewPager mViewPager;
    private TextView  mIndicatorText;
    private LinearLayout mIndicatorLayout;

    List<Object> bannerDatas=new ArrayList<>();
    private IBannerDataViewLoader mBannerDataViewLoader;

    static interface IBannerDataViewLoader{
        View loadView(Object bannerData);
    }

    public void setBannerDatas(List<Object> bannerDatas){
        this.bannerDatas = bannerDatas;
    }
    public void setDataAndLoader(List<Object> bannerDatas,IBannerDataViewLoader loader){
        setBannerDatas(bannerDatas);
        mBannerDataViewLoader = loader;
    }

    public Banner(@NonNull Context context) {
        this(context,null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
     mViewPager = new ViewPager(context);
     mIndicatorLayout = new LinearLayout(context);
     mIndicatorText = new TextView(context);
     removeAllViews();
     addView(mViewPager,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
     LayoutParams lp=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
     lp.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
     lp.bottomMargin = dp2px(context,10);
     addView(mIndicatorLayout,lp);
     addView(mIndicatorText,lp);
     mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }


    private PagerAdapter mAdapter=new PagerAdapter() {
        @Override
        public int getCount() {
            return bannerDatas.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    };

    private int dp2px(Context context,float dpValue)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float scale = displayMetrics.density;
        return (int) (scale*dpValue+.5f);
    }

}
