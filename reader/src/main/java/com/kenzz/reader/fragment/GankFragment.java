package com.kenzz.reader.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenzz.reader.R;
import com.kenzz.reader.adapter.MyFragmentAdapter;
import com.kenzz.reader.widget.SuperViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankFragment extends BaseFragment {

    @BindView(R.id.pv_gank_content)
    SuperViewPager mSuperViewPager;
    @BindView(R.id.tbl_head)
    TabLayout mTabLayout;
    List<String> titles=new ArrayList<String>(){
        {
          add("今日推荐");
          add("妹子福利");
          add("干货精选");
          add("大安卓");
        }
    };
    public GankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void initView() {
        mTabLayout.setupWithViewPager(mSuperViewPager);
        mSuperViewPager.setOffscreenPageLimit(2);
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new GankDailyFragment());
        fragments.add(new WelfareFragment());
        fragments.add(new GankIOFragment());
        fragments.add(new AndroidFragment());
        mSuperViewPager.setAdapter(new MyFragmentAdapter(getChildFragmentManager(),fragments,titles));
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_gank;
    }

}
