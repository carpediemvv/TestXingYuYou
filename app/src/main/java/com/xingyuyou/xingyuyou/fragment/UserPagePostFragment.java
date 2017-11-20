package com.xingyuyou.xingyuyou.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.adapter.MainVPAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/28.
 */
public class UserPagePostFragment extends BaseFragment {


    private TabLayout mTab;
    private ViewPager mContent;
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTitles;

    public static UserPagePostFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        UserPagePostFragment fragment = new UserPagePostFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.part_user_page_bottom, null);
    }

    @Override
    protected View initView() {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        minitView();
        minitData();
        toLiuYan();
    }

    /**
     * 监听留言事件
     */
    private void toLiuYan() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("toLiuYan");
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mContent.setCurrentItem(2);
            }

        };
        localBroadcastManager.registerReceiver(br, intentFilter);
    }
    private void minitView() {
        mTab = (TabLayout) getView().findViewById(R.id.tabs);
        mContent = (ViewPager) getView().findViewById(R.id.viewpager);
        mContent.setOffscreenPageLimit(2);
    }


    private void minitData() {
        mFragments = new ArrayList<Fragment>();
        mTitles = new ArrayList<String>();
        CollectPostListFragment hf3 = CollectPostListFragment.newInstance("推荐");
        MyPostListFragment hf4 = MyPostListFragment.newInstance("精品");
        Fragment_message_otherpage fragment_message_otherpage = new Fragment_message_otherpage();
        Bundle bundle = new Bundle();
        bundle.putString("uid", UserUtils.getUserId());
        fragment_message_otherpage.setArguments(bundle);
        mFragments.add(hf4);
        mFragments.add(hf3);
        mFragments.add(fragment_message_otherpage);
        mTitles.add("我的帖子");
        mTitles.add("收藏");
        mTitles.add("留言板");
        mContent.setAdapter(new MainVPAdapter(getChildFragmentManager(), mFragments, mTitles));
        mTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        mTab.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.colorPrimary));
        mTab.setTabMode(TabLayout.MODE_FIXED);
        mTab.setupWithViewPager(mContent);
    }


}
