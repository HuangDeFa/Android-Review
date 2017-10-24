package com.kenzz.gank.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenzz.gank.R;
import com.kenzz.gank.bean.GankEntity;
import com.kenzz.gank.net.ApiManager;
import com.kenzz.gank.net.IApiCallBack;

/**
 * A simple {@link Fragment} subclass.
 */
public class GirlFragment extends Fragment {

    private static final String TAG=GirlFragment.class.getSimpleName();
    public GirlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_girl, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getUserVisibleHint()){
            ApiManager.getInstance().getDataByCategory(ApiManager.WELFARE_TYPE, 1, new IApiCallBack<GankEntity>() {
                @Override
                public void onError(Throwable throwable) {
                    Log.d(TAG, throwable.getMessage());
                }

                @Override
                public void onSuccess(GankEntity data) {
                    Log.d(TAG, data.toString());
                }
            });
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, isVisibleToUser + "");
        if (isVisibleToUser) {
            ApiManager.getInstance().getDataByCategory(ApiManager.WELFARE_TYPE, 1, new IApiCallBack<GankEntity>() {
                @Override
                public void onError(Throwable throwable) {
                    Log.d(TAG, throwable.getMessage());
                }

                @Override
                public void onSuccess(GankEntity data) {
                    Log.d(TAG, data.toString());
                }
            });
        }
    }
}
