package com.kenzz.reader.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    Unbinder butterKnife;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getViewId());
        butterKnife = ButterKnife.bind(this);
    }

    protected abstract int getViewId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (butterKnife != null) butterKnife.unbind();
    }

    /**
     * 覆盖在状态栏的装饰View
     */
    static class StatusBarView extends View {

        public StatusBarView(Context context) {
            super(context);
        }

        public StatusBarView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }
    }

    /**
     * 设置状态栏的颜色
     * 注意：如果根部局是DrawerLayout 必须在布局文件设置fitSystemWindows=true;
     * @param statusBarColor
     */
    public void setStatusBarColor(@ColorInt int statusBarColor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar();
        View decorView = getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        View firstChild = rootView.getChildAt(0);
        if (firstChild instanceof DrawerLayout) {
            ViewGroup contentRoot = (ViewGroup) ((ViewGroup) firstChild).getChildAt(0);
            if (contentRoot.getChildAt(0) instanceof StatusBarView) {
                contentRoot.getChildAt(0).setBackgroundColor(statusBarColor);
            } else {
                contentRoot.addView(createStatusBarView(this, statusBarColor), 0);
                if (!(contentRoot instanceof LinearLayout) && contentRoot.getChildCount() > 1) {
                    View view = contentRoot.getChildAt(1);
                    view.setPadding(view.getPaddingLeft(),
                            getStatusBarHeight(this),
                            view.getRight(), view.getBottom());
                }
            }
            //contentRoot.setFitsSystemWindows(false);
            //contentRoot.setClipToPadding(true);
             firstChild.setFitsSystemWindows(false);
            ((ViewGroup)firstChild).getChildAt(1).setFitsSystemWindows(false);
        }else {
           if(((ViewGroup) firstChild).getChildAt(0) instanceof StatusBarView){
               ((ViewGroup) firstChild).getChildAt(0).setBackgroundColor(statusBarColor);
           }else {
               ((ViewGroup) firstChild).addView(createStatusBarView(this, statusBarColor), 0);
               if (!(firstChild instanceof LinearLayout) && ((ViewGroup)firstChild).getChildCount() > 1) {
                   View view =  ((ViewGroup) firstChild).getChildAt(1);
                   view.setPadding(view.getPaddingLeft(),
                           getStatusBarHeight(this),
                           view.getRight(), view.getBottom());
               }
           }
        }

    }

    /**
     * 状态栏透明，并且全屏显示
     */
    public void setFullScreen() {
        transparentStatusBar();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     * 状态栏透明
     */
    public void transparentStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public StatusBarView createStatusBarView(Context context, @ColorInt int color) {
        StatusBarView statusBarView = new StatusBarView(context);
        statusBarView.setBackgroundColor(color);
        statusBarView.setLayoutParams(new ViewGroup.MarginLayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(context)));
        return statusBarView;
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return (int) context.getResources().getDimension(identifier);
        }
        return 0;
    }
}
