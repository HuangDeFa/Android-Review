package com.kenzz.reviewapp.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kenzz.reviewapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<String> items=new ArrayList<>();
    {
        for(int i=0;i<30;i++){
            items.add("This item "+i);
        }
    }
    private MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        mRecyclerView= (RecyclerView) findViewById(R.id.demo_myRv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mAdapter=new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private class MyHolder extends RecyclerView.ViewHolder{
        TextView descriptionText;
        TextView showLabel;

        public MyHolder(View itemView) {
            super(itemView);
            descriptionText= (TextView) ((ViewGroup)itemView).getChildAt(0);
            showLabel= (TextView) ((ViewGroup)itemView).getChildAt(1);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyHolder>{

        private List<Integer> mSelectPos=new ArrayList<>();
        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(obtainHeaderViewItem());
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {
           holder.descriptionText.setText(items.get(position));
           holder.showLabel.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   TextView temp=(TextView)v;
                   if(mSelectPos.contains(position)){
                       mSelectPos.remove(position);
                       temp.setText("Show All");
                   }else {
                       mSelectPos.add(position);
                       temp.setText("Hide All");
                   }
               }
           });
           if(mSelectPos.contains(position)){
               holder.showLabel.setText("Hide All");
           }else {
               holder.showLabel.setText("Show All");
           }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private View obtainHeaderViewItem(){
        RelativeLayout container=new RelativeLayout(this);
        ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(-1,dp2px(this,35));
        container.setLayoutParams(lp);
        TextView textView=new TextView(this);
        textView.setTextColor(Color.parseColor("#3385e9"));
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(-2,-2);
        layoutParams.leftMargin=dp2px(this,15);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        container.addView(textView,layoutParams);

        textView=new TextView(this);
        textView.setText("show All");
        textView.setTextColor(Color.parseColor("#3385e9"));
        layoutParams=new RelativeLayout.LayoutParams(-2,-2);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.rightMargin=dp2px(this,20);
        container.addView(textView,layoutParams);
        return container;
    }

    private int dp2px(Context context, float dp){
      float scale = context.getResources().getDisplayMetrics().density;
      return (int) (scale*dp+0.5f);
    }
    private View obtainViewItem(){
        TextView textView=new TextView(this);
        textView.setTextColor(Color.parseColor("#3385e9"));
        return textView;
    }

}
