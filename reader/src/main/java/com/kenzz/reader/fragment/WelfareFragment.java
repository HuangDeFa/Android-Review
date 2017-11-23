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
public class WelfareFragment extends BaseFragment {


    public WelfareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void initView() {
     showErrorPage();
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_welfare;
    }

}
