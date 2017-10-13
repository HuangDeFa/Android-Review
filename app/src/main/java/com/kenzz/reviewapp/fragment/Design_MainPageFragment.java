package com.kenzz.reviewapp.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kenzz.reviewapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Design_MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Design_MainPageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @InjectView(R.id.design_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.design_appbar)AppBarLayout mBarLayout;
    @InjectView(R.id.design_toolbar_title)TextView mView;

    private int toolBarColor;
    private static final String TAG=Design_MainPageFragment.class.getSimpleName();
    public Design_MainPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Design_MainPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Design_MainPageFragment newInstance(String param1, String param2) {
        Design_MainPageFragment fragment = new Design_MainPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_design__main_page, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.inject(this,getActivity());
        initView();
    }

    private void initView() {
        toolBarColor = getResources().getColor(R.color.colorPrimary);
        mBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG,"verticalOffset: "+verticalOffset);
                float scale = Math.abs(verticalOffset)/ (float)(appBarLayout.getHeight() -mToolbar.getHeight());
                Log.d(TAG,"Scale: "+scale);
                float alpha= Color.alpha(toolBarColor)*scale;
                int argb = Color.argb((int) alpha, Color.red(toolBarColor), Color.green(toolBarColor), Color.blue(toolBarColor));
                mToolbar.setBackgroundColor(argb);
                mView.setAlpha(scale);
            }
        });
    }
}
