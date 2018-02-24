package com.kenzz.reader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenzz.reader.R;
import com.kenzz.reader.bean.OneBookViewModel;
import com.kenzz.reader.utils.ImageLoader;

import java.util.List;

/**
 * Created by ken.huang on 11/29/2017.
 *
 */

public class OneContentAdapter extends BaseRecyclerViewAdapter<OneBookViewModel,GankDailyAdapter.GankDailyVH> {

    public OneContentAdapter(List<OneBookViewModel> dataList) {
        this.dataList=dataList;
    }

    public interface ItemTouchListener{
        void onItemClick(View v,int position);
    }

    public void setListener(ItemTouchListener listener) {
        mListener = listener;
    }

    private ItemTouchListener mListener;
    @Override
    public GankDailyAdapter.GankDailyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_one_book,parent,false);
        return new GankDailyAdapter.GankDailyVH(itemView);
    }

    @Override
    public void onBindViewHolder(GankDailyAdapter.GankDailyVH holder, int position) {
        OneBookViewModel data=dataList.get(position);
        ImageView imageView=holder.getView(R.id.iv_item_one_book);
        TextView titleView=holder.getView(R.id.tv_item_one_title);
        TextView rateView=holder.getView(R.id.tv_item_one_rate);

        ImageLoader.LoadImage(imageView,data.url,R.mipmap.img_default_book);
        titleView.setText(data.title);
        rateView.setText("评分："+data.rate);
        imageView.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.onItemClick(v,position);
            }
        });
    }
}
