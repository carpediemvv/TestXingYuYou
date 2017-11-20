package com.xingyuyou.xingyuyou.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.CameraUtils;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.user.GetUserData;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.user.UpLoadUserBg;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.RetrofitServiceManager;
import com.xingyuyou.xingyuyou.Utils.glide.ColorFilterTransformation;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.MainContentVPAdapter;
import com.xingyuyou.xingyuyou.bean.DefultData;
import com.xingyuyou.xingyuyou.bean.user.UserInfo;
import com.xingyuyou.xingyuyou.fragment.GameFragment;
import com.xingyuyou.xingyuyou.fragment.UserPageMessageFragment;
import com.xingyuyou.xingyuyou.fragment.UserPagePostFragment;
import com.xingyuyou.xingyuyou.weight.CustomViewPager;
import com.yalantis.ucrop.UCrop;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserPageActivity extends AppCompatActivity {

    private ArrayList<Fragment> mFragments;
    private RelativeLayout mRl_message;
    private RelativeLayout mRl_post;
    private CustomViewPager mCustomViewPager;
    private TextView mUser_nickname;
    private TextView mTv_user_jn_number;
    private TextView mTv_user_signatures;
    private ImageView mIv_user_sex;
    private ImageView mIv_user_photo;
    private TextView mTv_fans_number;
    private TextView mTv_attention_number;
    private TextView mTv_jn_number;
    private ImageView mIv_user_page_bg;
    private Toolbar mToolbar;
    private NestedScrollView mNestedScrollView;
    private static final int REQUEST_IMAGE = 147;
    private List<String> mPath;
    private TextView mToolbar_title;
    private HashMap<String, String> mCorpImageMap;
    private int num;
    private Button iv_message_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //StatusBarUtil.setTranslucentForImageView(UserPageActivity.this, 0, null);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initToolbar();
        initView();
        initData();
        toLiuYan();
        loginOut();
        num = getUnread();
        if(num ==0){
            iv_message_status.setVisibility(View.GONE);
        }else {
            if(num>99){
                iv_message_status.setVisibility(View.VISIBLE);
                iv_message_status.setText("...");
            }else {
                iv_message_status.setVisibility(View.VISIBLE);
                iv_message_status.setText(num+"");
            }
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
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ImageView iv_toolbar_back = (ImageView) findViewById(R.id.iv_toolbar_back);
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       /*CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) mToolbar.getLayoutParams();
        layoutParams.setMargins(0, BarUtils.getStatusBarHeight(this), 0, 0);
        mToolbar.setLayoutParams(layoutParams);*/

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRangle = appBarLayout.getTotalScrollRange();
                //初始verticalOffset为0，不能参与计算。
                if (verticalOffset == 0) {
                    mToolbar_title.setAlpha(0);
                } else {
                    //保留一位小数
                    float alpha = Math.abs((verticalOffset * 10) / scrollRangle);
                    mToolbar_title.setAlpha((alpha / 10));
                }
            }
        });

    }

    private void initData() {
        RetrofitServiceManager.getInstance()
                .create(GetUserData.class)
                .getUserData(UserUtils.getUserId(), "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull UserInfo userInfo) {
                        setValues(userInfo);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        //获取更新的消息
        OkHttpUtils.post()//
                .url(XingYuInterface.SYSTEM_REQUEST_STATUS)
                .addParams("uid", UserUtils.getUserId())
                .addParams("system_count", UserUtils.getAllSystemMessageCount())
                .addParams("message_count", UserUtils.getAllPostMessageCount())
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            UserUtils.setAllSystemMessageCount(jsonObject.getString("system_all_num"));
                            UserUtils.setAllPostMessageCount(jsonObject.getString("message_all_num"));
                            if (jsonObject.getString("update_all_state").equals("1")) {
                                //将系统消息更新状态保存下来
                                if (!jsonObject.getString("update_system_num").equals("0")) {
                                    UserUtils.setAllSystemMessageStatus(true);
                                    UserUtils.setUpdateSystemMessageCount(jsonObject.getString("update_system_num"));
                                }
                                //将帖子消息更新状态保存下来
                                if (!jsonObject.getString("update_message_num").equals("0")) {
                                    UserUtils.setAllPostMessageStatus(true);
                                    UserUtils.setUpdatePostMessageCount(jsonObject.getString("update_message_num"));
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setValues(UserInfo userInfo) {
        if (userInfo.getStatus() == 0 || userInfo == null)
            return;
        mToolbar_title.setText(userInfo.getData().getNickname());
        mUser_nickname.setText(userInfo.getData().getNickname());
        mTv_user_jn_number.setText("胶囊号:" + userInfo.getData().getAccount());
        mTv_user_signatures.setText(userInfo.getData().getExplain());
        mTv_fans_number.setText(userInfo.getData().getFans_num());
        mTv_attention_number.setText(userInfo.getData().getFollow_num());
        mTv_jn_number.setText(userInfo.getData().getUser_integral());
        if (userInfo.getData().getSex().equals("1")) {
            mIv_user_sex.setImageResource(R.drawable.ic_action_women);
        } else {
            mIv_user_sex.setImageResource(R.drawable.ic_action_man);
        }
        Glide.with(getApplication())
                .load(userInfo.getData().getHead_image())
                .transform(new GlideCircleTransform(UserPageActivity.this))
                .priority(Priority.HIGH)
                .into(mIv_user_photo);
        Glide.with(getApplication())
                .load(userInfo.getData().getBack_ground())
                .bitmapTransform(new ColorFilterTransformation(this, R.color.black))
                .priority(Priority.HIGH)
                .into(mIv_user_page_bg);
    }

    /**
     * 监听留言事件
     */
    private void toLiuYan() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("toLiuYan");
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mCustomViewPager.setCurrentItem(1);
            }

        };
        localBroadcastManager.registerReceiver(br, intentFilter);
    }
    /**
     * 监听退出登陆事件
     */
    private void loginOut() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("loginOut");
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                UserPageActivity.this.finish();
            }

        };
        localBroadcastManager.registerReceiver(br, intentFilter);
    }
    private void initView() {
        iv_message_status = (Button) findViewById(R.id.iv_message_status);
/*        Button bu_message= (Button) findViewById(R.id.bt_message);
        bu_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               IntentUtils.startActivity(UserPageActivity.this, FriendActivity.class);
            }
        });*/
        mNestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        mNestedScrollView.setFillViewport(true);
        //背景墙
        mIv_user_page_bg = (ImageView) findViewById(R.id.iv_user_page_bg);
        mIv_user_page_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String s = outDir.getAbsolutePath() + "/JiaoNang";
                FileUtils.deleteDir(s);
                MultiImageSelector.create()
                        .showCamera(true)
                        .single()
                        .start(UserPageActivity.this, REQUEST_IMAGE);
            }
        });
        //设置
        ImageView iv_setting = (ImageView) findViewById(R.id.iv_setting);
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(UserPageActivity.this, SettingActivity.class);
            }
        });
        ImageView iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareUM();
            }
        });
        //头部用户信息
        mToolbar_title = (TextView) findViewById(R.id.toolbar_title);
        mUser_nickname = (TextView) findViewById(R.id.user_nickname);
        mTv_user_jn_number = (TextView) findViewById(R.id.tv_user_JN_number);
        mTv_user_signatures = (TextView) findViewById(R.id.tv_user_signatures);
        mIv_user_sex = (ImageView) findViewById(R.id.iv_user_sex);
        mIv_user_photo = (ImageView) findViewById(R.id.iv_user_photo);
        mIv_user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(UserPageActivity.this, UserInfoActivity.class);
                } else {
                    IntentUtils.startActivity(UserPageActivity.this, LoginActivity.class);
                }
            }
        });

        //私信
        Button bt_message = (Button) findViewById(R.id.bt_message);
        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(UserPageActivity.this, "正在开发中", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UserPageActivity.this,TalkActivity.class);
                startActivityForResult(intent,1);
            }
        });

        //中间三个粉丝关注胶囊
        mTv_fans_number = (TextView) findViewById(R.id.tv_fans_number);
        mTv_attention_number = (TextView) findViewById(R.id.tv_attention_number);
        mTv_jn_number = (TextView) findViewById(R.id.tv_JN_number);
        LinearLayout ll_JN = (LinearLayout) findViewById(R.id.ll_JN);
        ll_JN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        LinearLayout ll_fans = (LinearLayout) findViewById(R.id.ll_fans);
        ll_fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("re_uid",UserUtils.getUserId());
                intent.putExtra("type","2");
                intent.putExtra("tag","1");
                intent.setClass(UserPageActivity.this,ConcernListActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout ll_attention = (LinearLayout) findViewById(R.id.ll_attention);
        ll_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("re_uid",UserUtils.getUserId());
                intent.putExtra("type","1");
                intent.putExtra("tag","1");
                intent.setClass(UserPageActivity.this,FansListActivity.class);
                startActivity(intent);
            }
        });
        //中间位置四个button
        final ImageView iv_message = (ImageView) findViewById(R.id.iv_message);
        final ImageView iv_post = (ImageView) findViewById(R.id.iv_post);
        mRl_message = (RelativeLayout) findViewById(R.id.rl_message);
        mRl_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomViewPager.setCurrentItem(0);
            }
        });
        mRl_post = (RelativeLayout) findViewById(R.id.rl_post);
        mRl_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomViewPager.setCurrentItem(1);
            }
        });
        RelativeLayout rl_gift = (RelativeLayout) findViewById(R.id.rl_gift);
        rl_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(UserPageActivity.this, LoginActivity.class);
                } else {
                    IntentUtils.startActivity(UserPageActivity.this, MyGiftActivity.class);
                }
            }
        });
        RelativeLayout rl_tools = (RelativeLayout) findViewById(R.id.rl_tools);
        rl_tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(UserPageActivity.this, "正在开发中", Toast.LENGTH_SHORT).show();
                mCustomViewPager.setCurrentItem(2);
            }
        });


        //底部内容布局
        mFragments = new ArrayList<>();
        mFragments.add(UserPageMessageFragment.newInstance("信息"));
        mFragments.add(UserPagePostFragment.newInstance("帖子"));
        mFragments.add(GameFragment.newInstance("游戏"));
        MainContentVPAdapter adapter = new MainContentVPAdapter(getSupportFragmentManager(), mFragments);
        mCustomViewPager = (CustomViewPager) findViewById(R.id.main_fragment);
        mCustomViewPager.setAdapter(adapter);
        mCustomViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               /* if (position == 0) {
                    iv_message.setImageResource(R.drawable.ic_action_message_press);
                    iv_post.setImageResource(R.drawable.ic_action_post);
                } else {
                    iv_post.setImageResource(R.drawable.ic_action_post_press);
                    iv_message.setImageResource(R.drawable.ic_action_message);
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //加入管群
        ImageView iv_qq_group = (ImageView) findViewById(R.id.iv_qq_group);
        iv_qq_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinQQGroup("YzjlZwrRfUeZN0jnoSF47Kfuz_f2pDXp");
            }
        });
    }

    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    private void shareUM() {
        UMImage thumb = new UMImage(UserPageActivity.this, R.mipmap.icon);
        UMWeb web = new UMWeb("http://xingyuyou.com/app.php/Share/download");
        web.setTitle("人生如戏，全靠游戏");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("一个二次元的世界");//描述
        new ShareAction(UserPageActivity.this).withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setCallback(umShareTestListener).open();
    }

    private UMShareListener umShareTestListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            Toast.makeText(UserPageActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(UserPageActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(UserPageActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserUtils.getUserPhoto().equals("未登录")) {
            Glide.with(getApplication())
                    .load(R.drawable.ic_user_defalut)
                    .transform(new GlideCircleTransform(UserPageActivity.this))
                    .priority(Priority.HIGH)
                    .into(mIv_user_photo);
        } else {
            Glide.with(getApplication())
                    .load(UserUtils.getUserPhoto())
                    .transform(new GlideCircleTransform(UserPageActivity.this))
                    .priority(Priority.HIGH)
                    .into(mIv_user_photo);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      int  num = getUnread();
        if(num>99){
            iv_message_status.setVisibility(View.VISIBLE);
            iv_message_status.setText("...");
        }else if(num>0){
            iv_message_status.setVisibility(View.VISIBLE);
            iv_message_status.setText(num+"");
        }else {
            Intent intent=new Intent("ReadAllSendMessage");
            LocalBroadcastManager.getInstance(UserPageActivity.this).sendBroadcast(intent);
            iv_message_status.setVisibility(View.GONE);
        }
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                CameraUtils.startUCrop(this, mPath.get(0), 100, 16, 9);
                mCorpImageMap = new HashMap<>();
            }
        }
        //裁剪第一张图片
        if (resultCode == RESULT_OK && requestCode == 100) {
            Uri resultUri = UCrop.getOutput(data);
            CameraUtils.startUCrop(this, mPath.get(0), 101, 1, 1);
            mCorpImageMap.put("image", resultUri.getPath());
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
        //根据intent有没有数据进行二次裁剪返回拦截，进行第一次重新裁剪
        if (resultCode == RESULT_CANCELED && requestCode == 101 && (data == null)) {
            CameraUtils.startUCrop(this, mPath.get(0), 100, 16, 9);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
        //裁剪第二张图片
        if (resultCode == RESULT_OK && requestCode == 101) {
            Uri resultUri = UCrop.getOutput(data);
            mCorpImageMap.put("reimage", resultUri.getPath());
            //设置背景
            Glide.with(getApplication())
                    .load(mCorpImageMap.get("image"))
                    .bitmapTransform(new ColorFilterTransformation(this, R.color.black))
                    .diskCacheStrategy(DiskCacheStrategy.NONE
                    )
                    .into(mIv_user_page_bg);
            //上传背景图片
            uploadImage();
            //uploadImageOkHttpUtils();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }

    }

    private void uploadImage() {
        File file1 = new File(mCorpImageMap.get("image"));
        File file2 = new File(mCorpImageMap.get("reimage"));
        RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), UserUtils.getUserId());
        MultipartBody.Part[] file = new MultipartBody.Part[2];
        file[0] = MultipartBody.Part.createFormData("image", file1.getName(), requestFile1);
        file[1] = MultipartBody.Part.createFormData("reimage", file2.getName(), requestFile2);
        RetrofitServiceManager.getInstance()
                .create(UpLoadUserBg.class)
                .updateImage(file, tokenBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefultData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull DefultData defultData) {
                        if (defultData.getStatus()==1)
                            Toast.makeText(UserPageActivity.this, "背景墙修改成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }

}
