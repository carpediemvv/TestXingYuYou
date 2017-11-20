package com.xingyuyou.xingyuyou.login.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.login.presenter.LoginPresenter;

/**
 * Created by 24002 on 2017/8/31.
 */

public class WLoginActivity extends AppCompatActivity implements ILoginView {

    private EditText mEt_username;
    private EditText mEt_password;
    private Button mBt_login_button;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEt_username = (EditText) findViewById(R.id.et_username);
        mEt_password = (EditText) findViewById(R.id.et_password);
        mBt_login_button = (Button) findViewById(R.id.bt_login_button);

        mLoginPresenter = new LoginPresenter(this);

        //点击登陆
        mBt_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogin();
            }
        });
    }

    @Override
    public void toLogin() {
        mLoginPresenter.toLogin(mEt_username.getText().toString(),mEt_password.getText().toString());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void showLoading(String msg, int progress) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void showErrorMsg(String msg, String content) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
