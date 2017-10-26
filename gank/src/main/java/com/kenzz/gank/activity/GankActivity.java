package com.kenzz.gank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenzz.gank.R;
import com.kenzz.gank.bean.GankDailyEntity;
import com.kenzz.gank.bean.GankEntity;
import com.kenzz.gank.net.ApiManager;
import com.kenzz.gank.net.IApiCallBack;
import com.kenzz.gank.util.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GankActivity extends BaseActivity {

    private static final String TAG = GankActivity.class.getSimpleName();
    private int[] mDate=new int[3];
    @InjectView(R.id.back)
    ImageView mBackImageView;
    @InjectView(R.id.home_head_bar)
    View mHeadBar;
    @InjectView(R.id.gank_page_rv)
    RecyclerView mRecyclerView;
    @InjectView(R.id.home_head_title)
    TextView mTitleText;

    private GankDailyAdapter mAdapter;
    private List<GankEntity.ResultsBean> mResultsBeen=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank);
        setFullScreen();
        ButterKnife.inject(this);
        parseDateString();
        initView();
        loadData();
    }

    private void initView() {
        int statusBarHeight = getStatusBarHeight();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mHeadBar.getLayoutParams();
        layoutParams.topMargin=statusBarHeight;
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });
        mTitleText.setText("每日干货");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mAdapter = new GankDailyAdapter(mResultsBeen);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void parseDateString(){
       String date = getIntent().getStringExtra("DATE");
        date = date.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
        try {
            Date d = format.parse(date );
            System.out.print(d);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
           mDate[0] = calendar.get(Calendar.YEAR);
           mDate[1] = calendar.get(Calendar.MONTH)+1;
           mDate[2] = calendar.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Date parseDateString(String dateStr) throws ParseException {
        String date= dateStr.replace("Z"," UTC");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        Date d = format.parse(date);
        return d;
    }

    private void loadData(){
        ApiManager.getInstance().getDailyData(mDate[0], mDate[1], mDate[2], new IApiCallBack<GankDailyEntity>() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(GankDailyEntity data) {
                if (!data.isError()){
                    GankDailyEntity.ResultsBean results = data.getResults();
                    if(results!=null){
                        if(results.getAndroid()!=null) mResultsBeen.addAll(results.getAndroid());
                        if(results.getiOS()!=null) mResultsBeen.addAll(results.getiOS());
                        if(results.getApp()!=null) mResultsBeen.addAll(results.getApp());
                        if(results.getRestVideo()!=null) mResultsBeen.addAll(results.getRestVideo());
                        if(results.getFrontEnd()!=null) mResultsBeen.addAll(results.getFrontEnd());
                        if(results.getResource()!=null) mResultsBeen.addAll(results.getResource());
                        if(results.getRecommend()!=null) mResultsBeen.addAll(results.getRecommend());
                        if(results.getWelfare()!=null) mResultsBeen.addAll(results.getWelfare());
                        mAdapter.notifyDataSetChanged();
                    }
                }
                Log.d(TAG,data.getCategory().toString());
            }
        });
    }

    class GankVH extends RecyclerView.ViewHolder{

        @InjectView(R.id.gank_daily_imageView)
        ImageView mImageView;
        @InjectView(R.id.gank_daily_content)
        TextView  mContentText;
        @InjectView(R.id.gank_daily_publish)
        TextView  mPublishText;
        @InjectView(R.id.gank_daily_type)
        TextView  mTypeText;

        public GankVH(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }

    private class GankDailyAdapter extends RecyclerView.Adapter<GankVH>{

        private List<GankEntity.ResultsBean> mDataList;
        GankDailyAdapter(List<GankEntity.ResultsBean> dataList){
            this.mDataList = dataList;
        }
        @Override
        public GankVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.daily_gank_item,parent,false);
            return new GankVH(view);
        }

        @Override
        public void onBindViewHolder(GankVH holder, int position) {
            final GankEntity.ResultsBean data = mDataList.get(position);
            final String type = data.getType();
            holder.mTypeText.setText(type);
            holder.mContentText.setText(data.getDesc());
            if(type.equals(ApiManager.WELFARE_TYPE)){
                holder.mImageView.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().
                        loadImage(GankActivity.this,holder.mImageView,data.getUrl());
            }else {
                List<String> images = data.getImages();
                if(images==null){
                    holder.mImageView.setVisibility(View.GONE);
                }else {
                    holder.mImageView.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().
                            loadImage(GankActivity.this,holder.mImageView,images.get(0));
                }
            }
            try {
                Date date = parseDateString(data.getPublishedAt());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dateStr = format.format(date);
                holder.mPublishText.setText(data.getWho()+" 发布于 "+dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(type.equals(ApiManager.WELFARE_TYPE)){
                        Intent intent = new Intent(GankActivity.this, MeiZiActivity.class);
                        intent.putExtra("URL",data.getUrl());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                    }else {
                        Intent intent=new Intent(GankActivity.this,GankWebActivity.class);
                        intent.putExtra("URL",data.getUrl());
                        intent.putExtra("CONTENT",data.getDesc());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }
}
