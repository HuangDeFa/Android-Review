package com.kenzz.reader.adapter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ReplacementSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenzz.reader.R;
import com.kenzz.reader.bean.MovieViewModel;
import com.kenzz.reader.utils.ImageLoader;

import java.util.List;

/**
 * Created by ken.huang on 12/6/2017.
 * MovieAdapter
 */

public class MovieAdapter extends BaseRecyclerViewAdapter<MovieViewModel,GankDailyAdapter.GankDailyVH> {

    public MovieAdapter(List<MovieViewModel> dataList) {
        this.dataList = dataList;
    }

    public interface ItemTouchListener{
        void onItemClick(View view,int position);
    }

    public void setListener(ItemTouchListener listener) {
        mListener = listener;
    }

    private ItemTouchListener mListener;

    @Override
    public GankDailyAdapter.GankDailyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(viewType==0){
            itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dou_movie_head,parent,false);
        }
        else {
            itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dou_movie_content,parent,false);
        }
        return new GankDailyAdapter.GankDailyVH(itemView);
    }

    @Override
    public void onBindViewHolder(GankDailyAdapter.GankDailyVH holder, int position) {
        if(position!=0){
            MovieViewModel data=dataList.get(position-1);
            ImageView imageView = holder.getView(R.id.iv_item_dou_movie);
            ImageLoader.LoadImage(imageView,data.imageUrl,R.mipmap.img_default_movie);
            TextView titleView=holder.getView(R.id.tv_item_dou_movie_title);
            titleView.setText(data.title);
            TextView directors=holder.getView(R.id.tv_item_dou_movie_director);

            FrameSpan span=new FrameSpan();
            Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(15);
            paint.setColor(directors.getContext().getResources().getColor(R.color.colorPrimary));
            span.setPaint(paint);
            String original="导演："+data.directors;
            SpannableString spannableString=new SpannableString(original);
            spannableString.setSpan(span,0,original.indexOf("："),0);
            directors.setText(spannableString);

            TextView cast=holder.getView(R.id.tv_item_dou_movie_cast);
            cast.setText(String.format("%s%s","主演：",data.casts));
            TextView type=holder.getView(R.id.tv_item_dou_movie_type);
            type.setText(String.format("%s%s","类型：",data.type));
            TextView rate=holder.getView(R.id.tv_item_dou_movie_rate);
            rate.setText(String.format("%s%s","评分：",data.rate));

        }else {
           ImageView imageView= holder.getView(R.id.iv_item_dou_movie_head);
           ImageLoader.LoadImage(imageView,R.mipmap.ic_nav_meizi,R.mipmap.ic_nav_meizi);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    if(position!=0){
                        v=holder.getView(R.id.iv_item_dou_movie);
                    }
                    mListener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
       if(position==0){
           return 0;
       }
       return 1;
    }

    public static class FrameSpan extends ReplacementSpan{

        private int mWith;
        private Paint mPaint;

        public void setPaint(Paint paint) {
            mPaint = paint;
        }

        public FrameSpan() {
            mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
            mWith = (int) paint.measureText(text,start,end);
            return mWith;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
          canvas.drawLine(x,bottom,x+mWith,bottom,mPaint);
          canvas.drawText(text,start,end,x,y,paint);
        }
    }
}
