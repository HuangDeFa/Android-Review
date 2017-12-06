package com.kenzz.reader.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenzz.reader.R;
import com.kenzz.reader.activity.MovieTop250Activity;
import com.kenzz.reader.adapter.MovieAdapter;
import com.kenzz.reader.bean.MovieEntity;
import com.kenzz.reader.bean.MovieViewModel;
import com.kenzz.reader.http.ApiManager;
import com.kenzz.reader.http.DouService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class DouFragment extends BaseFragment implements OnLoadmoreListener {

    @BindView(R.id.smartRL_dou_movie)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_dou_movie)
    RecyclerView mRecyclerView;

    private int offset;
    private MovieEntity mMovieEntity;
    private List<MovieViewModel> mMovieViewModels=new ArrayList<>();
    private MovieAdapter mAdapter;
    public DouFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void initView() {
       mRefreshLayout.setOnLoadmoreListener(this);
       mAdapter=new MovieAdapter(mMovieViewModels);
       mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
       mRecyclerView.setAdapter(mAdapter);
       if(mMovieViewModels.size()==0){
           showLoadingPage();
       }
       mAdapter.setListener(new MovieAdapter.ItemTouchListener() {
           @Override
           public void onItemClick(View view, int position) {
              if(position==0){
                  MovieTop250Activity.startActivity(getActivity());
              }
           }
       });
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
        loadData();
    }

    private void loadData() {
        ApiManager.getInstance().getService(DouService.class)
                .getMovieInThread(offset,20)
                .subscribeOn(Schedulers.io())
                .doOnError(e->{
                    if(mRecyclerView!=null){
                        showErrorPage();
                    }
                })
                .doOnSubscribe(this::addDisable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x->{
                   if(x.subjects!=null && x.subjects.size()>0){
                      mMovieEntity=x;
                       for (MovieEntity.Subject subject : x.subjects) {
                           mMovieViewModels.add(MovieViewModel.build(subject));
                       }
                       if(mRecyclerView!=null){
                           mAdapter.notifyDataSetChanged();
                           mRefreshLayout.setLoadmoreFinished(true);
                           hideLoadingPage();
                       }
                   }
                });
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_dou;
    }


    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
         offset=offset+mMovieViewModels.size();
         loadData();
    }
}
