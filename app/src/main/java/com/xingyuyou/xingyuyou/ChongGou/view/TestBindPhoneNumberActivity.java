package com.xingyuyou.xingyuyou.ChongGou.view;

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
import com.xingyuyou.xingyuyou.ChongGou.presenter.BindPhoneNumberPresenter;
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

import java.util.ArrayList;

public class TestBindPhoneNumberActivity extends AppCompatActivity implements View.OnClickListener, IBindPhoneNumberView {

    private Toolbar mToolbar;
    private EditText mPhoneNumber;
    private Button mSendCode;
    private HttpUtils mHttp;
    private Button mBtRegister;
    private EditText mPhoneCode;
    private EditText mPassword;
    private ValueAnimator mValueAnimator;
    private EditText mEt_password_again;
    private BindPhoneNumberPresenter mBindPhoneNumberPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone_number_copy);
        initToolBar();
        initView();
        mBindPhoneNumberPresenter = new BindPhoneNumberPresenter(this);
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
        mSendCode.setOnClickListener(this);

        //绑定
        mPassword = (EditText) findViewById(R.id.et_password);
        mEt_password_again = (EditText) findViewById(R.id.et_password_again);
        mPhoneCode = (EditText) findViewById(R.id.et_phone_code);
        mBtRegister = (Button) findViewById(R.id.bt_register_button);
        mBtRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_code:
                mBindPhoneNumberPresenter.getRegisterCode(getPhoneNumber());
                break;
            case R.id.bt_register_button:
                mBindPhoneNumberPresenter.BindPhoneNumberCode(getPhoneNumber(),getRegisterCode(),getPassword());
                break;
            default:
                break;
        }
    }

    @Override
    public String getPhoneNumber() {
        return mPhoneNumber.getText().toString().trim();
    }

    @Override
    public String getRegisterCode() {
        return mPhoneCode.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return mPassword.getText().toString().trim();
    }

    @Override
    public String getPasswordAgain() {
        return mEt_password_again.getText().toString().trim();
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBindPhoneNumberPresenter.onDestroy();
    }
}
