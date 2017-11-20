package com.xingyuyou.xingyuyou.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.umeng.message.PushAgent;

/**
 * Created by Administrator on 2017/2/20.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //友盟推送需要调用
        PushAgent.getInstance(this).onAppStart();
    }
    public void initData() {
        Log.e("weiwei","initData");
    }
}
