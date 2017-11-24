package com.kenzz.reader.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by ken.huang on 11/23/2017.
 *
 */

public class MyFragmentAdapter extends FragmentStatePagerAdapter {

    private MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }
    private List<Fragment> mFragments;
    private List<String> mTitles;

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragments){
        this(fm);
        this.mFragments = fragments;
    }

    public MyFragmentAdapter(FragmentManager fm,List<Fragment> fragments,List<String> titles){
        super(fm);
        this.mFragments = fragments;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitles!=null && mTitles.size()>0){
            return mTitles.get(position);
        }
        return null;
    }
}
