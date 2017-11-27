package com.kenzz.reader.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenzz.reader.R;
import com.kenzz.reader.bean.GankEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankIOFragment extends BaseFragment {


    public GankIOFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.smartRL_gank_io)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_gank_io)
    RecyclerView mRecyclerView;

    List<AbstractMap.SimpleEntry<String,List<GankEntity.ResultsBean>>> dataList=new ArrayList<>();
    String currentType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void initView() {
     mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
     mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public int getContentId() {
        return R.layout.fragment_gank_io;
    }

}
