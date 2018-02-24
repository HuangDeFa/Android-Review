package com.kenzz.reader.fragment;


import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kenzz.reader.BuildConfig;
import com.kenzz.reader.MyApplication;
import com.kenzz.reader.R;
import com.kenzz.reader.utils.ACache;
import com.kenzz.reader.utils.NetWorkUtil;
import com.kenzz.reader.utils.ToastUtil;

import java.io.File;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements NetWorkUtil.onNetWorkChangeListener{

    protected Unbinder butterKnife;
    private RelativeLayout rootView;
    private FrameLayout viewContainer;
    protected CompositeDisposable mCompositeDisposable=new CompositeDisposable();
    @BindView(R.id.ll_fragment_content_error)
    LinearLayout errorLayout;
    @BindView(R.id.ll_fragment_content_loading)
    LinearLayout loadingLayout;
    private ImageView loadingImageVew;
    @BindView(R.id.tv_fragment_network_error)
    TextView netWorkError;
    public BaseFragment() {
        // Required empty public constructor
    }


    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_base_layout,container,false);
        viewContainer=rootView.findViewById(R.id.fragment_content);
        View view=inflater.inflate(getContentId(),container,false);
        viewContainer.removeAllViews();
        viewContainer.addView(view,new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        butterKnife = ButterKnife.bind(this,rootView);
        loadingImageVew = (ImageView) loadingLayout.getChildAt(0);
        initView();
        return rootView;
    }

    @OnClick({R.id.tv_fragment_content_error})
    public final void onErrorClick(View view){
        onErrorRefresh();
    }

    @CallSuper
    protected  void onErrorRefresh(){
         showLoadingPage();
    }

    public void showErrorPage(){
        errorLayout.setVisibility(View.VISIBLE);
        viewContainer.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
    }

    public void hideErrorPage(){
        errorLayout.setVisibility(View.GONE);
        viewContainer.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
    }

    public void showLoadingPage(){
        errorLayout.setVisibility(View.GONE);
        viewContainer.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        AnimationDrawable drawable = (AnimationDrawable) loadingImageVew.getDrawable();
        drawable.start();
    }



    public void hideLoadingPage(){
        errorLayout.setVisibility(View.GONE);
        viewContainer.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        AnimationDrawable drawable = (AnimationDrawable) loadingImageVew.getDrawable();
        drawable.stop();
    }

    protected boolean isLazyLoaded;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            onVisible();
            if(!isLazyLoaded){
                onLazyLoad();
                isLazyLoaded=true;
            }
        }else {
            onInVisible();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected  void onInVisible(){

    }

    protected  void onLazyLoad(){}

    protected  void onVisible(){}

    protected void addDisable(Disposable disposable){
        mCompositeDisposable.add(disposable);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
        mCompositeDisposable.dispose();
    }

    public abstract void initView();

    public abstract int getContentId();

    @Override
    public void onConnected() {
        netWorkError.animate()
                .alpha(1)
                .setDuration(600)
                .withEndAction(()->
                    netWorkError.setVisibility(View.GONE))
                .start();
    }

    @Override
    public void onDisConnected() {
        netWorkError.setText("网络不可用");
        netWorkError.setVisibility(View.VISIBLE);
        netWorkError.setAlpha(0);
        netWorkError.animate()
                    .alpha(1)
                    .setDuration(600)
                    .start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        NetWorkUtil.unregistNetWorkListener(this);
    }

    public final boolean checkNetConnected(){
        return NetWorkUtil.isNetworkAvailable(MyApplication.getInstance());
    }

    private ACache getCache(){
       return ACache.get(new File(MyApplication.getInstance()
               .getExternalCacheDir(),"readerCache"));
    }

    public <T> T getDataFromCache(String key){
        return (T) getCache().getAsObject(key);
    }
    public <T> T putDataToCache(String key, Serializable data,int saveTime){
        getCache().put(key,data,saveTime);
        return (T) data;
    }
}
