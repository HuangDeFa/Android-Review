package com.kenzz.reader.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.kenzz.reader.R;
import com.kenzz.reader.activity.WelfareActivity;
import com.kenzz.reader.adapter.WelfareAdapter;
import com.kenzz.reader.bean.GankEntity;
import com.kenzz.reader.http.ApiManager;
import com.kenzz.reader.http.GankService;
import com.scwang.smartrefresh.header.CircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelfareFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {


    public WelfareFragment() {
        // Required empty public constructor
    }
    @BindView(R.id.smartRL_gank_welfare)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_gank_welfare)
    RecyclerView mRecyclerView;


    private List<GankEntity.ResultsBean> dataList=new ArrayList<>();
    private int pageIndex=1;
    private WelfareAdapter mWelfareAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mWelfareAdapter = new WelfareAdapter();
        mRecyclerView.setAdapter(mWelfareAdapter);
        mRefreshLayout.
                setRefreshHeader(new CircleHeader(getContext()))
                .setEnableRefresh(true)
                .setEnableAutoLoadmore(true)
                .setOnRefreshListener(this)
                .setOnLoadmoreListener(this);
        if(dataList.size()==0){
            if(checkNetConnected())
            showLoadingPage();
            else
                showErrorPage();
        }else {
            mWelfareAdapter.updateDataList(dataList);
        }
        mWelfareAdapter.setWelfareListener((view, position) -> {
            ArrayList<Map.Entry<String,String>> urls=new ArrayList<>();
            for (GankEntity.ResultsBean resultsBean : dataList) {
                urls.add(new AbstractMap.SimpleEntry<>(resultsBean.url,resultsBean.desc));
            }
            WelfareActivity.startActivity(getActivity(),urls,position);
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    Glide.with(getContext()).resumeRequests();
                }else {
                    Glide.with(getContext()).pauseRequests();
                }
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
        if(!checkNetConnected()) return;
        loadData();
    }

    private void loadData(){
        ApiManager.getInstance()
                .getService(GankService.class)
                .getGankDayByPage("福利",pageIndex)
                .subscribeOn(Schedulers.io())
                .map(x->x.results)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> addDisable(disposable))
                .doOnError(error->{
                    error.printStackTrace();
                    showErrorPage();
                })
                .subscribe(result->{
                    if(mRefreshLayout!=null){
                        hideLoadingPage();
                        if(mRefreshLayout.isRefreshing()){
                            mRefreshLayout.finishRefresh();
                        }
                        if(mRefreshLayout.isLoading()){
                            mRefreshLayout.finishLoadmore();
                        }
                    }
                    if(pageIndex==1)dataList.clear();
                    dataList.addAll(result);
                    mWelfareAdapter.updateDataList(dataList);
                });
    }

    @Override
    protected void onErrorRefresh() {
        if(!checkNetConnected())return;
        super.onErrorRefresh();
        loadData();
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_welfare;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if(!checkNetConnected()){
            mRefreshLayout.finishRefresh();
            return;
        }
        pageIndex=1;
        loadData();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if(!checkNetConnected()){
            mRefreshLayout.finishLoadmore();
            return;
        }
        ++pageIndex;
        loadData();
    }
}
