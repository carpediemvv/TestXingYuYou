package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.fragment.EasyFragment;

import java.util.List;

public class TalkActivity extends AppCompatActivity {
    // 当前聊天的 ID
    private EasyFragment easeConversationListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
       init();
    }
    private void init(){
        easeConversationListFragment=new EasyFragment();
        easeConversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent=new Intent(TalkActivity.this,ECChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId());
                intent.putExtra("re_uid",conversation.conversationId());
                startActivity(intent);
            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_talk,easeConversationListFragment)
                .commit();
    }

    EMMessageListener messageListener=new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //接收到新的消息
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };
    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                // refresh conversation list
                if (easeConversationListFragment != null) {
                    easeConversationListFragment.refresh();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }
}