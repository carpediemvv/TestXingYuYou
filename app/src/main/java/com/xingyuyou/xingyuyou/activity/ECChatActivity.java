package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.fragment.MyChatFragment;

public class ECChatActivity extends AppCompatActivity {

    // 当前聊天的 ID
    private String mChatId;
    private MyChatFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecchat);
        // 这里直接使用EaseUI封装好的聊天界面
        chatFragment = new MyChatFragment();
        // 将参数传递给聊天界面
        //设置要发送出去的昵称
        Intent intent=getIntent();
        chatFragment.setArguments(intent.getExtras());
        //传入参数
        getSupportFragmentManager().beginTransaction().add(R.id.ec_layout_container, chatFragment).commit();

        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
