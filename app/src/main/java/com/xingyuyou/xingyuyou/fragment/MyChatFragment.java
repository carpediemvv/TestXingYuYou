package com.xingyuyou.xingyuyou.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.xingyuyou.xingyuyou.Dao.APPConfig;
import com.xingyuyou.xingyuyou.Dao.HxEaseuiHelper;
import com.xingyuyou.xingyuyou.Dao.RobotUser;
import com.xingyuyou.xingyuyou.Dao.SharedPreferencesUtils;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.Constant;
import com.xingyuyou.xingyuyou.Utils.KeyboardUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.Dialog_adapter;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by Administrator on 2017/8/19.
 */

public class MyChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {
    private TopRightMenu mTopRightMenu;
    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_FILE = 12;

    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;

    //red packet code : 红包功能使用的常量
    private static final int ITEM_RED_PACKET = 16;
    private static final int ITEM_TRANSFER_PACKET = 17;
    //end of red packet code

    /**
     * if it is chatBot
     */
    private boolean isRobot;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void emptyHistory() {
        super.emptyHistory();
        mTopRightMenu = new TopRightMenu(getActivity());
        List<MenuItem> menuItems = new ArrayList<>();
        titleBar.setLeftImageResource(R.drawable.ic_action_back);
        menuItems.add(new MenuItem("清空聊天记录"));
        menuItems.add(new MenuItem("举报ta"));
/*        menuItems.add(new MenuItem(R.mipmap.ic_launcher, "扫一扫"));
        menuItems.add(new MenuItem(R.mipmap.ic_launcher, "付款"));
        menuItems.add(new MenuItem(R.mipmap.ic_launcher, "面对面快传"));*/
        mTopRightMenu
//                        .setHeight(480)     //默认高度480
//                        .setWidth(320)      //默认宽度wrap_content
                .showIcon(true)     //显示菜单图标，默认为true
                .dimBackground(true)           //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        // Toast.makeText(MainActivity.this, "点击菜单:" + position, Toast.LENGTH_SHORT).show();
                        switch (position) {
                            case 0:
                                String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
                                new EaseAlertDialog(getActivity(), null, msg, null, new EaseAlertDialog.AlertDialogUser() {
                                    @Override
                                    public void onResult(boolean confirmed, Bundle bundle) {
                                        if (confirmed) {
                                            if (conversation != null) {
                                                conversation.clearAllMessages();
                                            }
                                            messageList.refresh();
                                            haveMoreData = true;
                                        }
                                    }
                                }, true).show();
                                break;

                            case 1:
                                AlertDialog.Builder   normalDialog = new AlertDialog.Builder(getActivity());
                                final AlertDialog dialog = normalDialog.create();
                                report(dialog,getActivity(),"294");
                        }
                    }
                })
                .showAsDropDown(titleBar.getRightLayout(), -titleBar.getRightLayout().getWidth() - 50, 0);
        // -moreBtn.getWidth()-175 == -252
//                        .showAsDropDown(moreBtn);
    }

    private void report(final AlertDialog dialog, final Context context, String s) {
        dialog.show();
        View v=View.inflate(context,R.layout.dialog,null);
        if(v.getParent()!=null){
            ((ViewGroup)v.getParent()).removeAllViews();
        }
        dialog.setContentView(v);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        final EditText edittext=(EditText)v.findViewById(R.id.dia_edtext);
        Button sure= (Button) v.findViewById(R.id.sure);
        Button cancle= (Button) v.findViewById(R.id.cancle);
        ListView listView= (ListView) v.findViewById(R.id.dialog_List);
        final Dialog_adapter mAdapter=new Dialog_adapter(context);
        listView.setAdapter(mAdapter);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String re_uid= getArguments().getString("re_uid");
                Log.d("qqqqq1",UserUtils.getEmid()+"");
                if (mAdapter.getIndex() == -1) {
                    Toast.makeText(context, "请选择一项举报理由", Toast.LENGTH_SHORT).show();
                } else {
                    KeyboardUtils.hideSoftInput((Activity) context);
                    OkHttpUtils.post()
                            .addParams("uid", UserUtils.getEmid())
                            .addParams("re_uid",re_uid)
                            .addParams("type", String.valueOf(mAdapter.getIndex()))
                            .addParams("content", StringUtils.isEmpty(edittext.getText().toString().trim()) == true ? "" : edittext.getText().toString().trim())
                            .url(XingYuInterface.GET_USER_REPORT)
                            .tag(this)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }
                                @Override
                                public void onResponse(String response, int id) {
                                    handler.obtainMessage(1, response).sendToTarget();
                                    Toast.makeText(context, "举报成功", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void setUpView() {
        setChatFragmentListener(this);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            Map<String, RobotUser> robotMap = HxEaseuiHelper.getInstance().getRobotList();
            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
                isRobot = true;
            }
        }
        super.setUpView();
    }

    public void setChatFragmentListener(EaseChatFragmentHelper chatFragmentHelper) {
        this.chatFragmentHelper = chatFragmentHelper;
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if (isRobot) {
            //set message extension
            message.setAttribute("em_robot_message", isRobot);
        }
        //设置要发送扩展消息用户昵称
        message.setAttribute(Constant.USER_NAME, (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), APPConfig.USER_NAME, "nike"));
        //设置要发送扩展消息用户头像
        message.setAttribute(Constant.HEAD_IMAGE_URL, (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), APPConfig.USER_HEAD_IMG, ""));

    }


    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }


    @Override
    public void onEnterToChatDetails() {
    }

    @Override
    public void onAvatarClick(String username) {
        //handling when user click avatar
//        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
//        intent.putExtra("username", username);
//        startActivity(intent);
    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        //处理接收到的消息
        for (EMMessage message : messages) {
            //接收并处理扩展消息
            String userName = message.getStringAttribute(Constant.USER_NAME, "");
            String userId = message.getStringAttribute(Constant.USER_ID, "");
            String userPic = message.getStringAttribute(Constant.HEAD_IMAGE_URL, "");
            String hxIdFrom = message.getFrom();
            System.out.println("接收到的用户名：" + userName + "接收到的id：" + userId + "头像：" + userPic);
            EaseUser easeUser = new EaseUser(hxIdFrom);
            easeUser.setAvatar(userPic);
            easeUser.setNick(userName);
            //存入内存
        }
        super.onMessageReceived(messages);
    }

//    @Override
//    public void onCmdMessageReceived(List<EMMessage> messages) {
//        //red packet code : 处理红包回执透传消息
////        for (EMMessage message : messages) {
////            EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
////            String action = cmdMsgBody.action();//获取自定义action
////            if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
////                RedPacketUtil.receiveRedPacketAckMessage(message);
////                messageList.refresh();
////            }
////        }
//        //end of red packet code
//
//
//        super.onCmdMessageReceived(messages);
//    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
        // no message forward when in chat room
//        startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message",message)
//                        .putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM),
//                REQUEST_CODE_CONTEXT_MENU);
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_VIDEO://视频
//                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                break;
            case ITEM_FILE: //file
                selectFileFromLocal();
                break;
            case ITEM_VOICE_CALL:
//                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
//                startVideoCall();
                break;
            //red packet code : 进入发红包页面
            case ITEM_RED_PACKET:
//                if (chatType == Constant.CHATTYPE_SINGLE) {
//                    //单聊红包修改进入红包的方法，可以在小额随机红包和普通单聊红包之间切换
//                    RedPacketUtil.startRandomPacket(new RPRedPacketUtil.RPRandomCallback() {
//                        @Override
//                        public void onSendPacketSuccess(Intent data) {
//                            sendMessage(RedPacketUtil.createRPMessage(getActivity(), data, toChatUsername));
//                        }
//
//                        @Override
//                        public void switchToNormalPacket() {
//                            RedPacketUtil.startRedPacketActivityForResult(ChatFragment.this, chatType, toChatUsername, REQUEST_CODE_SEND_RED_PACKET);
//                        }
//                    },getActivity(),toChatUsername);
//                } else {
//                    RedPacketUtil.startRedPacketActivityForResult(this, chatType, toChatUsername, REQUEST_CODE_SEND_RED_PACKET);
//                }
                break;
            case ITEM_TRANSFER_PACKET://进入转账页面
//                RedPacketUtil.startTransferActivityForResult(this, toChatUsername, REQUEST_CODE_SEND_TRANSFER_PACKET);
                break;
            //end of red packet code
            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }


    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 12;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //red packet code : 红包消息、红包回执消息以及转账消息的chatrow type
//                else if (RedPacketUtil.isRandomRedPacket(message)) {
//                    //小额随机红包
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RANDOM : MESSAGE_TYPE_SEND_RANDOM;
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {
//                    //发送红包消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_PACKET : MESSAGE_TYPE_SEND_RED_PACKET;
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
//                    //领取红包消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_PACKET_ACK : MESSAGE_TYPE_SEND_RED_PACKET_ACK;
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_TRANSFER_PACKET_MESSAGE, false)) {
//                    //转账消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TRANSFER_PACKET : MESSAGE_TYPE_SEND_TRANSFER_PACKET;
//                }
                //end of red packet code
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
//            if(message.getType() == EMMessage.Type.TXT){
            // voice call or video call
//                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
//                        message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
//                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
//                }
            //red packet code : 红包消息、红包回执消息以及转账消息的chat row
//                else if (RedPacketUtil.isRandomRedPacket(message)) {//小额随机红包
//                    return new ChatRowRandomPacket(getActivity(), message, position, adapter);
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {//红包消息
//                    return new ChatRowRedPacket(getActivity(), message, position, adapter);
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {//红包回执消息
//                    return new ChatRowRedPacketAck(getActivity(), message, position, adapter);
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_TRANSFER_PACKET_MESSAGE, false)) {//转账消息
//                    return new ChatRowTransfer(getActivity(), message, position, adapter);
//                }
            //end of red packet code
//            }
            return null;
        }

    }
}
