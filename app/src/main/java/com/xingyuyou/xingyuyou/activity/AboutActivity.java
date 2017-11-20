package com.xingyuyou.xingyuyou.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.RetrofitServiceManager;
import com.xingyuyou.xingyuyou.login.api.ToLogin;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AboutActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //注释钱
        setContentView(R.layout.activity_about);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(AboutActivity.this, ExemptionActivity.class);
            }
        });
        initView();
        //getPhoneCode();
        getLayoutInflater();
        //test ChangeList22222
        /*
        测试Changlist
         */
    }


    private void getPhoneCode() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("account", String.valueOf("18291910677"));
            jsonObject.put("password", String.valueOf("pppppp"));
            jsonObject.put("device_token", "787778");
            jsonObject.put("uid", "1");
            jsonObject.put("name", "1");
            jsonObject.put("gender", "1");
            jsonObject.put("iconurl", "1");
            jsonObject.put("login_type", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String encodeToString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.DEFAULT);
        RetrofitServiceManager.getInstance()
                .create(ToLogin.class)
                .tologin(encodeToString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("bindlogin", "onSubscribe: " );
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.e("bindlogin", "onNext: "+s );
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("bindlogin", "onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("bindlogin", "onComplete: " );
                    }
                });
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("关于星宇游");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        DCAgent.resume(this);
        DCPage.onEntry("AboutActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("AboutActivity");
    }
}
