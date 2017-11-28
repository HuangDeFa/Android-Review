package com.kenzz.reader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenzz.reader.R;
import com.kenzz.reader.bean.GankEntity;
import com.kenzz.reader.utils.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ken.huang on 11/28/2017.
 *
 */

public class GankIOAdapter extends BaseRecyclerViewAdapter<GankEntity.ResultsBean,GankDailyAdapter.GankDailyVH> {

   public GankIOAdapter(List<GankEntity.ResultsBean> dataList){
        this.dataList = dataList;
    }

    public void updateDataList(List<GankEntity.ResultsBean> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public interface OnClickListener{
        void onClick(View view,int position);
    }

    public void setListener(OnClickListener listener) {
        mListener = listener;
    }

    private OnClickListener mListener;

    @Override
    public GankDailyAdapter.GankDailyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=null;
        if(viewType==1){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gank_io_with_image,parent,false);
        }else {
            itemView =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gank_io_normal,parent,false);
        }
        return new GankDailyAdapter.GankDailyVH(itemView);
    }

    @Override
    public void onBindViewHolder(GankDailyAdapter.GankDailyVH holder, int position) {
        GankEntity.ResultsBean data = dataList.get(position);
        TextView contentText = holder.getView(R.id.tv_item_gank_io_normal_content);
        TextView userText = holder.getView(R.id.tv_item_gank_io_normal_user);
        TextView timeText = holder.getView(R.id.tv_item_gank_io_normal_time);
        if(getItemViewType(position)==1){
         ImageView imageView = holder.getView(R.id.iv_item_gank_io);
            ImageLoader.LoadImage(imageView,data.images.get(0),R.mipmap.img_one_bi_one);
        }
        contentText.setText(data.desc);
        userText.setText(data.who);
        timeText.setText(formatTime(data.publishedAt));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onClick(v,position);
                }
            }
        });
    }

    private String formatTime(String publishedAt) {

        try {
            Date date = parseDateString(publishedAt);
            publishedAt =friendDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return publishedAt;
    }

    @Override
    public int getItemViewType(int position) {
        GankEntity.ResultsBean  data= dataList.get(position);
        if(data.images!=null && data.images.size()>0){
            return 1;
        }
        return 0;
    }

    private Date parseDateString(String dateStr) throws ParseException {
        String date= dateStr.replace("Z"," UTC");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        Date d = format.parse(date);
        return d;
    }

    private String friendDate(Date date){
        Calendar now=Calendar.getInstance();
        final Calendar source =Calendar.getInstance();
        source.setTime(date);
        int year = now.get(Calendar.YEAR)-source.get(Calendar.YEAR);
        if(year>0){
            return year+"年前";
        }
        int month = now.get(Calendar.MONTH)-source.get(Calendar.MONTH);
        if(month>0){
            return month+"月前";
        }
        int day = now.get(Calendar.DAY_OF_MONTH)-source.get(Calendar.DAY_OF_MONTH);
        if(day>0){
            return day+"天前";
        }
        int hour=now.get(Calendar.HOUR_OF_DAY)-source.get(Calendar.HOUR_OF_DAY);
        if(hour>0){
            return hour+"小时前";
        }
        int min=now.get(Calendar.MINUTE)-source.get(Calendar.MINUTE);
        if(min>0){
            return min+"分钟前";
        }
        return "刚刚";
    }
}
