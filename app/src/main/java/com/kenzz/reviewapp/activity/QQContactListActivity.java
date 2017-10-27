package com.kenzz.reviewapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.StackView;
import android.widget.TextView;

import com.kenzz.reviewapp.R;
import com.kenzz.reviewapp.adapter.ComBaseVH;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;

public class QQContactListActivity extends BaseActivity {

    @BindView(R.id.qqList_head_bar)
    RelativeLayout mLayout;
    @BindView(R.id.qqList_contact_list)
    ExpandableListView mExpandableListView;

    private List<String> mGroups;
    private List<List<String>> mItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_qqcontact_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        int height=getStatusBarHeight();
        ViewGroup.MarginLayoutParams layoutParams =(ViewGroup.MarginLayoutParams) mLayout.getLayoutParams();
        layoutParams.topMargin+=height;
        mLayout.setLayoutParams(layoutParams);

        mGroups = new ArrayList<>();
        mItemList = new ArrayList<>();
        for(int i =0;i<5;i++){
            mGroups.add(i+"");
            List<String> list = new ArrayList<>();
            for(int j=0;j<10;j++){
                list.add("Group "+i+": Item "+j);
            }
            mItemList.add(list);
        }



        mExpandableListView.setAdapter(new MyExpandAdapter(this,mGroups,mItemList));
        mExpandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private class MyExpandAdapter extends BaseExpandableListAdapter{

        private Context mContext;
        private List<String> mGroupList;
        private List<List<String>> mItemList;

        public MyExpandAdapter(Context context, @NonNull List<String> groupList, @Nullable List<List<String>> itemList){
            mContext = context;
            mGroupList = groupList;
            mItemList = itemList;
        }


        @Override
        public int getGroupCount() {
            return mGroupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mItemList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mItemList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            ComBaseVH holder;
            if(convertView==null){
                convertView= LayoutInflater.from(mContext).inflate(R.layout.qqcontact_list_group_item,parent,false);
                holder = new ComBaseVH(convertView);
            }else{
                holder = (ComBaseVH) convertView.getTag();
            }
            TextView tv = holder.getView(R.id.qqContact_group_item);
            tv.setText(mGroupList.get(groupPosition));
            convertView.setTag(holder);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView= LayoutInflater.from(mContext).inflate(R.layout.qqcontact_list_user_item,parent,false);
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
