package com.kenzz.reader.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenzz.reader.R;
import com.kenzz.reader.widget.Banner;
import com.kenzz.reader.widget.DefaultBannerLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankDailyFragment extends BaseFragment {

    @BindView(R.id.rv_gank_daily)
    RecyclerView mRecyclerView;

    public GankDailyFragment() {
        // Required empty public constructor
    }

    @Override
    public void initView() {

    }

    @Override
    public int getContentId() {
        return R.layout.fragment_gank_daily;
    }

}
