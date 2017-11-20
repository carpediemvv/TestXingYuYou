package com.xingyuyou.xingyuyou.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.demo.crop.AliyunVideoCrop;
import com.aliyun.demo.crop.MediaActivity;
import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.fenglinshanhuo.flshsdk.api.FlshSdkPluginApi;
import com.umeng.socialize.UMShareAPI;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppUtils;
import com.xingyuyou.xingyuyou.Utils.ChangeBitmap;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.MainContentVPAdapter;
import com.xingyuyou.xingyuyou.base.BaseActivity;
import com.xingyuyou.xingyuyou.fragment.GodFragment;
import com.xingyuyou.xingyuyou.fragment.ThreeFragment;
import com.xingyuyou.xingyuyou.fragment.TwoFragment;
import com.xingyuyou.xingyuyou.weight.CustomViewPager;
import com.xingyuyou.xingyuyou.weight.ProgressButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;

import static android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC;
import static com.xingyuyou.xingyuyou.activity.VideoActivity.REQUEST_RECORD;

public class MainActivity extends BaseActivity {
    private ArrayList<Fragment> fragments;
    public static BottomNavigationBar bottomNavigationBar;
    private CustomViewPager customViewPager;
    private MainContentVPAdapter adapter;
    private static final int REQUEST_CODE = 0; // 请求码
    private String mAppDownload;
    private String mVersionText;
    private int mVersionCode;
    private String mUpdateInfo;
    String path;
    private String imagePath;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if (jsonObject.getString("status").equals("-1")) {
                        String errorinfo = jsonObject.getString("errorinfo");
                        String download_url = jsonObject.getString("download_url");
                        ToThirdUpadte(errorinfo,download_url);
                    }
                    if (jsonObject.getString("status").equals("2")) {
                        String errorinfo = jsonObject.getString("errorinfo");
                        String download_url = jsonObject.getString("download_url");
                        ToSimpleThirdUpadte(errorinfo,download_url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 2) {
                mPopWindow.showAtLocation(customViewPager, Gravity.CENTER, 0, 0);
            }
        }
    };
    private TextView mTvUpdateInfo;
    private ProgressButton mBtUpdate;
    private AlertDialog mAlertDialog;
    private SPUtils mConfig_def;
    private PopupWindow mPopWindow;
    private Runnable mRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVersionCode = AppUtils.getAppVersionCode(this);
  /*    setImg();
        FlshSdkPluginApi.themeColor("#ff717c");*/
        initView();
        checkUpdate();
        toCheckPermission(Manifest.permission.READ_PHONE_STATE);
        firstStartForDay();


    }

    private void setImg() {
        if (UserUtils.logined()) {
            FlshSdkPluginApi.registeredUser(UserUtils.getUserId(),UserUtils.getNickName(),UserUtils.getUserPhoto());
        } else {
            FlshSdkPluginApi.registeredUser("1","游客","");
        }
        Bitmap bitmap_search = BitmapFactory.decodeResource(getResources(), com.fenglinshanhuo.flshsdk.R.drawable.empty_search_jiaonang);
        Bitmap bitmap_livestop = BitmapFactory.decodeResource(getResources(), com.fenglinshanhuo.flshsdk.R.drawable.empty_livestop_jiaonang);
        Bitmap bitmap_empty_data = BitmapFactory.decodeResource(getResources(), com.fenglinshanhuo.flshsdk.R.drawable.empty_data_jiaonang);
        FlshSdkPluginApi.setUI(bitmap_search, bitmap_livestop, bitmap_empty_data, "#ff717c", false, com.fenglinshanhuo.flshsdk.R.drawable.frame_jiaonang);
    }

    /**
     * 不断轮询接口，如果有新消息发送一个广播
     */
    private void initUserAllMessageData() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                checkNewData();
                mHandler.postDelayed(this, 10 * 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 3000);
    }

    private void checkNewData() {

        OkHttpUtils.post()//
                .url(XingYuInterface.SYSTEM_REQUEST_STATUS)
                .addParams("uid", UserUtils.getUserId())
                .addParams("system_count", UserUtils.getAllSystemMessageCount())
                .addParams("message_count", UserUtils.getAllPostMessageCount())
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            //将消息条数保存下来
                            UserUtils.setAllSystemMessageCount(jsonObject.getString("system_all_num"));
                            UserUtils.setAllPostMessageCount(jsonObject.getString("message_all_num"));
                            if (jsonObject.getString("update_all_state").equals("1")) {
                                //将系统消息更新状态保存下来
                                if (!jsonObject.getString("update_system_num").equals("0")) {
                                    UserUtils.setAllSystemMessageStatus(true);
                                    UserUtils.setUpdateSystemMessageCount(jsonObject.getString("update_system_num"));
                                }
                                //将帖子消息更新状态保存下来
                                if (!jsonObject.getString("update_message_num").equals("0")) {
                                    UserUtils.setAllPostMessageStatus(true);
                                    UserUtils.setUpdatePostMessageCount(jsonObject.getString("update_message_num"));
                                }
                                //通知首页、游戏界面更新红点状态
                                Intent intent = new Intent("updateUserAllMessage");
                                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
                                //停止轮询
                                mHandler.removeCallbacks(mRunnable);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * Manifest.permission.WRITE_EXTERNAL_STORAGE
     */
    private void toCheckPermission(final String permission) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                //进入到这里代表没有权限.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    //已经禁止提示了
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("申请权限")
                            .setMessage("就是胶囊需要这个权限，绑定您的手机信息")
                            .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, REQUEST_CODE);
                                }
                            })
                            .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MainActivity.this, "您已禁止该权限，如需请在设置中重新开启。", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE);
                }
            } else {

            }
        } else {
            //进入到这里代表没有权限.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //已经禁止提示了
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("申请权限")
                        .setMessage("就是胶囊需要这个权限，下载游戏，发表图片等等...")
                        .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

                            }
                        })
                        .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }


    }

  /* private void toCheckPermission() {
        //READ_PHONE_STATE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //进入到这里代表没有权限.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                //已经禁止提示了
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("申请权限")
                        .setMessage("就是胶囊需要这个权限，下载游戏，发表图片等等...")
                        .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);

                            }
                        })
                        .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
            }
        }

    }*/

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void firstStartForDay() {
        mConfig_def = new SPUtils("config_def");
        String sameDayTime = mConfig_def.getString("sameDayTime", "777");
        if (TimeUtils.isSameDay(sameDayTime)) {
            // Toast.makeText(this, "同一天", Toast.LENGTH_SHORT).show();
        } else {
            mConfig_def.putBoolean("isSig", false);
            View popupView = LayoutInflater.from(this).inflate(R.layout.popup_signatures_layout, null);
            mPopWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPopWindow.setBackgroundDrawable(getResources().getDrawable(R.color.custom_gray));
            mHandler.sendEmptyMessageDelayed(2, 2000);
            //签到按钮
            Button btSig = (Button) popupView.findViewById(R.id.bt_sig);
            btSig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (UserUtils.logined()) {
                        sigDay();
                    } else {
                        IntentUtils.startActivity(MainActivity.this, LoginActivity.class);
                    }
                }
            });
            //取消签到
            ImageView ivClose = (ImageView) popupView.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopWindow.dismiss();
                }
            });
        }

    }

    private void sigDay() {
        OkHttpUtils.post()//
                .url(XingYuInterface.USER_SIGN)
                .addParams("uid", UserUtils.getUserId())
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mPopWindow.dismiss();
                        Toast.makeText(MainActivity.this, "签到大成功！\n" +
                                "经验+5╰(*°▽°*)╯~", Toast.LENGTH_SHORT).show();
                        mConfig_def.putBoolean("isSig", true);
                        mConfig_def.putString("sameDayTime", TimeUtils.getNowTimeString());
                    }
                });
    }


    private void initView() {
        getFragments();
        customViewPager = (CustomViewPager) findViewById(R.id.main_fragment);
        customViewPager.setAdapter(adapter);
        customViewPager.setOffscreenPageLimit(4);//设置缓存页数，缓存所有fragment
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.shequ_app, "首页").setActiveColorResource(R.color.colorPrimary).setInActiveColorResource(R.color.haha))
                .addItem(new BottomNavigationItem(R.drawable.fenlei_app, "分区").setActiveColorResource(R.color.colorPrimary).setInActiveColorResource(R.color.haha))
                .addItem(new BottomNavigationItem(R.drawable.shenshe_app, "神社").setActiveColorResource(R.color.colorPrimary).setInActiveColorResource(R.color.haha)) .setMode(BottomNavigationBar.MODE_FIXED)//设置底部代文字显示模式。MODE_DEFAULT默认MODE_FIXED代文字MODE_SHIFTING不带文字.setInactiveIcon(getResources().getDrawable(R.drawable.ic_info_black_24dp)))
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)//背景模式BACKGROUND_STYLE_RIPPLE涟漪BACKGROUND_STYLE_STATIC静态
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            //当前的选中的tab
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        customViewPager.setCurrentItem(0);
                        break;
                    case 1:
                        customViewPager.setCurrentItem(1);
                        break;
                    case 2:
                        customViewPager.setCurrentItem(2);
                        break;
                }
            }
            //上一个选中的tab
            @Override
            public void onTabUnselected(int position) {
                Log.i("tab", "onTabUnselected position:" + position);
            }

            //当前tab被重新选中
            @Override
            public void onTabReselected(int position) {
                Log.i("tab", "onTabReselected position:" + position);
            }
        });
    }

    private ArrayList<Fragment> getFragments() {
        fragments = new ArrayList<>();
        fragments.add(TwoFragment.newInstance("社区"));
        fragments.add(ThreeFragment.newInstance("分类"));
        fragments.add(GodFragment.newInstance("神社"));
        adapter = new MainContentVPAdapter(getSupportFragmentManager(), fragments);
        return fragments;
    }

    @Override
    protected void onResume() {
        super.onResume();
        DCAgent.resume(this);
        DCPage.onEntry("MainActivity");
        //开始轮询
        if (!UserUtils.getAllPostMessageStatus() && !UserUtils.getAllSystemMessageStatus()) {
            mHandler.removeCallbacks(mRunnable);
            initUserAllMessageData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意授权

                } else {
                    //用户拒绝授权
                    //Toast.makeText(this, "您已拒绝访问sd卡权限，会导致无法下载游戏", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {

                finish();
                System.exit(0);

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2002){
            if(resultCode == Activity.RESULT_OK && data!= null){
                int type = data.getIntExtra(MediaActivity.RESULT_TYPE,0);
                if(type ==  MediaActivity.RESULT_TYPE_CROP){
                    String path = data.getStringExtra(AliyunVideoCrop.RESULT_KEY_CROP_PATH);
                    Toast.makeText(this,"文件路径为 "+ path + " 时长为 " +
                            data.getLongExtra(AliyunVideoCrop.RESULT_KEY_DURATION,0),Toast.LENGTH_SHORT).show();
                }else if(type ==  MediaActivity.RESULT_TYPE_RECORD){
                    Toast.makeText(this,"文件路径为 "+
                            data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH),Toast.LENGTH_SHORT).show();
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"用户取消裁剪",Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode ==  REQUEST_RECORD){
            if(resultCode == Activity.RESULT_OK && data!= null){
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE,0);

               if(type ==  AliyunVideoRecorder.RESULT_TYPE_CROP){
                   String path = data.getStringExtra(AliyunVideoCrop.RESULT_KEY_CROP_PATH);
                   if(path.endsWith(".mp4")) {
                       MediaMetadataRetriever media = new MediaMetadataRetriever();
                       media.setDataSource(path);
                       Bitmap bitmap = media.getFrameAtTime();
                       ChangeBitmap changeBitmap = new ChangeBitmap();
                       imagePath = changeBitmap.saveBitmap(MainActivity.this, bitmap);
                       Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                       Bundle b = new Bundle();
                       b.putParcelable("bitmap", bitmap);
                       b.putString("path", path);
                       intent.putExtras(b);
                       startActivity(intent);
                   }else {
                       Toast.makeText(MainActivity.this,"请选择视频上传",Toast.LENGTH_SHORT).show();
                   }
                }else if(type ==  AliyunVideoRecorder.RESULT_TYPE_RECORD) {
                        path = data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH);
                    if (path.endsWith(".mp4")) {
                        //设置图片
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
                        media.setDataSource(path);
                        Bitmap bitmap = media.getFrameAtTime(1500,OPTION_CLOSEST_SYNC);
                        ChangeBitmap changeBitmap = new ChangeBitmap();
                        imagePath = changeBitmap.saveBitmap(MainActivity.this, bitmap);
                        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                        Bundle b = new Bundle();
                        b.putParcelable("bitmap", bitmap);
                        b.putString("path", path);
                        intent.putExtras(b);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this,"请选择视频上传",Toast.LENGTH_SHORT).show();
                }
                }/*else if(requestCode == 2002){
                   if(type ==  MediaActivity.RESULT_TYPE_CROP){
                       String path = data.getStringExtra(AliyunVideoCrop.RESULT_KEY_CROP_PATH);
                       //设置图片
                       MediaMetadataRetriever media = new MediaMetadataRetriever();
                       media.setDataSource(path);
                       Bitmap bitmap = media.getFrameAtTime();
                       ChangeBitmap changeBitmap = new ChangeBitmap();
                       imagePath = changeBitmap.saveBitmap(MainActivity.this, bitmap);
                       Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                       Bundle b = new Bundle();
                       b.putParcelable("bitmap", bitmap);
                       b.putString("path", path);
                       intent.putExtras(b);
                       startActivity(intent);
                       Toast.makeText(MainActivity.this,"哈哈1",Toast.LENGTH_SHORT).show();
                   }
                   else if(type ==  MediaActivity.RESULT_TYPE_RECORD){
                       //设置图片
                      String path= data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH);
                       MediaMetadataRetriever media = new MediaMetadataRetriever();
                       media.setDataSource(path);
                       Bitmap bitmap = media.getFrameAtTime();
                       ChangeBitmap changeBitmap = new ChangeBitmap();
                       imagePath = changeBitmap.saveBitmap(MainActivity.this, bitmap);
                       Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                       Bundle b = new Bundle();
                       b.putParcelable("bitmap", bitmap);
                       b.putString("path", path);
                       intent.putExtras(b);
                       startActivity(intent);
                       Toast.makeText(MainActivity.this,"哈哈2",Toast.LENGTH_SHORT).show();
                   }
               }else if(resultCode == Activity.RESULT_CANCELED){
                   Toast.makeText(this,"用户取消裁剪",Toast.LENGTH_SHORT).show();
               }*/
            }/*else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(MainActivity.this,"用户取消录制",Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    private void checkUpdate() {
        OkHttpUtils.post()//
                .url(XingYuInterface.VERSION_UPDATE)
                .addParams("version_number",String.valueOf(AppUtils.getAppVersionCode(this)))
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }

    private void ToThirdUpadte(String error, final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_third_update_app, null);
        builder.setView(view);
        mTvUpdateInfo = (TextView) view.findViewById(R.id.tv_update_info);
        mTvUpdateInfo.setText(error);
        TextView tv_update = (TextView) view.findViewById(R.id.tv_update);
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转浏览器
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("market://details?id=com.xingyuyou.xingyuyou");
                    intent.setData(content_url);
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    startActivity(intent);
                }
            }
        });
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

    private void ToSimpleThirdUpadte(String errorinfo, final String download_url) {
        //获取上次时间
        mConfig_def = new SPUtils("config_def");
        long checkup_date = mConfig_def.getLong("checkup_date",11);
        //获取当前时间
        final long currentTimeMillis = System.currentTimeMillis();
        //和上次存储时间对比（超过2*24*60*60进行提示）
        if ((currentTimeMillis-checkup_date)>2*24*60*60*1000){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_third_update_app, null);
            builder.setView(view);
            mTvUpdateInfo = (TextView) view.findViewById(R.id.tv_update_info);
            mTvUpdateInfo.setText(errorinfo);
            TextView tv_update = (TextView) view.findViewById(R.id.tv_update);
            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转浏览器
                    try {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("market://details?id=com.xingyuyou.xingyuyou");
                        intent.setData(content_url);
                        startActivity(intent);
                    } catch (Exception e) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(download_url);
                        intent.setData(content_url);
                        startActivity(intent);
                    }

                }
            });
            TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mConfig_def.putLong("checkup_date",currentTimeMillis);
                    mAlertDialog.dismiss();
                }
            });
            mAlertDialog = builder.create();
            mAlertDialog.setCancelable(false);
            mAlertDialog.show();
        }
    }

    private void ToUpadte() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_update_app, null);
        builder.setView(view);
        mTvUpdateInfo = (TextView) view.findViewById(R.id.tv_update_info);
        mTvUpdateInfo.setText(mUpdateInfo);
        mBtUpdate = (ProgressButton) view.findViewById(R.id.bt_update);
        mBtUpdate.setTag(0);
        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int) mBtUpdate.getTag() == 0) {
                    mBtUpdate.setTag(1);
                    toDownload();
                    Toast.makeText(MainActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "正在下载，请稍后", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

    private void toDownload() {
        OkHttpUtils//
                .get()//
                .url(mAppDownload)//
                .build()//http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/%E5%8D%95%E6%9C%BA/Iter.apk
                .execute(new FileCallBack(FileUtils.fileSavePath, "xingyuyou.apk")//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MainActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {

                        mBtUpdate.setProgress((int) (progress * 100));
                    }

                    @Override
                    public void onResponse(File response, int id) {
                    }

                    @Override
                    public void onAfter(int id) {
                        String path = FileUtils.fileSavePath + "xingyuyou" + ".apk";
                        AppUtils.installApp(MainActivity.this, path);
                        Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("MainActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
