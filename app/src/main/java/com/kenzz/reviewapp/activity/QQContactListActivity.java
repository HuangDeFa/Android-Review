package com.kenzz.reviewapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.StackView;

import com.kenzz.reviewapp.R;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class QQContactListActivity extends BaseActivity {

    @InjectView(R.id.qqList_head_bar)
    RelativeLayout mLayout;
    @InjectView(R.id.qqList_contact_list)
    ExpandableListView mExpandableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_qqcontact_list);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        int height=getStatusBarHeight();
        ViewGroup.MarginLayoutParams layoutParams =(ViewGroup.MarginLayoutParams) mLayout.getLayoutParams();
        layoutParams.topMargin+=height;
        mLayout.setLayoutParams(layoutParams);

    }

    private class MyExpandAdapter extends SimpleExpandableListAdapter{

        public MyExpandAdapter(Context context, List<? extends Map<String, ?>> groupData,
                               int groupLayout, String[] groupFrom, int[] groupTo,
                               List<? extends List<? extends Map<String, ?>>> childData,
                               int childLayout, String[] childFrom, int[] childTo) {
            super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
        }
    }
}
