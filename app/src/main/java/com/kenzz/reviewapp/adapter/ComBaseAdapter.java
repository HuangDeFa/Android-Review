package com.kenzz.reviewapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ken.huang on 10/9/2017.
 *
 */

public abstract class ComBaseAdapter<T> extends RecyclerView.Adapter {

    private List<T> mDataList;

    public ComBaseAdapter(List<T> dataList) {
        this.mDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final  View itemView=createItemView(parent,viewType);
        return new ComBaseVH(itemView);
    }

    public abstract View createItemView(ViewGroup parent,int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
     onBindView((ComBaseVH) holder,position);
    }

    public abstract void onBindView(ComBaseVH holder,int position);

    @Override
    public int getItemCount() {
        return this.mDataList==null?0:this.mDataList.size();
    }
}
