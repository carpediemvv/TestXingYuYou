package com.xingyuyou.xingyuyou;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.dataeye.sdk.api.app.DCAgent;
import com.fenglinshanhuo.flshsdk.api.FlshSdkPluginApi;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.xingyuyou.xingyuyou.Dao.HxEaseuiHelper;
import com.xingyuyou.xingyuyou.Utils.AppUtils;
import com.xingyuyou.xingyuyou.Utils.Constant;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.activity.MainActivity;
import com.xingyuyou.xingyuyou.service.InitializeService;

/**
 * Created by Administrator on 2017/2/20.
 */

public class App extends Application {
    EMConnectionListener connectionListener;
    public static Context mContext;
    public static String DEVICETOKEN;
      public static String mAppVersionCode;
    // 记录是否已经初始化
    private boolean isInit = false;
    public static View oldView;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //超过方法数
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        //软件版本
        mAppVersionCode = String.valueOf(AppUtils.getAppVersionCode(this));
        //intentservice初始化l,,
        InitializeService.start(this);
        //bugly
        CrashReport.initCrashReport(getApplicationContext(), "dc36a6e107", true);
        //友盟推送
        youMengPush();
        // 初始化环信SDK
        initEasemob();
        //初始化直播SDK
        FlshSdkPluginApi.register("43189671","fxaacrutsctwyfdkkrminxxzyahjzomf");
        //dataeye
        DCAgent.initWithAppIdAndChannelId(this, "CD38DE8CEB84E2A6E97AA697074DDDFEC", "default");
        initTest();
    }
//   加载阿里云播放区
    public void initTest(){
        System.loadLibrary("QuCore-ThirdParty");
        System.loadLibrary("QuCore");

        QupaiHttpFinal.getInstance().initOkHttpFinal();
    }


    private void youMengPush() {
        //友盟推送初始化
        final PushAgent mPushAgent = PushAgent.getInstance(this);
        //开启log
        mPushAgent.setDebugMode(true);
        //应用在前台时否显示通知
        mPushAgent.setNotificaitonOnForeground(true);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                DEVICETOKEN=deviceToken;
                //注册成功会返回device token
                Log.e("weiwei", "onSuccess: " + deviceToken);
                if (UserUtils.logined()){
                    mPushAgent.addExclusiveAlias(UserUtils.getUserId(), "custom_type_my",
                            new UTrack.ICallBack() {
                                @Override
                                public void onMessage(boolean isSuccess, String message) {
                                    Log.e("weiwei", "onMessage: "+ isSuccess+":"+message);
                                }
                            });
                }
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        //自定义友盟推送消息
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //自定义友盟推送样式
     /*   UmengMessageHandler messageHandler = new UmengMessageHandler() {

            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        Notification.Builder builder = new Notification.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                                R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
                                getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon,
                                getSmallIconId(context, msg));
                        builder.setContent(myNotificationView)
                                .setSmallIcon(getSmallIconId(context, msg))
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);

                        return builder.getNotification();
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };*/
        //不显示通知栏，完全自定义消息
        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            @Override
            public void dealWithCustomMessage(final Context context, final UMessage uMessage) {
                new Handler(getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // 对于自定义消息，PushSDK默认只统计送达。若开发者需要统计点击和忽略，则需手动调用统计方法。
                        boolean isClickOrDismissed = true;
                        if(isClickOrDismissed) {
                            //自定义消息的点击统计
                            Log.e("weiwei", "run: "+uMessage.custom );
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(uMessage);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(uMessage);
                        }
                        Toast.makeText(context, uMessage.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

        };
        mPushAgent.setMessageHandler(messageHandler);
    }


    /**
     * 初始化环信SDK
     */
    private void initEasemob() {

        if (EaseUI.getInstance().init(mContext, initOptions())) {

            // 设置开启debug模式
            EMClient.getInstance().setDebugMode(true);

            // 设置初始化已经完成
            isInit = true;

            HxEaseuiHelper.getInstance().init(this.getApplicationContext());
            //设置全局监听
            setGlobalListeners();
        }
    }


    private EMOptions initOptions() {

        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
        // options.setRequireServerAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);

        return options;
    }


    /**
     * 设置一个全局的监听
     */
    protected void setGlobalListeners(){


        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                if (error == EMError.USER_REMOVED) {// 显示帐号已经被移除
                    onUserException(Constant.ACCOUNT_REMOVED);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {// 显示帐号在其他设备登录
                    onUserException(Constant.ACCOUNT_CONFLICT);
                    EMClient.getInstance().logout(true);//退出登录
                    Toast.makeText(getApplicationContext(),"退出成功",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {//
                    onUserException(Constant.ACCOUNT_FORBIDDEN);
                }
            }

            @Override
            public void onConnected() {
                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events

            }
        };

        //register connection listener
        EMClient.getInstance().addConnectionListener(connectionListener);
    }
    protected void onUserException(String exception){
        // EMLog.e(TAG, "onUserException: " + exception);
        Toast.makeText(getApplicationContext(),exception,Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(getBaseContext(), UserQrCodeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(exception, true);
//        this.startActivity(intent);
    }
}
