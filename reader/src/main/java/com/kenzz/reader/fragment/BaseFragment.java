package com.kenzz.reader.fragment;


import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kenzz.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    protected Unbinder butterKnife;
    private RelativeLayout rootView;
    private FrameLayout viewContainer;

    @BindView(R.id.ll_fragment_content_error)
    LinearLayout errorLayout;

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
        initView();
        return rootView;
    }

    @OnClick({R.id.tv_fragment_content_error})
    public void onErrorClick(View view){
        onErrorRefresh();
    }

    protected  void onErrorRefresh(){}

    public void showErrorPage(){
        errorLayout.setVisibility(View.VISIBLE);
        viewContainer.setVisibility(View.GONE);
    }

    public void hideErrorPage(){
        errorLayout.setVisibility(View.GONE);
        viewContainer.setVisibility(View.VISIBLE);
    }

    protected boolean isLazyLoaded;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(getUserVisibleHint()){
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
    }

    public abstract void initView();

    public abstract int getContentId();

}
