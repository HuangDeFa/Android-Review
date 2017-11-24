package com.kenzz.reader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ken.huang on 11/24/2017.
 * BaseRecyclerViewAdapter
 */

public class BaseRecyclerViewAdapter<E> extends RecyclerView.Adapter {

    private List<E> dataList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataList==null?0:dataList.size();
    }
}
