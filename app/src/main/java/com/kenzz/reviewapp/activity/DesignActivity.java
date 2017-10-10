package com.kenzz.reviewapp.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.kenzz.reviewapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DesignActivity extends BaseActivity {

    @InjectView(R.id.design_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.design_bottom_tab)
    TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);
        getWindow().getAttributes().flags|= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {

        setSupportActionBar(mToolbar);

        mTabLayout.addTab(mTabLayout.newTab().setText("暴漫"));
        mTabLayout.addTab(mTabLayout.newTab().setText("肯打鸡"));
        mTabLayout.addTab(mTabLayout.newTab().setText("小黑屋"));
        mTabLayout.addTab(mTabLayout.newTab().setText("王尼玛"));
    }
}
