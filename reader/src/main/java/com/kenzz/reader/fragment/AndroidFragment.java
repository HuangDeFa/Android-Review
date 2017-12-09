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
import com.kenzz.reader.activity.WebActivity;
import com.kenzz.reader.adapter.GankIOAdapter;
import com.kenzz.reader.bean.GankEntity;
import com.kenzz.reader.http.ApiManager;
import com.kenzz.reader.http.GankService;
import com.kenzz.reader.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class AndroidFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {


    public AndroidFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.smartRL_gank_android)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_gank_android)
    RecyclerView mRecyclerView;

    List<GankEntity.ResultsBean> dataList=new ArrayList<>();
    GankIOAdapter mAdapter;
    int currentPage=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void initView() {
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
      mRecyclerView.setItemAnimator(new DefaultItemAnimator());
      mAdapter=new GankIOAdapter(dataList);
      mRecyclerView.setAdapter(mAdapter);
      mRecyclerView.setVerticalScrollBarEnabled(true);
      mRecyclerView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
      mAdapter.setListener((view, position) -> {
          GankEntity.ResultsBean data = dataList.get(position);
          WebActivity.startActivity(getActivity(),data.url,data.desc);
      });
      if(dataList.size()==0){
          if(checkNetConnected())
          showLoadingPage();
          else showErrorPage();
      }
      mRefreshLayout.setOnRefreshListener(this);
      mRefreshLayout.setOnLoadmoreListener(this);
    }

    @Override
    protected void onLazyLoad() {
       if(checkNetConnected())
           loadData();
    }

    private void loadData(){
        ApiManager.getInstance().getService(GankService.class)
                .getGankDayByPage("Android",currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::addDisable)
                .doOnError(x->{
                    if(mRecyclerView!=null) {
                        showErrorPage();
                        mRefreshLayout.finishLoadmore();
                        mRefreshLayout.finishRefresh();
                    }
                    ToastUtil.showShortToast(getContext(),x.getMessage());
                })
                .doOnComplete(()->{
                 ToastUtil.showShortToast(getContext(),"Complete");
                })
                .subscribe(x->{
                    if(x.error){
                        if(mRecyclerView!=null) {
                            showErrorPage();
                            mRefreshLayout.finishLoadmore();
                            mRefreshLayout.finishRefresh();
                        }
                        ToastUtil.showShortToast(getContext(),"net work error!");
                    }else{
                        if(currentPage==1){
                            dataList.clear();
                        }
                        dataList.addAll(x.results);
                        if(mRecyclerView!=null){
                            mAdapter.notifyDataSetChanged();
                            mRefreshLayout.finishLoadmore();
                            mRefreshLayout.finishRefresh();
                            hideLoadingPage();
                        }
                    }
                });
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_android;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if(!checkNetConnected()){
            mRefreshLayout.finishRefresh();
            return;
        }
        currentPage = 1;
        loadData();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if(!checkNetConnected()){
            mRefreshLayout.finishLoadmore();
            return;
        }
        ++currentPage;
        loadData();
    }

    @Override
    protected void onErrorRefresh() {
        if(!checkNetConnected())return;
        super.onErrorRefresh();
        loadData();
    }
}
