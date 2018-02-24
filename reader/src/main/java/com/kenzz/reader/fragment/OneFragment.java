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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends BaseFragment {


    public OneFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.pv_gank_content)
    SuperViewPager mViewPager;
    @BindView(R.id.tbl_head)
    TabLayout mTabLayout;

    private MyFragmentAdapter mFragmentAdapter;
    private String[] titles = {"文学", "文化", "生活"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        List<Fragment> fragments = new ArrayList<>(3);
        fragments.add(OneContentFragment.newInstance("文学"));
        fragments.add(OneContentFragment.newInstance("文化"));
        fragments.add(OneContentFragment.newInstance("生活"));
        mFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager(), fragments, Arrays.asList(titles));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mFragmentAdapter);
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_one;
    }


}
