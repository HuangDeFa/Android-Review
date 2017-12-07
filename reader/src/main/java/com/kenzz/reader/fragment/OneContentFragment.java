package com.kenzz.reader.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenzz.reader.R;
import com.kenzz.reader.activity.OneBookActivity;
import com.kenzz.reader.adapter.OneContentAdapter;
import com.kenzz.reader.bean.OneBookEntity;
import com.kenzz.reader.bean.OneBookViewModel;
import com.kenzz.reader.http.ApiManager;
import com.kenzz.reader.http.DouService;
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
public class OneContentFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {


    private static final String CONTENT_TYPE="type";

    public OneContentFragment(){}

    public static OneContentFragment newInstance(String contentType){
        OneContentFragment fragment =new OneContentFragment();
        Bundle bundle=new Bundle();
        bundle.putString(CONTENT_TYPE,contentType);
        fragment.setArguments(bundle);
        return fragment;
    }
    @BindView(R.id.smartRL_one_content)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_one_content)
    RecyclerView mRecyclerView;

    private String contentType="";
    private int pageOffset;
    private OneContentAdapter mAdapter;
    private List<OneBookEntity.Books> mBookEntities=new ArrayList<>();
    private List<OneBookViewModel> mModelList=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            contentType=bundle.getString(CONTENT_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void initView() {
      mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false));
      mAdapter=new OneContentAdapter(mModelList);
      mRecyclerView.setAdapter(mAdapter);
      mRefreshLayout.setOnLoadmoreListener(this);
      mRefreshLayout.setOnRefreshListener(this);
      //第一次进入
      if(mModelList.size()==0){
          mRefreshLayout.autoRefresh();
      }
      mAdapter.setListener(new OneContentAdapter.ItemTouchListener() {
          @Override
          public void onItemClick(View v, int position) {
              OneBookEntity.Books data = mBookEntities.get(position);
              OneBookActivity.startActivity(getActivity(),data,v);
          }
      });
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_one_content;
    }

    void loadData(){
        ApiManager.getInstance().getService(DouService.class)
                .getBooksByPage(contentType,pageOffset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::addDisable)
                .doOnError(x->{showErrorPage();})
                .subscribe(x->{
                   if(x.books!=null && x.books.size()>0){
                       if(pageOffset==0){
                           mBookEntities.clear();
                           mModelList.clear();
                       }
                       for (OneBookEntity.Books book : x.books) {
                           mModelList.add(new OneBookViewModel(book.image,book.title,Float.valueOf(book.rating.average)));
                       }
                       mBookEntities.addAll(x.books);
                       mAdapter.notifyDataSetChanged();
                   }
                   mRefreshLayout.finishRefresh();
                   mRefreshLayout.finishLoadmore();
                   hideLoadingPage();
                });
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
      pageOffset=0;
      loadData();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
     pageOffset=pageOffset+mModelList.size();
     loadData();
    }

    @Override
    protected void onErrorRefresh() {
        super.onErrorRefresh();
        loadData();
    }
}
