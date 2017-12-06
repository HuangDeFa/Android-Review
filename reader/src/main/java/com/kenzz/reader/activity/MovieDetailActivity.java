package com.kenzz.reader.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kenzz.reader.R;

import butterknife.BindView;
import butterknife.OnClick;

public class MovieDetailActivity extends BaseActivity {

    @BindView(R.id.tbl_head)
    ViewGroup titleBarLayout;
    @BindView(R.id.tb_head)
    Toolbar toolBar;
    @BindView(R.id.status_view)
    View mView;
    @BindView(R.id.ll_movie_activity_content_container)
    LinearLayout contentLayout;
    @BindView(R.id.rl_activity_movie_content)
    RelativeLayout movieHeadLayout;
    @BindView(R.id.tv_toolbar_subtitle)
    TextView subTitle;
    @BindView(R.id.tv_toolbar_title)
    TextView title;
    @BindView(R.id.tv_movie_activity_rate)
    TextView rate;
    @BindView(R.id.tv_movie_activity_director)
    TextView director;
    @BindView(R.id.tv_movie_activity_cast)
    TextView cast;
    @BindView(R.id.tv_movie_activity_type)
    TextView movieType;
    @BindView(R.id.tv_movie_activity_showtime)
    TextView showTime;
    @BindView(R.id.tv_movie_activity_country)
    TextView country;
    @BindView(R.id.tv_movie_activity_summary)
    TextView movieSummary;
    @BindView(R.id.tv_movie_activity_names)
    TextView movieNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setSupportActionBar(toolBar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        initView();
    }

    private void initView() {
        titleBarLayout.measure(0,0);
        int titleBarHeight = titleBarLayout.getMeasuredHeight();
        int statusBarHeight = getStatusBarHeight(this);
        //占据状态栏
        mView.getLayoutParams().height=statusBarHeight;
        mView.setVisibility(View.VISIBLE);
        titleBarLayout.setBackground(new ColorDrawable(Color.TRANSPARENT));
        toolBar.setBackground(new ColorDrawable(Color.TRANSPARENT));

        ((ViewGroup.MarginLayoutParams)movieHeadLayout.getLayoutParams()).topMargin=titleBarHeight+statusBarHeight*2;
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_movie_detail;
    }

    @OnClick({R.id.iv_back})
    public void onBack(){
        onBackPressed();
    }
}
