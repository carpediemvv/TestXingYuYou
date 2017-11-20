package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.user.UserBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class ManagementActivity extends AppCompatActivity {
    private RelativeLayout mSetting;
    private RelativeLayout mUnInstall;
    private ImageView mUserPhoto;
    private RelativeLayout mFeedBack;
    private RelativeLayout mGameUpdate;
    private RelativeLayout mPoJie;
    private RelativeLayout mTool;
    private RelativeLayout mGameDownload;
    private RelativeLayout mAppShare;
    private RelativeLayout mAboutXingYu;
    private Toolbar mToolbar;
    private UserBean mUserBean;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                if (msg.what == 1) {
                    String response = (String) msg.obj;
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(response);
                        String string = jo.getString("status");
                        if (string.equals("1")) {
                            JSONObject ja = jo.getJSONObject("data");
                            Gson gson = new Gson();
                            mUserBean = gson.fromJson(ja.toString(), UserBean.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setValues();
                }
            }

        }
    };
    private TextView mTvNickName;
    private RelativeLayout rl_letter;
    private Button mBtSig;
    private SPUtils mConfig_def;
    private TextView mTvUserIntegral;
    private TextView mTvNickName1;

    private void setValues() {
        UserUtils.setNickName(mUserBean.getNickname());
        UserUtils.setUserPhoto(mUserBean.getHead_image());
        mTvNickName.setText(mUserBean.getNickname());
       // mTvUserIntegral.setText("积分："+mUserBean.getUser_integral());
        Glide.with(getApplication())
                .load(mUserBean.getHead_image())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(ManagementActivity.this))
                .priority(Priority.HIGH )
                .into(mUserPhoto);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        initToolBar();
        initData();
        initUserData();
    }
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
       // mToolbar.setTitle("管理");
        mToolbar.inflateMenu(R.menu.management_activity_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ab_setting:
                        IntentUtils.startActivity(ManagementActivity.this, SettingActivity.class);
                        break;
                    case R.id.action_share:
                        shareUM();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //获取用户信息
    private void initUserData() {
        if (UserUtils.logined()) {
            OkHttpUtils.post()//
                    .url(XingYuInterface.GET_USER_INFO)
                    .addParams("uid", UserUtils.getUserId())
                    .addParams("re_uid", "0")
                    .tag(this)//
                    .build()//
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            mHandler.obtainMessage(1, response).sendToTarget();
                        }
                    });
        }
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        //显示用户信息
        if (UserUtils.logined()) {
            mTvNickName.setText(UserUtils.getNickName());
            if (UserUtils.getUserPhoto().equals("userPhoto")){
                Glide.with(getApplication())
                        .load(R.drawable.ic_user_defalut)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .transform(new GlideCircleTransform(getApplication()))
                        .dontAnimate()
                        .into(mUserPhoto);
            }else {
                Glide.with(getApplication())
                        .load(UserUtils.getUserPhoto())
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .transform(new GlideCircleTransform(getApplication()))
                        .dontAnimate()
                        .into(mUserPhoto);
            }
        }else {
            mTvNickName.setText("点击登陆");
            Glide.with(getApplication())
                    .load(R.drawable.ic_user_defalut)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .transform(new GlideCircleTransform(getApplication()))
                    .dontAnimate()
                    .into(mUserPhoto);
        }
    }

    private void initData() {
       /* //签到
        mBtSig = (Button) findViewById(R.id.bt_sig);
        mConfig_def = new SPUtils("config_def");
        boolean isSig = mConfig_def.getBoolean("isSig", false);
        if (isSig){
            mBtSig.setText("已签到");
            mBtSig.setEnabled(false);
        }else {
            mBtSig.setText("签到");
        }
        mBtSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    sigDay();
                } else {
                    IntentUtils.startActivity(ManagementActivity.this, LoginActivity.class);
                }
            }
        });*/

        mTvNickName = (TextView) findViewById(R.id.user_nickname);
        mTvNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(ManagementActivity.this, UserInfoActivity.class);
                } else {
                    IntentUtils.startActivity(ManagementActivity.this, LoginActivity.class);
                }
            }
        });
        mTvUserIntegral = (TextView) findViewById(R.id.tv_user_integral);

        //登录
        mUserPhoto = (ImageView) findViewById(R.id.user_photo);
        Glide.with(ManagementActivity.this)
                .load(R.mipmap.profile_picture)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(ManagementActivity.this))
                .dontAnimate()
                .into(mUserPhoto);
        mUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(ManagementActivity.this, UserInfoActivity.class);
                } else {
                    IntentUtils.startActivity(ManagementActivity.this, LoginActivity.class);
                }
            }
        });
        //游戏更新
        /*mGameUpdate = (ImageView) findViewById(R.id.image_one);
        mGameUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IntentUtils.startActivity(ManagementActivity.this, UninstallAppActivity.class);
                Toast.makeText(ManagementActivity.this, "功能暂未开放", Toast.LENGTH_SHORT).show();
            }
        });*/
        //加入官群
        mPoJie = (RelativeLayout) findViewById(R.id.rl_eight);
        mPoJie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinQQGroup("YzjlZwrRfUeZN0jnoSF47Kfuz_f2pDXp");
            }
        });
       /* //游戏卸载
        mUnInstall = (RelativeLayout) findViewById(R.id.rl_two);
        mUnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(ManagementActivity.this, UninstallAppActivity.class);
            }
        });*/
        //我的回帖
        mSetting = (RelativeLayout) findViewById(R.id.rl_reply_post);
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    //IntentUtils.startActivity(ManagementActivity.this, MyReplyPostActivity.class);
                } else {
                    IntentUtils.startActivity(ManagementActivity.this, LoginActivity.class);
                }
            }
        });
        //我的收藏
        mSetting = (RelativeLayout) findViewById(R.id.rl_three);
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(ManagementActivity.this, CollectListActivity.class);
                } else {
                    IntentUtils.startActivity(ManagementActivity.this, LoginActivity.class);
                }
            }
        });
        //我的消息
        mSetting = (RelativeLayout) findViewById(R.id.rl_my_message);
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    //IntentUtils.startActivity(ManagementActivity.this, MyMessagePostActivity.class);
                } else {
                    IntentUtils.startActivity(ManagementActivity.this, LoginActivity.class);
                }
            }
        });
        //我的帖子
        mFeedBack = (RelativeLayout) findViewById(R.id.rl_four);
        mFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(ManagementActivity.this, MyPostListActivity.class);
                } else {
                    IntentUtils.startActivity(ManagementActivity.this, LoginActivity.class);
                }
            }
        });

        //私信
        rl_letter = (RelativeLayout) findViewById(R.id.rl_letter);
        rl_letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(ManagementActivity.this, PrivateLetterDetailActivity.class);
                } else {
                    IntentUtils.startActivity(ManagementActivity.this, LoginActivity.class);
                }

            }
        });
        //下载管理
        mGameDownload = (RelativeLayout) findViewById(R.id.rl_seven);
        mGameDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(ManagementActivity.this, DownLoadActivity.class);
            }
        });
        //小工具
        mGameDownload = (RelativeLayout) findViewById(R.id.rl_tools);
        mGameDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManagementActivity.this, "正在开发中...", Toast.LENGTH_SHORT).show();
            }
        });
        //我的礼包
        mTool = (RelativeLayout) findViewById(R.id.rl_nine);
        mTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(ManagementActivity.this, LoginActivity.class);
                } else {
                    IntentUtils.startActivity(ManagementActivity.this, MyGiftActivity.class);
                }
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
        UMImage thumb =  new UMImage(ManagementActivity.this, R.mipmap.icon);
        UMWeb web = new UMWeb("http://www.xingyuyou.com");
        web.setTitle("人生如戏，全靠游戏");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("一个二次元的世界");//描述
        new ShareAction(ManagementActivity.this).withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA)
                .setCallback(umShareTestListener).open();
    }

    private UMShareListener umShareTestListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {

            Toast.makeText(ManagementActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ManagementActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ManagementActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void sigDay() {
        OkHttpUtils.post()//
                .url(XingYuInterface.USER_SIGN)
                .addParams("uid", UserUtils.getUserId())
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(ManagementActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                        mConfig_def.putBoolean("isSig", true);
                        mBtSig.setText("已签到");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
