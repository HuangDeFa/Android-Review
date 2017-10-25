package com.kenzz.gank.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenzz.gank.R;
import com.kenzz.gank.activity.GankActivity;
import com.kenzz.gank.activity.MeiZiActivity;
import com.kenzz.gank.bean.GankEntity;
import com.kenzz.gank.net.ApiManager;
import com.kenzz.gank.net.IApiCallBack;
import com.kenzz.gank.util.ImageLoader;
import com.kenzz.gank.widget.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GirlFragment extends Fragment {

    private static final String TAG=GirlFragment.class.getSimpleName();
    public GirlFragment() {
        // Required empty public constructor
    }

    @InjectView(R.id.girl_page_sr)
    SuperSwipeRefreshLayout mRefreshLayout;
    @InjectView(R.id.girl_page_rv)
    RecyclerView mRecyclerView;

    private List<GankEntity.ResultsBean> mResultsBeen;
    private GirlAdapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_girl, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.inject(this,getView());
        initView();
    }

    private void initView() {
      mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
      mRecyclerView.setLayoutManager(mLayoutManager);
      mResultsBeen = new ArrayList<>();
      mAdapter = new GirlAdapter(mResultsBeen);
      mRecyclerView.setAdapter(mAdapter);
      mRefreshLayout.setColorSchemeColors(Color.parseColor("#518ffa"),Color.parseColor("#34c4fa"));
      mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
              pageIndex=1;
              loadPage(1);
          }
      });

      mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
          @Override
          public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
              super.onScrolled(recyclerView, dx, dy);
              int[] lasPos = new int[mLayoutManager.getSpanCount()];
              mLayoutManager.findLastVisibleItemPositions(lasPos);
              if (mResultsBeen.size() - Math.max(lasPos[0], lasPos[1]) < 4 &&
                      !mRefreshLayout.isRefreshing()) {
                 loadPage(++pageIndex);
              }
          }
      });
    }

    class GirlAdapter extends RecyclerView.Adapter<GirlVH>{

        private List<GankEntity.ResultsBean> mGankEntities;
        public GirlAdapter(List<GankEntity.ResultsBean> gankEntities){
            this.mGankEntities = gankEntities;
        }
        @Override
        public GirlVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.girl_image_layout,parent,false);
            return new GirlVH(view);
        }

        @Override
        public void onBindViewHolder(GirlVH holder, int position) {
          final GankEntity.ResultsBean data = mGankEntities.get(position);
            ImageLoader.getInstance().loadImage(GirlFragment.this,holder.mImageView,data.getUrl());
            holder.mTextView.setText(data.getDesc());
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MeiZiActivity.class);
                    intent.putExtra("URL",data.getUrl());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                }
            });
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getContext(), GankActivity.class);
                    intent.putExtra("DATE",data.getPublishedAt());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mGankEntities.size();
        }
    }

    class GirlVH extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mTextView;
        public GirlVH(View itemView) {
            super(itemView);
            mImageView =itemView.findViewById(R.id.girl_image);
            mTextView =itemView.findViewById(R.id.girl_title);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getUserVisibleHint()){
            loadPage(1);
        }
    }

    private int pageIndex=1;
    private void loadPage(int pageNum){
        ApiManager.getInstance().getDataByCategory(ApiManager.WELFARE_TYPE, pageNum, new IApiCallBack<GankEntity>() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, throwable.getMessage());
            }

            @Override
            public void onSuccess(GankEntity data) {
                Log.d(TAG, data.toString());
                if(!data.isError() && data.getResults()!=null) {
                    mResultsBeen.addAll(data.getResults());
                    mAdapter.notifyDataSetChanged();
                }
                setRefreshStatus(false);
            }
        });
    }

    private void setRefreshStatus(boolean isRefreshing){
        mRefreshLayout.setRefreshing(isRefreshing);
    }
}
