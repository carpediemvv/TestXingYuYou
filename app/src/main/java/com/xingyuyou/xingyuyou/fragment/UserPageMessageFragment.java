package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.adapter.MainVPAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/28.
 */
public class UserPageMessageFragment extends BaseFragment {


    private TabLayout mTab;
    private ViewPager mContent;
    private ArrayList<BaseFragment> mFragments;
    private ArrayList<String> mTitles;
    private TextView mIv_post_message_status;
    private TextView mIv_system_message_status;

    public static UserPageMessageFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        UserPageMessageFragment fragment = new UserPageMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.part_user_message_page_bottom, null);
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
        updateAllMessageStatus();
    }

    /**
     * 更新消息状态
     */
    private void updateAllMessageStatus() {
        //更新系统消息红点状态
        if (UserUtils.getAllSystemMessageStatus()) {
            mIv_system_message_status.setVisibility(View.VISIBLE);
            mIv_system_message_status.setText(UserUtils.getUpdateSystemMessageCount());
        }else {
            mIv_system_message_status.setVisibility(View.INVISIBLE);
        }
        //更新帖子消息红点状态
        if (UserUtils.getAllPostMessageStatus()) {
            mIv_post_message_status.setVisibility(View.VISIBLE);
            mIv_post_message_status.setText(UserUtils.getUpdatePostMessageCount());
        }else {
            mIv_post_message_status.setVisibility(View.INVISIBLE);
        }
    }


    private void minitView() {
        mTab = (TabLayout) getView().findViewById(R.id.tabs);
        mContent = (ViewPager) getView().findViewById(R.id.viewpager);
        mContent.setOffscreenPageLimit(2);
        mIv_system_message_status = (TextView) getView().findViewById(R.id.iv_system_message_status);
        mIv_post_message_status = (TextView) getView().findViewById(R.id.iv_post_message_status);
    }


    private void minitData() {
        mFragments = new ArrayList<BaseFragment>();
        mTitles = new ArrayList<String>();
        MyReplyPostListFragment hf = MyReplyPostListFragment.newInstance("1");
        MyMessagePostListFragment hf1 = MyMessagePostListFragment.newInstance("2");
        MyAllMessageFragment hf2 = MyAllMessageFragment.newInstance("2","3");
        mFragments.add(hf);
        mFragments.add(hf1);
        mFragments.add(hf2);
        mTitles.add("我的评论");
        mTitles.add("回复我的");
        mTitles.add("系统消息");
        mContent.setAdapter(new MainVPAdapter(getChildFragmentManager(), mFragments, mTitles));
        mTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        mTab.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.colorPrimary));
        mTab.setTabMode(TabLayout.MODE_FIXED);
        mTab.setupWithViewPager(mContent);
        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==1) {
                    //更新帖子消息红点状态
                    if (UserUtils.getAllPostMessageStatus()) {
                        mIv_post_message_status.setVisibility(View.INVISIBLE);
                        UserUtils.setAllPostMessageStatus(false);
                    }
                }
                if (tab.getPosition()==2) {
                    //更新系统消息红点状态
                    if (UserUtils.getAllSystemMessageStatus()) {
                        mIv_system_message_status.setVisibility(View.INVISIBLE);
                        UserUtils.setAllSystemMessageStatus(false);
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}
