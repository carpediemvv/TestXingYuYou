package com.xingyuyou.xingyuyou.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.lidroid.xutils.HttpUtils;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.RegexUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class BindPhoneNumberActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mPhoneNumber;
    private Button mSendCode;
    private HttpUtils mHttp;
    private Button mBtRegister;
    private EditText mPhoneCode;
    private EditText mPassword;
    private ValueAnimator mValueAnimator;
    private EditText mEt_password_again;
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                String result = (String) msg.obj;
                JSONObject jsonObject = null;
                try {
                    mValueAnimator = ValueAnimator.ofFloat(60, 0);
                    mValueAnimator.setDuration(60000);
                    mValueAnimator.setInterpolator( new LinearInterpolator());
                    mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mSendCode.setText(Integer.valueOf(((int)(float)animation.getAnimatedValue()))+"s");
                            mSendCode.setEnabled(false);
                            if (animation.getAnimatedValue().toString().equals("0.0")){
                                mSendCode.setText("重新发送");
                                mSendCode.setEnabled(true);
                            }

                        }
                    });
                    mValueAnimator.start();
                    jsonObject = new JSONObject(result);
                    String return_msg = jsonObject.getString("msg");
                    Toast.makeText(BindPhoneNumberActivity.this, return_msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }if (msg.what==1){
                String result = (String) msg.obj;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String return_msg = jsonObject.getString("errorinfo");
                    Toast.makeText(BindPhoneNumberActivity.this, return_msg+"", Toast.LENGTH_SHORT).show();
                    if (jsonObject.getString("status").equals("1")){
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone_number_copy);
        initToolBar();
        initView();
    }
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("绑定手机号");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initView() {
        //发送验证码
        mPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        mSendCode = (Button) findViewById(R.id.bt_send_code);
        mSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoneCode(0);
            }
        });

        //绑定
        mPassword = (EditText) findViewById(R.id.et_password);
        mEt_password_again = (EditText) findViewById(R.id.et_password_again);
        mPhoneCode = (EditText) findViewById(R.id.et_phone_code);
        mBtRegister = (Button) findViewById(R.id.bt_register_button);
        mBtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegister(1);
            }
        });
    }

    /**
     * 发送验证码
     */
    public void getPhoneCode(int code) {
        String phoneCodeText = mPhoneNumber.getText().toString().trim();
        if (RegexUtils.isMobileExact(phoneCodeText)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone", String.valueOf(phoneCodeText));
                jsonObject.put("demand", String.valueOf(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            POST(code, XingYuInterface.SEND_SMS,jsonObject.toString());
        } else {
            Toast.makeText(this, "请检查手机号码是否正确", Toast.LENGTH_SHORT).show();
    }
    }
    private void toRegister(int code) {
        String phoneNumberText = mPhoneNumber.getText().toString().trim();
        String passwordText = mPassword.getText().toString().trim();
        String password_again = mEt_password_again.getText().toString().trim();
        if (!passwordText.equals(password_again)){
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        String phoneCodeText = mPhoneCode.getText().toString().trim();
        if (RegexUtils.isMobileExact(phoneNumberText)&& !StringUtils.isEmpty(passwordText)
                && !StringUtils.isEmpty(phoneCodeText)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("uid", UserUtils.getUserId());
                jsonObject.put("login_type", "1");
                jsonObject.put("qq_secret", "1");
                jsonObject.put("wx_secret", "1");
                jsonObject.put("wb_secret", "1");
                jsonObject.put("mobile", String.valueOf(phoneNumberText));
                jsonObject.put("vcode", String.valueOf(phoneCodeText));
                jsonObject.put("password", String.valueOf(passwordText));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            BindPOST(code,XingYuInterface.UPDATE_RELATION_ACCOUNT,jsonObject.toString());
            Log.e("weiwie", "toRegister: "+jsonObject.toString() );
        } else {
            Toast.makeText(this, "请输入完整正确的注册信息", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Post网络请求
     *
     * @return
     */
    public void POST(final int code, String url, String body) {
        RequestParams params = new RequestParams(url);
        String encodeToString = Base64.encodeToString(body.toString().getBytes(), Base64.DEFAULT);
        params.setBodyContent(encodeToString);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("POST错误信息", arg0.toString());
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onSuccess(String json) {
                try {
                    Log.e("加密返回的json", json);
                    String result = new String(Base64.decode(json, Base64.DEFAULT), "utf-8");

                    if (code==0){
                        mHandler.obtainMessage(0, result).sendToTarget();
                    }
                    if (code==1){
                        mHandler.obtainMessage(1, result).sendToTarget();
                    }

                } catch (Exception e) {
                    Log.e("POST+json成功回调出错：", e.toString());
                }
            }
            @Override
            public boolean onCache(String json) {
                return true;
            }
        });
    }
    /**
     * Post网络请求
     *
     * @return
     */
    public void BindPOST(final int code, String url, String body) {
        RequestParams params = new RequestParams(url);
        params.setBodyContent(body);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("POST错误信息", arg0.toString());
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onSuccess(String json) {
                try {
                    Log.e("绑定第三方返回的json", json);
                    if (code==0){
                        mHandler.obtainMessage(0, json).sendToTarget();
                    }
                    if (code==1){
                        mHandler.obtainMessage(1, json).sendToTarget();
                    }

                } catch (Exception e) {
                    Log.e("POST+json成功回调出错：", e.toString());
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
        DCAgent.resume(this);
        DCPage.onEntry("BindPhoneNumberActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("BindPhoneNumberActivity");
    }
}
