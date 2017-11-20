package com.xingyuyou.xingyuyou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/7/5.
 */

public class AOtherpagerAdapter extends FragmentPagerAdapter{
    //存放Fragment的数组
    private ArrayList<Fragment> fragmentsList;
    private ArrayList<String> mTitles;

    public AOtherpagerAdapter(FragmentManager fm, ArrayList<String> mTitles, ArrayList<Fragment> fragmentsList) {
        super(fm);
        this.mTitles = mTitles;
        this.fragmentsList = fragmentsList;
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
