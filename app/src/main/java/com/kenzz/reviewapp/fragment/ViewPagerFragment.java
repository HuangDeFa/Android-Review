package com.kenzz.reviewapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kenzz.reviewapp.R;

/**
 * A simple {@link Fragment} subclass.
 * 适用于ViewPager的Fragment 基类
 * 主要用于控制数据的懒加载，避免viewpager的缓存机制导致重复的数据加载
 */
public abstract class ViewPagerFragment extends Fragment {


    public ViewPagerFragment() {
        // Required empty public constructor
    }

    protected View rootView;

    protected boolean hasCreateView;
    protected boolean isFragmentVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }

    public void initVariable() {
        hasCreateView=false;
        isFragmentVisible=false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(rootView==null){
            return;
        }
        hasCreateView=true;
        if(isVisibleToUser){
            onFragmentVisible(true);
            isFragmentVisible=true;
            return;
        }

        if(isFragmentVisible){
            onFragmentVisible(false);
            isFragmentVisible=false;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
               rootView=createView(inflater,container,savedInstanceState);
               if(!hasCreateView && getUserVisibleHint()){
                   onFragmentVisible(true);
                   isFragmentVisible=true;
               }
        return rootView;
    }


    public abstract View createView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState);

    public abstract void onFragmentVisible(boolean isVisible);

}
