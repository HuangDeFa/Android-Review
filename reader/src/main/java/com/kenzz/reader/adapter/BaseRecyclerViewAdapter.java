package com.kenzz.reader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ken.huang on 11/24/2017.
 * BaseRecyclerViewAdapter
 */

public abstract class BaseRecyclerViewAdapter<E,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public List<E> dataList;

    @Override
    public int getItemCount() {
        return dataList==null?0:dataList.size();
    }
}
