package com.xingyuyou.xingyuyou.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.activity.DownLoadActivity;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.SearchActivity;
import com.xingyuyou.xingyuyou.activity.TalkActivity;
import com.xingyuyou.xingyuyou.activity.UserPageActivity;
import com.xingyuyou.xingyuyou.adapter.MainVPAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/28.
 */
public class GameFragment extends BaseFragment {
    private TabLayout mTab;
    private Toolbar mToolbar;
    private ViewPager mContent;
    private List<BaseFragment> mFragments;
    public Map<String, Integer> mClasses = new HashMap<String, Integer>();
    private List<String> mTitles;
    private boolean isHide;
    private TextView mTvUserAccount;
    private ImageView mIvManage;
    private ImageView mIv_message_status;

    private int num;
    public static GameFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }


    /**/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one, null);
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
        initUserData();
        updatePostAndMessage();
        initMenu();
    }
    private void initMenu() {
        Menu menu = mToolbar.getMenu();
        MenuItem  item = menu.findItem(R.id.ab_send);
        item.setIcon(R.mipmap.ic_action_senmessage_mgg);
        num=getUnread();
        if(num==0){
            item.setIcon(R.mipmap.ic_action_senmessage_mg);
        }else {
            item.setIcon(R.mipmap.ic_action_senmessage_mgg);
        }
    }

    private void initUserData() {
        //登陆账号设置
        mTvUserAccount = (TextView) getView().findViewById(R.id.tv_user_account);
        mTvUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()){
                    IntentUtils.startActivity(mActivity, UserPageActivity.class);
                    mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }else {
                    IntentUtils.startActivity(mActivity, LoginActivity.class);
                }
                mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        mIvManage = (ImageView) getView().findViewById(R.id.iv_manage);
        mIvManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()){
                    IntentUtils.startActivity(mActivity, UserPageActivity.class);
                    mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }else {
                    IntentUtils.startActivity(mActivity, LoginActivity.class);
                }
                mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        mIv_message_status = (ImageView) getView().findViewById(R.id.iv_message_status);
    }

    private void minitView() {
        mToolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        initToolbar();
        mTab = (TabLayout) getView().findViewById(R.id.tabs);
        mContent = (ViewPager) getView().findViewById(R.id.viewpager);
        mContent.setOffscreenPageLimit(2);

    }
    private void updatePostAndMessage() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("updateUserAllMessage");
        intentFilter.addAction("easeUiSendMessage");
        intentFilter.addAction("TwoFragmentSendMessage");

        intentFilter.addAction("ReadAllSendMessage");
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("updateUserAllMessage")) {
                    mIv_message_status.setVisibility(View.VISIBLE);
                }
                if (intent.getAction().equals("easeUiSendMessage"))

                {
                    Menu menu = mToolbar.getMenu();
                    MenuItem  item = menu.findItem(R.id.ab_send);
                    item.setIcon(R.mipmap.ic_action_senmessage_mgg);
                }

                if (intent.getAction().equals("TwoFragmentSendMessage")){
                     Menu menu = mToolbar.getMenu();
                    MenuItem  item = menu.findItem(R.id.ab_send);
                    num=getUnread();
                    if(num==0){
                        item.setIcon(R.mipmap.ic_action_senmessage_mg);
                    }else {
                        item.setIcon(R.mipmap.ic_action_senmessage_mgg);
                    }
                }

                if (intent.getAction().equals("ReadAllSendMessage")){
                    Menu menu = mToolbar.getMenu();
                    MenuItem  item = menu.findItem(R.id.ab_send);
                    num=getUnread();
                    if(num==0){
                        item.setIcon(R.mipmap.ic_action_senmessage_mg);
                    }else {
                        item.setIcon(R.mipmap.ic_action_senmessage_mgg);
                    }
                }
            }
        };
        localBroadcastManager.registerReceiver(br, intentFilter);
    }
    private void initToolbar() {
        mToolbar.inflateMenu(R.menu.all_tab_fragment_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_download:
                        IntentUtils.startActivity(mActivity, DownLoadActivity.class);
                        break;
                    case R.id.ab_search:
                        IntentUtils.startActivity(mActivity, SearchActivity.class);
                        break;
                    case R.id.ab_send:
                        Intent intent=new Intent(mActivity,TalkActivity.class);
                        startActivityForResult(intent,1);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Menu menu = mToolbar.getMenu();
        MenuItem  item = menu.findItem(R.id.ab_send);
        num=getUnread();
        if(num==0){
            item.setIcon(R.mipmap.ic_action_senmessage_mg);
        }else {
            item.setIcon(R.mipmap.ic_action_senmessage_mgg);
        }
        updatePostAndMessage();
        Intent intent=new Intent("GameFragmentSendMessage");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
    public  int getUnread(){
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal-chatroomUnreadMsgCount;
    }
    private void minitData() {
        mFragments = new ArrayList<BaseFragment>();
        mTitles = new ArrayList<String>();
        RecommendWithCoverFragment hf = RecommendWithCoverFragment.newInstance("推荐");
        HotGameWithCoverFragment hf1 = HotGameWithCoverFragment.newInstance("精品");
        NewGameFragment hf2 = NewGameFragment.newInstance("最新");
        mFragments.add(hf);
        mFragments.add(hf1);
        mFragments.add(hf2);
        mTitles.add("次元专区");
        mTitles.add("精品");
        mTitles.add("最新");
        mContent.setAdapter(new MainVPAdapter(getChildFragmentManager(), mFragments, mTitles));
        mTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorControlNormal));
        mTab.setTabMode(TabLayout.MODE_FIXED);
        mTab.setupWithViewPager(mContent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserUtils.logined()) {
            mTvUserAccount.setText(UserUtils.getNickName());
            Glide.with(mActivity)
                    .load(UserUtils.getUserPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .transform(new GlideCircleTransform(mActivity))
                    .priority(Priority.HIGH)
                    .into(mIvManage);
        } else {
            mTvUserAccount.setText("未登陆");
            Glide.with(mActivity)
                    .load(R.drawable.ic_user_defalut)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .transform(new GlideCircleTransform(mActivity))
                    .priority(Priority.HIGH)
                    .into(mIvManage);
        }
        //更新红点状态
        if (UserUtils.getAllSystemMessageStatus()||UserUtils.getAllPostMessageStatus()) {
            mIv_message_status.setVisibility(View.VISIBLE);
        }else {
            mIv_message_status.setVisibility(View.GONE);
        }
    }
}
