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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.common.utils.StorageUtils;
import com.aliyun.demo.crop.AliyunVideoCrop;
import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.aliyun.struct.common.ScaleMode;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.FlashType;
import com.aliyun.struct.snap.AliyunSnapVideoParam;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.glide.Utils;
import com.xingyuyou.xingyuyou.activity.DownLoadActivity;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.PostingActivity;
import com.xingyuyou.xingyuyou.activity.SearchCommuActivity;
import com.xingyuyou.xingyuyou.activity.TalkActivity;
import com.xingyuyou.xingyuyou.activity.UserPageActivity;
import com.xingyuyou.xingyuyou.activity.VideoLongActivity;
import com.xingyuyou.xingyuyou.adapter.MainVPAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.views.FinishProjectPopupWindows;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xingyuyou.xingyuyou.activity.VideoActivity.REQUEST_RECORD;

/**
 * Created by Administrator on 2016/6/28.
 */
public class TwoFragment extends BaseFragment {
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
    String[] eff_dirs;
    private VideoQuality videoQuality;
    private FinishProjectPopupWindows mFinishProjectPopupWindow;
    private View.OnClickListener itemsOnClick;
    private ScaleMode cropMode = AliyunVideoCrop.SCALE_CROP;
    String path;
    private String imagePath;
    private MainVPAdapter mainVPAdapter;
    private VideoQuality videoQulity;


    public static TwoFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        TwoFragment fragment = new TwoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two, null);
    }

    @Override
    protected View initView() {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAssetPath();
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


    private void initAssetPath() {
        String path = StorageUtils.getCacheDirectory(mActivity).getAbsolutePath() + File.separator + Utils.QU_NAME + File.separator;
        eff_dirs = new String[]{
                null,
                path + "filter/chihuang",
                path + "filter/fentao",
                path + "filter/hailan",
                path + "filter/hongrun",
                path + "filter/huibai",
                path + "filter/jingdian",
                path + "filter/maicha",
                path + "filter/nonglie",
                path + "filter/rourou",
                path + "filter/shanyao",
                path + "filter/xianguo",
                path + "filter/xueli",
                path + "filter/yangguang",
                path + "filter/youya",
                path + "filter/zhaoyang"
        };
    }


    private void initUserData() {
        //登陆账号设置
        mTvUserAccount = (TextView) getView().findViewById(R.id.tv_user_account);
        mTvUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(mActivity, UserPageActivity.class);
                    mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    IntentUtils.startActivity(mActivity, LoginActivity.class);
                }
            }
        });
        mIvManage = (ImageView) getView().findViewById(R.id.iv_manage);
        mIvManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(mActivity, UserPageActivity.class);
                    mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    IntentUtils.startActivity(mActivity, LoginActivity.class);
                }
            }
        });
        mIv_message_status = (ImageView) getView().findViewById(R.id.iv_message_status);

    }

    private void updatePostAndMessage() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("updateFragment");
        intentFilter.addAction("updateUserAllMessage");
        intentFilter.addAction("easeUiSendMessage");
        intentFilter.addAction("GameFragmentSendMessage");
        intentFilter.addAction("ReadAllSendMessage");
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("updateFragment")) {
                    mContent.setCurrentItem(2);
                }
                if (intent.getAction().equals("updateUserAllMessage")) {
                    mIv_message_status.setVisibility(View.VISIBLE);
                }

                if (intent.getAction().equals("easeUiSendMessage"))

                {
                    Menu menu = mToolbar.getMenu();
                    MenuItem  item = menu.findItem(R.id.ab_send);
                    item.setIcon(R.mipmap.ic_action_senmessage_mgg);
                }
                if (intent.getAction().equals("GameFragmentSendMessage"))
                {
                    Menu menu = mToolbar.getMenu();
                    MenuItem  item = menu.findItem(R.id.ab_send);
                    num=getUnread();
                    if(num==0){
                        item.setIcon(R.mipmap.ic_action_senmessage_mg);
                    }else {
                        item.setIcon(R.mipmap.ic_action_senmessage_mgg);
                    }
                }

                if (intent.getAction().equals("ReadAllSendMessage"))
                {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Menu menu = mToolbar.getMenu();
        MenuItem  item = menu.findItem(R.id.ab_send);
        item.setIcon(R.mipmap.ic_action_senmessage_mgg);
        num=getUnread();
        if(num==0){
            item.setIcon(R.mipmap.ic_action_senmessage_mg);
        }else {
            item.setIcon(R.mipmap.ic_action_senmessage_mgg);
        }

        Intent intent=new Intent("TwoFragmentSendMessage");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }


    /*    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(getActivity());
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("updateFragment");
        intentFilter.addAction("updateUserAllMessage");
        intentFilter.addAction("easeUiSendMessage");
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("updateFragment")) {
                    mContent.setCurrentItem(2);
                }
                if (intent.getAction().equals("updateUserAllMessage")) {
                    mIv_message_status.setVisibility(View.VISIBLE);
                }

                if (intent.getAction().equals("easeUiSendMessage"))

                {
                    MenuItem menuItem =menu.findItem(R.id.ab_send);
                    menuItem.setIcon(R.mipmap.ic_launcher);
                    Toast.makeText(getActivity(),"我接收到消息了",Toast.LENGTH_SHORT).show();
                }
            }

        };
        localBroadcastManager.registerReceiver(br, intentFilter);

    }*/

    private void minitView() {
        mToolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        initToolbar();
        mTab = (TabLayout) getView().findViewById(R.id.tabs);
        mContent = (ViewPager) getView().findViewById(R.id.viewpager);
        mContent.setOffscreenPageLimit(2);
        //发帖按钮
        final ImageView floatingActionButton = (ImageView) getView().findViewById(R.id.fab_add_comment);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFinishProjectPopupWindow = new FinishProjectPopupWindows(mActivity,floatingActionButton,itemsOnClick);
                mFinishProjectPopupWindow.showAtLocation(mActivity.findViewById(R.id.fab_add_comment),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                floatingActionButton.setVisibility(View.GONE);
            }
        });

        itemsOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinishProjectPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.popupwindow_Button_longvideo:
                        if (UserUtils.logined()) {
                            startActivity(new Intent(mActivity, VideoLongActivity.class));
                        } else {
                            IntentUtils.startActivity(mActivity, LoginActivity.class);
                        }
                        break;
                    case R.id.popupwindow_Button_saveProject:
                        if (UserUtils.logined()) {
                            IntentUtils.startActivity(mActivity, PostingActivity.class);
                        } else {
                            IntentUtils.startActivity(mActivity, LoginActivity.class);
                        }
                        break;
                    case R.id.popupwindow_Button_abandonProject:
                        if (UserUtils.logined()) {
                          AliyunVideoRecorder.startRecordForResult(mActivity,REQUEST_RECORD,aliyunSnap());
                        } else {
                            IntentUtils.startActivity(mActivity, LoginActivity.class);
                        }
                        break;
                }
            }
        };


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
                        IntentUtils.startActivity(mActivity, SearchCommuActivity.class);
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


    //    拉起录制
    public AliyunSnapVideoParam aliyunSnap(){
        AliyunSnapVideoParam recordParam = new AliyunSnapVideoParam.Builder()
                //设置录制分辨率，目前支持360p，480p，540p，720p
                .setResulutionMode(AliyunSnapVideoParam.RESOLUTION_360P)
                //设置视频比例，目前支持1:1,3:4,9:16
                .setRatioMode(AliyunSnapVideoParam.RATIO_MODE_9_16)
                .setRecordMode(AliyunSnapVideoParam.RECORD_MODE_AUTO) //设置录制模式，目前支持按录，点录和混合模式
                .setFilterList(eff_dirs) //设置滤镜地址列表,具体滤镜接口接收的是一个滤镜数组
                .setBeautyLevel(80) //设置美颜度
                .setBeautyStatus(true) //设置美颜开关
                .setCameraType(CameraType.FRONT) //设置前后置摄像头
                .setFlashType(FlashType.ON) // 设置闪光灯模式
                .setNeedClip(true) //设置是否需要支持片段录制
                .setMaxDuration(30000) //设置最大录制时长 单位毫秒
                .setMinDuration(2000) //设置最小录制时长 单位毫秒
                .setVideQuality(videoQuality) //设置视频质量
                .setGop(5) //设置关键帧间隔
               // .setCropMode(ScaleMode.PS)
                .build();
        return recordParam;
    }
    private void minitData() {
        mFragments = new ArrayList<BaseFragment>();
        mTitles = new ArrayList<String>();
        CommHotFragment hf = CommHotFragment.newInstance("1");
        CommBestFragment hf1 = CommBestFragment.newInstance("2");
        CommNewFragment hf2 = CommNewFragment.newInstance("3");
        mFragments.add(hf);
        mFragments.add(hf1);
        mFragments.add(hf2);
        mTitles.add("推荐");
        mTitles.add("关注");
        mTitles.add("最新");
        mainVPAdapter = new MainVPAdapter(getChildFragmentManager(), mFragments, mTitles);
        mContent.setAdapter(mainVPAdapter);
        mTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorControlNormal));
        mTab.setTabMode(TabLayout.MODE_FIXED);
        mTab.setupWithViewPager(mContent);
    }

    @Override
    public void onResume() {
        super.onResume();
        DCPage.onEntry("TwoFragment");
        if (UserUtils.logined()) {
            mTvUserAccount.setText(UserUtils.getNickName());
            Glide.with(mActivity)
                    .load(UserUtils.getUserPhoto())
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
        num=getUnread();
        if (UserUtils.getAllSystemMessageStatus()||UserUtils.getAllPostMessageStatus()||num>0) {
            mIv_message_status.setVisibility(View.VISIBLE);
        }else {
            mIv_message_status.setVisibility(View.GONE);
        }
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
    @Override
    public void onPause() {
        super.onPause();
        DCPage.onExit("TwoFragment");
    }
}
