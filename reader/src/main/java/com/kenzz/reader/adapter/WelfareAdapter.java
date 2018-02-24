package com.kenzz.reader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kenzz.reader.R;
import com.kenzz.reader.bean.GankEntity;
import com.kenzz.reader.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangdefa on 26/11/2017.
 * Version 1.0
 */

public class WelfareAdapter extends BaseRecyclerViewAdapter<GankEntity.ResultsBean,GankDailyAdapter.GankDailyVH> {

    public WelfareAdapter() {
        this.dataList=new ArrayList<>();
    }

    public  interface WelfareAdapterListener{
        void onClick(View view,int position);
    }

    public void setWelfareListener(WelfareAdapterListener listener) {
        mListener = listener;
    }

    private WelfareAdapterListener mListener;

    public void updateDataList(List<GankEntity.ResultsBean> dataList){
        this.dataList=dataList;
        notifyDataSetChanged();
    }

    @Override
    public GankDailyAdapter.GankDailyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gank_welfare,parent,false);
        return new GankDailyAdapter.GankDailyVH(itemView);
    }

    @Override
    public void onBindViewHolder(GankDailyAdapter.GankDailyVH holder, int position) {
        ImageView imageView = holder.getView(R.id.iv_item_gank_welfare);
        imageView.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.onClick(v,position);
            }
        });
        ImageLoader.LoadImage(imageView,dataList.get(position).url,R.mipmap.img_default_meizi);
    }
}
