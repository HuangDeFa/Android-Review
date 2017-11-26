package com.kenzz.reader.fragment;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kenzz.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    protected Unbinder butterKnife;
    private RelativeLayout rootView;
    private FrameLayout viewContainer;
    protected CompositeDisposable mCompositeDisposable=new CompositeDisposable();
    @BindView(R.id.ll_fragment_content_error)
    LinearLayout errorLayout;
    @BindView(R.id.ll_fragment_content_loading)
    LinearLayout loadingLayout;
    private ImageView loadingImageVew;
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

}
