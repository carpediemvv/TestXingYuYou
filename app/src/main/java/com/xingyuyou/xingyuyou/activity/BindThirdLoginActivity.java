package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xingyuyou.xingyuyou.ChongGou.view.TestBindPhoneNumberActivity;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.user.UserBean;
import com.xingyuyou.xingyuyou.bean.user.thirdLoginBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

public class BindThirdLoginActivity extends AppCompatActivity {
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    String string = jo.getString("status");
                    if (string.equals("1")) {
                        JSONObject ja = jo.getJSONObject("data");
                        Gson gson = new Gson();
                        mThirdLoginBean = gson.fromJson(ja.toString(), thirdLoginBean.class);
                        setValues();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 2) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    String errorinfo = jo.getString("errorinfo");
                    Toast.makeText(BindThirdLoginActivity.this, errorinfo, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private Toolbar mToolbar;
    private TextView mTv_qq;
    private TextView mTv_wb;
    private TextView mTv_phone;
    private TextView mTv_wx;
    private thirdLoginBean mThirdLoginBean;
    private UMShareAPI mShareAPI;
    private RelativeLayout mRl_bind_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_third_login);
        mShareAPI = UMShareAPI.get(this);
        initView();
        initBindStatus();
    }

    /**
     * 初始化绑定状态
     */
    private void initBindStatus() {
        OkHttpUtils.post()//
                .url(XingYuInterface.RELATION_ACCOUNT)
                .addParams("uid", UserUtils.getUserId())
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(1, response).sendToTarget();
                        Log.e("weiwei", "onResponse: " + response);
                    }
                });
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("绑定账号");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //展示绑定的信息
        mTv_qq = (TextView) findViewById(R.id.tv_qq);
        mTv_wb = (TextView) findViewById(R.id.tv_wb);
        mTv_phone = (TextView) findViewById(R.id.tv_phone);
        mTv_wx = (TextView) findViewById(R.id.tv_wx);


        //绑定qq
        RelativeLayout rl_bind_qq = (RelativeLayout) findViewById(R.id.rl_bind_qq);
        rl_bind_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAPI.deleteOauth(BindThirdLoginActivity.this, SHARE_MEDIA.QQ, umDeleteAuthListener);
                mShareAPI.getPlatformInfo(BindThirdLoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
            }
        });
        RelativeLayout rl_bind_wb = (RelativeLayout) findViewById(R.id.rl_bind_wb);
        rl_bind_wb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAPI.deleteOauth(BindThirdLoginActivity.this, SHARE_MEDIA.SINA, umDeleteAuthListener);
                mShareAPI.getPlatformInfo(BindThirdLoginActivity.this, SHARE_MEDIA.SINA, umAuthListener);
            }
        });
        //手机号绑定
        mRl_bind_phone = (RelativeLayout) findViewById(R.id.rl_bind_phone);

        RelativeLayout rl_bind_wx = (RelativeLayout) findViewById(R.id.rl_bind_wx);
        rl_bind_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAPI.deleteOauth(BindThirdLoginActivity.this, SHARE_MEDIA.WEIXIN, umDeleteAuthListener);
                mShareAPI.getPlatformInfo(BindThirdLoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
            }
        });
    }

    //展示绑定信息
    private void setValues() {
        if (!mThirdLoginBean.getQq_secret().equals("1")) {
            mTv_qq.setText("绑定");
        }
        if (!mThirdLoginBean.getWb_secret().equals("1")) {
            mTv_wb.setText("绑定");
        }
        if (!mThirdLoginBean.getAccount().equals("1")) {
            mTv_phone.setText("绑定");
        }
        if (!mThirdLoginBean.getWx_secret().equals("1")) {
            mTv_wx.setText("绑定");
        }
        mRl_bind_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mThirdLoginBean.getAccount().equals("1")) {
                    Toast.makeText(BindThirdLoginActivity.this, "已经绑定", Toast.LENGTH_SHORT).show();
                } else {
                    IntentUtils.startActivity(BindThirdLoginActivity.this, BindPhoneNumberActivity.class);
                }
            }
        });

    }
    private UMAuthListener umDeleteAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            Log.e("qqlogin", "onStart: ");
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.e("thirdLogin", "onComplete:解绑 ");

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
           // Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
            Log.e("qqlogin", "onError: " + t.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
           // Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
            Log.e("qqlogin", "onCancel: ");
        }
    };

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            Log.e("thirdLogin", "onStart: ");
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.e("thirdLogin", "onComplete: ");
            if (data != null) {
                Log.e("thirdLogin", "onComplete: " + data.toString());
                onThirdBind(platform, data);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            //Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
            Log.e("thirdLogin", "onError: " + t.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
           // Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
            Log.e("thirdLogin", "onCancel: ");
        }
    };

    /**
     * 绑定第三方
     *
     * @param platform
     * @param data
     */
    private void onThirdBind(SHARE_MEDIA platform, Map<String, String> data) {
        String login_type = "1";
        String qq_secret = "1";
        String wx_secret = "1";
        String wb_secret = "1";
        if (platform.toSnsPlatform().mKeyword.equals("qq")) {
            login_type = "2";
            qq_secret = data.get("uid");
        }
        if (platform.toSnsPlatform().mKeyword.equals("wechat")) {
            login_type = "3";
            wx_secret = data.get("uid");
        }
        if (platform.toSnsPlatform().mKeyword.equals("sina")) {
            login_type = "4";
            wb_secret = data.get("uid");
        }
        OkHttpUtils.post()//
                .url(XingYuInterface.UPDATE_RELATION_ACCOUNT)
                .addParams("uid", UserUtils.getUserId())
                .addParams("login_type", login_type)
                .addParams("qq_secret", qq_secret)
                .addParams("wx_secret", wx_secret)
                .addParams("wb_secret", wb_secret)
                .addParams("mobile", "1")
                .addParams("vcode", "1")
                .addParams("password", "1")
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(2, response).sendToTarget();
                        initBindStatus();
                        Log.e("thirdLogin", "onResponse: " + response);
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        DCAgent.resume(this);
        DCPage.onEntry("BindThirdLoginActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("BindThirdLoginActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
