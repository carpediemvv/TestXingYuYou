package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.xingyuyou.xingyuyou.Dao.HxEaseuiHelper;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppConstants;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;

public class SplashActivity extends AppCompatActivity {
    /**
     * {
     * "status": 1,
     * "msg": {
     * "qq": "765161218",
     * "weixin": "",
     * "qq_group": "",
     * "network": "www.xingyuyou.com",
     * "icon": null,
     * "version": "1.0",
     * "app_download": "www。百度",
     * "app_name": "星宇游app",
     * "app_welcome": "http://xingyuyou.com/Uploads/Picture/2017-02-16/58a5020da2ba7.jpg",
     * "about_ico": "http://xingyuyou.com/Uploads/Picture/2017-02-16/58a5020da2ba7.jpg"
     * }
     * }
     */
   /* Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if (jsonObject.getString("status").equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("msg");
                        mQq = object.getString("qq");
                        mNetwork = object.getString("network");
                        mAppDownload = object.getString("app_download");
                        mVersionText = object.getString("version");
                        mUpdateInfo = object.getString("app_name");
                        mText.setText("QQ:" + mQq + "\n" + "官网：" + mNetwork);
                        mVersion.setText(mVersionText);
                        if (Integer.valueOf(mVersionText) > mVersionCode) {
                          //  ToUpadte();

                        } else {
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                  //  IntentUtils.startActivityAndFinish(SplashActivity.this, MainActivity.class);
                                }
                            }, 1000);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };*/
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirstOpen = UserUtils.getBoolean(this, AppConstants.FIRST_OPEN);
        if (!isFirstOpen) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        //判断环信是否登录
      if(HxEaseuiHelper.getInstance().isLoggedIn()){

        }else {
            if(UserUtils.logined()){
                signIn(UserUtils.getEmchat_zh(),UserUtils.getEmchat_pw());
            }
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE); //设置无标题
        setContentView(R.layout.activity_splash);
        DCAgent.openPageTrack(false);
        DCAgent.initWithAppIdAndChannelId(this, "CD38DE8CEB84E2A6E97AA697074DDDFEC", "Default");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentUtils.startActivityAndFinish(SplashActivity.this, MainActivity.class);
            }
        },2000);
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

     /*   mVersionCode = AppUtils.getAppVersionCode(this);
        Log.e("banben", mVersionCode + "版本号");
        mText = (TextView) findViewById(R.id.text);
        mVersion = (TextView) findViewById(R.id.version);
        checkUpdate();*/
    }

   /* private void ToUpadte() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_update_app, null);
        builder.setView(view);
        mTvUpdateInfo = (TextView) view.findViewById(R.id.tv_update_info);
        mTvUpdateInfo.setText(mUpdateInfo);
        mBtUpdate = (ProgressButton) view.findViewById(R.id.bt_update);
        mBtUpdate.setTag(0);
        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int)mBtUpdate.getTag()==0){
                    mBtUpdate.setTag(1);
                    toDownload();
                    Toast.makeText(SplashActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(SplashActivity.this, "正在下载，请稍后", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }*/

  /*  private void toDownload() {
        OkHttpUtils//
                .get()//
                .url(mAppDownload)//
                .build()//http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/%E5%8D%95%E6%9C%BA/Iter.apk
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "xingyu.apk")//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(SplashActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {

                        mBtUpdate.setProgress((int)(progress*100));
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.e("dizhi", "onResponse :" + response.getAbsolutePath());
                    }

                    @Override
                    public void onAfter(int id) {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xingyu.apk";
                        Log.e("dizhi", "onResponse :" + path);
                        AppUtils.installApp(SplashActivity.this,path);
                        Toast.makeText(SplashActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

  /*  */



    /**
     * 检查更新 XingYuInterface.ABOUT_US
     *//*
    private void checkUpdate() {
        OkHttpUtils.post()//
                .url(XingYuInterface.ABOUT_US)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(1, response).sendToTarget();
                        Log.e("xiazai", response + ":e");
                    }
                });
    }*/

    /**
     * 登录方法
     */
    private void signIn(String username, String password) {
 /*       mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在登陆，请稍后...");
        mDialog.show();*/
        EMClient.getInstance().login(username, password, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.d("lzan13", "登录成功");
                        // 加载所有群组到内存，如果使用了群组的话
                        // EMClient.getInstance().groupManager().loadAllGroups();
/*
                        // 登录成功跳转界面
                        Intent intent = new Intent(LoginActivity.this, ECMainActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                });
            }

            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //      mDialog.dismiss();
                        Log.d("lzan13", "登录失败 Error code:" + i + ", message:" + s);
                        /**
                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                         */
                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                break;
                            default:
                                break;
                        }
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        DCAgent.resume(this);
        DCPage.onEntry("SplashScreen");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("SplashScreen");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
