package com.kenzz.reader.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.kenzz.reader.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
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

public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener {
    static final String TAG=Banner.class.getSimpleName();
    private static final int AUTODURATION=900;
    public static final int TEXT_INDICATOR=1;
    public static final int DOT_INDICATOR=2;
    private static final int MAX_COUNT=Integer.MAX_VALUE;

    @IntDef({TEXT_INDICATOR,DOT_INDICATOR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IndicatorMode{}
    private int mIndicatorStyle=DOT_INDICATOR;
    private boolean isAutoPlay=true;
    private int currentPageIndex;

    private Handler mHandler;

    private ViewPager mViewPager;
    private TextView  mIndicatorText;
    private LinearLayout mIndicatorLayout;
    private int dotSelectedColor;
    private int dotNormalColor;
    private float dotDistance;

    List<Object> bannerDatas=new ArrayList<>();
    private IBannerDataViewLoader mBannerDataViewLoader;


    static interface IBannerDataViewLoader{
        View loadView(ViewGroup parent,Object bannerData);
        void dataBind(View view,Object bannerData);
    }

    class DotView extends View{

        private int radius;
        private int selectColor;
        private int normalColor;
        private Paint paint;

        public DotView(Context context) {
            super(context);
            init(context);
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            invalidate();
        }

        private void init(Context context) {
            selectColor = Color.RED;
            normalColor = Color.DKGRAY;
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setDither(true);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
        }

        public void setSelectColor(@ColorInt int selectColor){
            this.selectColor = selectColor;
        }
        public void setNormalColor(@ColorInt int normalColor){
            this.normalColor = normalColor;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paint.setColor(isSelected()?selectColor:normalColor);
            canvas.drawCircle(radius,radius,radius,paint);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
           int widthMode= MeasureSpec.getMode(widthMeasureSpec);
           int widthSize= MeasureSpec.getSize(widthMeasureSpec);
           int heightMode= MeasureSpec.getMode(heightMeasureSpec);
           int heightSize= MeasureSpec.getSize(heightMeasureSpec);
           if(widthMode==MeasureSpec.AT_MOST && heightMode==MeasureSpec.AT_MOST){
               widthSize = heightSize=dp2px(getContext(),8);
               setMeasuredDimension(widthSize,heightSize);
               radius = widthSize/2;
           }else {
               int size = Math.max(widthSize,heightSize);
               setMeasuredDimension(size,size);
               radius = size/2;
           }
        }
    }

    static class BannerViewPager extends ViewPager{

        public BannerViewPager(Context context) {
            super(context);
            try {
              Field field = ViewPager.class.getDeclaredField("mScroller");
              field.setAccessible(true);
              field.set(this,new BannerScroller(context));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        class BannerScroller extends Scroller{
            static final int DURATION=1000;
            public BannerScroller(Context context) {
                super(context);
            }
            @Override
            public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                super.startScroll(startX, startY, dx, dy, DURATION);
            }
        }
    }

    public void setBannerDatas(List<Object> bannerDatas,boolean keepLastPosition){

        if(bannerDatas==null)return;
        this.bannerDatas.clear();
        this.bannerDatas.addAll(bannerDatas);
        mAdapter.notifyDataSetChanged();
        mIndicatorText.setText(String.format("%1s/%2s","1",bannerDatas.size()));
        mIndicatorLayout.removeAllViews();
        final int count=bannerDatas.size();
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.rightMargin= (int) dotDistance;
        for(int i=0;i<count;i++){
            DotView view=new DotView(getContext());
            view.setLayoutParams(lp);
            view.normalColor = dotNormalColor;
            view.selectColor = dotSelectedColor;
            mIndicatorLayout.addView(view);
            if(i==0){
                view.setSelected(true);
            }
        }
        if(!keepLastPosition)
        currentPageIndex=MAX_COUNT/2;
        mViewPager.setCurrentItem(currentPageIndex,true);
        stopPlay();
        if(isAutoPlay){
            startPlay();
        }
    }
    public void setDataAndLoader(List<Object> bannerDatas,IBannerDataViewLoader loader){
        setBannerDatas(bannerDatas,true);
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

    public void setIndicatorStyle(@IndicatorMode int indicatorStyle){
        this.mIndicatorStyle = indicatorStyle;
        if(mIndicatorStyle==DOT_INDICATOR){
            mIndicatorLayout.setVisibility(VISIBLE);
            mIndicatorText.setVisibility(GONE);
        }else {
            mIndicatorLayout.setVisibility(GONE);
            mIndicatorText.setVisibility(VISIBLE);
        }
    }

    public int getIndicatorStyle(){return mIndicatorStyle;}

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

     TypedArray a = context.getResources().obtainAttributes(attrs,R.styleable.Banner);
     isAutoPlay = a.getBoolean(R.styleable.Banner_AutoPlay,true);
     dotNormalColor = a.getColor(R.styleable.Banner_DotNormalColor,Color.GRAY);
     dotSelectedColor = a.getColor(R.styleable.Banner_DotSelectedColor,Color.RED);
     mIndicatorStyle = a.getInt(R.styleable.Banner_IndicatorStyle,DOT_INDICATOR);
     dotDistance = a.getDimension(R.styleable.Banner_DotIndicatorDistance,dp2px(context,5));
     a.recycle();

     mViewPager = new BannerViewPager(context);
     mViewPager.setOffscreenPageLimit(2);
     mViewPager.addOnPageChangeListener(this);
     mViewPager.setAdapter(mAdapter);

     mIndicatorLayout = new LinearLayout(context);
     mIndicatorLayout.setOrientation(LinearLayout.HORIZONTAL);

     mIndicatorText = new TextView(context);

     removeAllViews();
     addView(mViewPager,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
     LayoutParams lp=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
     lp.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
     lp.bottomMargin = dp2px(context,10);
     addView(mIndicatorLayout,lp);
     addView(mIndicatorText,lp);
     mHandler = new BannerHandler();
     setIndicatorStyle(mIndicatorStyle);
     currentPageIndex=MAX_COUNT/2;
    }

    private class BannerHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mViewPager.setCurrentItem(currentPageIndex++,true);
            mHandler.sendEmptyMessageDelayed(1,1500);
        }
    }

    private void triggerIndicator() {
      int position=convertViewPagerPositionToReal(currentPageIndex);
      mIndicatorText.setText(String.format("%1s / %2s",position+1,bannerDatas.size()));
      final int childCount = mIndicatorLayout.getChildCount();
      for(int i=0;i<childCount;i++){
       View child=mIndicatorLayout.getChildAt(i);
       if(i==position){
           child.setSelected(true);
       }else {
           child.setSelected(false);
       }
      }
    }

    public void startPlay(){
        mHandler.sendEmptyMessageDelayed(1,1500);
       // isAutoPlay = true;
    }

    public void stopPlay(){
        mHandler.removeMessages(1);
       // isAutoPlay = false;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_MOVE){
            if(isAutoPlay)
                stopPlay();
        }
        if(ev.getAction()==MotionEvent.ACTION_UP){
            if(isAutoPlay){
                startPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopPlay();
        mHandler.removeCallbacksAndMessages(null);
    }


    private PagerAdapter mAdapter=new PagerAdapter() {
        @Override
        public int getCount() {
            if(bannerDatas!=null && bannerDatas.size()>0){
                return MAX_COUNT;
            }
            return 0;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position=convertViewPagerPositionToReal(position);
            View view=mBannerDataViewLoader.loadView(container,bannerDatas.get(position));
            mBannerDataViewLoader.dataBind(view,bannerDatas.get(position));
            try {
                //某些时候在Fragment中使用会出现View没有remove的情况
                if(view.getParent()!=null){
                    ((ViewGroup)view.getParent()).removeView(view);
                }
                container.addView(view);
            }catch (Exception e){
                e.printStackTrace();
            }
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    };

    private int convertViewPagerPositionToReal(int position){
        boolean a=position<MAX_COUNT/2;
        position =Math.abs(position-MAX_COUNT/2);
        position =position%bannerDatas.size();
        if(a && position!=0){
            position = bannerDatas.size()-position;
        }
        return position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      currentPageIndex = position;
      triggerIndicator();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private int dp2px(Context context,float dpValue)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float scale = displayMetrics.density;
        return (int) (scale*dpValue+.5f);
    }

}
