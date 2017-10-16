package com.kenzz.reviewapp;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kenzz.reviewapp.activity.BaseActivity;
import com.kenzz.reviewapp.activity.BroadCastReceiverActivity;
import com.kenzz.reviewapp.activity.DesignActivity;
import com.kenzz.reviewapp.activity.QQContactListActivity;
import com.kenzz.reviewapp.activity.RemoteViewsActivity;
import com.kenzz.reviewapp.activity.ServiceActivity;
import com.kenzz.reviewapp.activity.ViewLearningActivity;
import com.kenzz.reviewapp.adapter.ComBaseAdapter;
import com.kenzz.reviewapp.adapter.ComBaseVH;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.main_my_recyclerView)
    RecyclerView mRecyclerView;
    private List<String> mList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initData();
    }

    private void initData(){
        mList.add("View的学习和总结");
        mList.add("SupportDesign View的学习和总结");
        mList.add("仿QQ联系人列表");
        mList.add("BroadcastReceiver广播接收者");
        mList.add("Service服务");
        mList.add("RemoteViews");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
               outRect.set(20,20,20,20);
            }
        });
        mRecyclerView.setAdapter(new ComBaseAdapter<String>(mList) {
            @Override
            public View createItemView(ViewGroup parent, int viewType) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                return view;
            }

            @Override
            public void onBindView(ComBaseVH holder, final int position) {
                TextView textView = (TextView) holder.itemView;
                textView.setBackgroundColor(Color.GRAY);
                textView.setText(mList.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (position){
                            case 0:
                                startActivity(new Intent(MainActivity.this, ViewLearningActivity.class));
                                break;
                            case 1:
                                startActivity(new Intent(MainActivity.this, DesignActivity.class));
                                break;
                            case 2:
                                startActivity(new Intent(MainActivity.this, QQContactListActivity.class));
                                break;
                            case 3:
                                startActivity(new Intent(MainActivity.this, BroadCastReceiverActivity.class));
                                break;
                            case 4:
                                startActivity(new Intent(MainActivity.this, ServiceActivity.class));
                                break;
                            case 5:
                                startActivity(new Intent(MainActivity.this, RemoteViewsActivity.class));
                                break;
                        }
                    }
                });
            }
        });
    }


}
