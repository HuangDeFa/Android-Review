package com.kenzz.gank.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kenzz.gank.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {

    protected CompositeDisposable mCompositeDisposable;
    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewId());
        mCompositeDisposable = new CompositeDisposable();
        if(isNeedButterKnife()){
            mUnbinder = ButterKnife.bind(this);
        }
    }

    protected abstract boolean isNeedButterKnife();

    /**
     * 使用Rxjava 时将disposable对象收集起来便于管理释放
     * @param disposable
     */
    protected void addDisableResource(Disposable disposable){
        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCompositeDisposable!=null){
            mCompositeDisposable.dispose();
        }
        if(mUnbinder!=null){
            mUnbinder.unbind();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    protected abstract int getContentViewId();

    protected int getStatusBarHeight(){
        int statusBarHeight=0;
        int resId=getResources().getIdentifier("status_bar_height","dimen","android");
        if(resId!=0){
            statusBarHeight = (int) getResources().getDimension(resId);
        }
        return statusBarHeight;
    }

    protected void setFullScreen(){
        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}
