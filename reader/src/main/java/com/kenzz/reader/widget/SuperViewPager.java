package com.kenzz.reader.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by ken.huang on 11/23/2017.
 */

public class SuperViewPager extends ViewPager {
    private SuperScroller mScroller;

    public SuperViewPager(Context context) {
        this(context, null);
    }

    public SuperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new SuperScroller(context, 900);
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(this, mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // 在使用PhotoView会发生错误，这里可以try-catch 处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //uncomment if you really want to see these errors
            //e.printStackTrace();
            return false;
        }

    }

    static class SuperScroller extends Scroller {
        static final int DEFAULT_DURATION = 600;
        private int mDuration = DEFAULT_DURATION;

        public SuperScroller(Context context, int customDuration) {
            super(context);
            if (customDuration > 0) this.mDuration = customDuration;
        }

        public void setDuration(int duration) {
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, this.mDuration);
        }
    }
}
