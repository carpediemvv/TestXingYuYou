package com.xingyuyou.xingyuyou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by 24002 on 2017/9/21.
 */

public class EmotionTabAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments;
    public EmotionTabAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        mFragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
