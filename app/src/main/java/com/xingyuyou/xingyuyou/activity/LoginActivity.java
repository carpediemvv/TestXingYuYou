package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.fenglinshanhuo.flshsdk.api.FlshSdkPluginApi;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.lidroid.xutils.HttpUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xingyuyou.xingyuyou.App;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppConstants;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;
    private Button mLogin;
    private HttpUtils mHttp;
    private Button mButtonRegister;
    private Toolbar mToolbar;
    private Button mForgetPassword;
    private CustomDialog mLoadingDialog;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mLoadingDialog.dismissDialog();
                String result = (String) msg.obj;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    if (!jsonObject.getString("status").equals("1"))
                        return;
                    JSONObject list = jsonObject.getJSONObject("list");
                    UserUtils.Login(list.getString("id"), list.getString("account"), list.getString("nickname"), list.getString("head_image"));
                    //环信登陆
                 /*   EaseUser  easeUser=new EaseUser(list.getString("nickname"));
                    easeUser.setAvatar(list.getString("head_image"));*/
                    signIn(list.getString("emchat_id"), list.getString("emchat_pwd"));
                    UserUtils.setEmchat_zh(list.getString("emchat_id"));
                    UserUtils.setEmchat_pw(list.getString("emchat_pwd"));
                    UserUtils.setEmid(list.getString("emchat_id"));
                    String id = UserUtils.getEmid();
                    mLoadingDialog.CancelDialog();


                    //直播登录
                    FlshSdkPluginApi.registeredUser(list.getString("id"), list.getString("nickname"), list.getString("head_image"));

                    //如果是第一次启动，则先进入功能引导页
                    boolean isFirstOpen = UserUtils.getBoolean(LoginActivity.this, AppConstants.FIRST_choose);
                    if (!isFirstOpen) {
                        Intent intent = new Intent(LoginActivity.this, ChooseActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private UMShareAPI mShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //用户协议
        TextView textView = (TextView) findViewById(R.id.tv_user_agree);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(LoginActivity.this, ExemptionActivity.class);
            }
        });
        initThreeLogin();
        initToolBar();
        mUserName = (EditText) findViewById(R.id.et_username);
        mUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_login_user);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mUserName.setCompoundDrawables(drawable, null, null, null);
                    mUserName.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_login_user_gery);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mUserName.setCompoundDrawables(drawable, null, null, null);
                    mUserName.setHintTextColor(getResources().getColor(R.color.grey));
                }
            }
        });
        mPassword = (EditText) findViewById(R.id.et_password);
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_login_password);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mPassword.setCompoundDrawables(drawable, null, null, null);
                    mPassword.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_login_password_grey);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mPassword.setCompoundDrawables(drawable, null, null, null);
                    mPassword.setHintTextColor(getResources().getColor(R.color.grey));
                }
            }
        });
        mLogin = (Button) findViewById(R.id.bt_login_button);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfo();
            }
        });
        mForgetPassword = (Button) findViewById(R.id.bt_forgetPassword);
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(LoginActivity.this, ForgetPasswordActivity.class);
            }
        });

        mButtonRegister = (Button) findViewById(R.id.bt_register);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(LoginActivity.this, RegisterActivity.class);
            }
        });

    }

    private void initThreeLogin() {
        mShareAPI = UMShareAPI.get(this);
        ImageView qq = (ImageView) findViewById(R.id.iv_login_qq);
        ImageView wx = (ImageView) findViewById(R.id.iv_login_wx);
        ImageView xl = (ImageView) findViewById(R.id.iv_login_xl);
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAPI.deleteOauth(LoginActivity.this, SHARE_MEDIA.QQ, umDeleteAuthListener);
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
            }
        });
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAPI.deleteOauth(LoginActivity.this, SHARE_MEDIA.WEIXIN, umDeleteAuthListener);
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
            }
        });
        xl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAPI.deleteOauth(LoginActivity.this, SHARE_MEDIA.SINA, umDeleteAuthListener);
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, umAuthListener);
            }
        });
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("登录");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.forget_password_activity_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ab_send:
                        IntentUtils.startActivity(LoginActivity.this, ForgetPasswordActivity.class);
                        break;
                }
                return false;
            }
        });
    }

    public void getUserInfo() {
        String usernameText = mUserName.getText().toString().trim();
        String passwordText = mPassword.getText().toString().trim();
        if ((!StringUtils.isEmpty(usernameText)) && (!StringUtils.isEmpty(passwordText))) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("account", String.valueOf(usernameText));
                jsonObject.put("password", String.valueOf(passwordText));
                jsonObject.put("device_token", App.DEVICETOKEN);
                jsonObject.put("uid", "1");
                jsonObject.put("name", "1");
                jsonObject.put("gender", "1");
                jsonObject.put("iconurl", "1");
                jsonObject.put("login_type", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //账号登录
            toLoginPOST(0, XingYuInterface.USER_LOGIN_TEST, jsonObject.toString());
        } else {
            Toast.makeText(this, "请输入完整登录信息", Toast.LENGTH_SHORT).show();
        }
    }

    public void toLoginPOST(final int code, String url, String body) {
        mLoadingDialog = new CustomDialog(this);
        mLoadingDialog.showDialog();
        org.xutils.http.RequestParams params = new org.xutils.http.RequestParams(url);
        String encodeToString = android.util.Base64.encodeToString(body.toString().getBytes(), android.util.Base64.DEFAULT);
        params.setBodyContent(encodeToString);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                mLoadingDialog.dismissDialog();
            }

            @Override
            public void onFinished() {
                Log.e("POST错误信息", "onFinished");
            }

            @Override
            public void onSuccess(String json) {
                Log.e("thirdLogin", "jsononSuccess: " + json);
                try {
                    String result = new String(android.util.Base64.decode(json, android.util.Base64.DEFAULT), "utf-8");
                    if (code == 0) {
                        Log.e("thirdLogin", "onSuccess: " + result);
                        mHandler.obtainMessage(0, result).sendToTarget();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public boolean onCache(String json) {
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "hahahahah", Toast.LENGTH_SHORT).show();
        DCAgent.resume(this);
        DCPage.onEntry("LoginActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("LoginActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

            Log.e("qqlogin", "onError: " + t.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Log.e("qqlogin", "onCancel: ");
        }
    };
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            Log.e("qqlogin", "onStart: ");
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            // Log.e("qqlogin", "onComplete: " + data.toString());
            if (data != null) {
                onThirdLogin(platform, data);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "登陆出错，请重试", Toast.LENGTH_SHORT).show();
            Log.e("qqlogin", "onError: " + t.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "登陆取消", Toast.LENGTH_SHORT).show();
            Log.e("qqlogin", "onCancel: ");
        }
    };

    /**
     * 第三方登陆
     */
    private void onThirdLogin(SHARE_MEDIA platform, Map<String, String> data) {
        Log.e("thirdLogin", "onThirdLogin: " + platform.toSnsPlatform().mKeyword +
                "\n" + data.get("uid") + "\n" + data.get("name") + "\n" + data.get("gender") + "\n" + data.get("iconurl"));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("account", "1");
            jsonObject.put("password", "1");
            jsonObject.put("device_token", App.DEVICETOKEN);
            jsonObject.put("uid", data.get("uid"));
            jsonObject.put("name", data.get("name"));
            jsonObject.put("gender", data.get("gender"));
            jsonObject.put("iconurl", data.get("iconurl"));
            if (platform.toSnsPlatform().mKeyword.equals("qq")) {
                jsonObject.put("login_type", "2");
            }
            if (platform.toSnsPlatform().mKeyword.equals("wechat")) {
                jsonObject.put("login_type", "3");
            }
            if (platform.toSnsPlatform().mKeyword.equals("sina")) {
                jsonObject.put("login_type", "4");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //账号登录
        toLoginPOST(0, XingYuInterface.USER_LOGIN_TEST, jsonObject.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

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
                                Toast.makeText(LoginActivity.this, "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(LoginActivity.this, "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(LoginActivity.this, "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(LoginActivity.this, "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(LoginActivity.this, "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                Toast.makeText(LoginActivity.this, "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                Toast.makeText(LoginActivity.this, "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(LoginActivity.this, "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(LoginActivity.this, "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(LoginActivity.this, "ml_sign_in_failed code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
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
}
