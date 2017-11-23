package com.kenzz.reader.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenzz.reader.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankIOFragment extends BaseFragment {


    public GankIOFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void initView() {

    }

    @Override
    public int getContentId() {
        return R.layout.fragment_gank_io;
    }

}
