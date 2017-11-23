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
public class GankFragment extends BaseFragment {


    public GankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gank, container, false);
    }

    @Override
    public int getContentId() {
        return 0;
    }

}
