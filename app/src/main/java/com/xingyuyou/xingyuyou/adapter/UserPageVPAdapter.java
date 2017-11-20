package com.xingyuyou.xingyuyou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.xingyuyou.xingyuyou.base.BaseFragment;

import java.util.List;

/**
 * Created by LG on 2016/11/27.
 */

public class UserPageVPAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mFragments;
    private List<String> keySet;
    private FragmentManager fm;

    public UserPageVPAdapter(FragmentManager fm, List<BaseFragment> fragments, List<String> keySet) {
        super(fm);
        this.fm = fm;
        this.mFragments = fragments;
        this.keySet = keySet;
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
        return keySet.get(position);
    }
}