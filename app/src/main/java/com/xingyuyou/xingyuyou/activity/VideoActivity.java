package com.xingyuyou.xingyuyou.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.aliyun.common.utils.StorageUtils;
import com.aliyun.demo.crop.AliyunVideoCrop;
import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.aliyun.struct.common.ScaleMode;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.FlashType;
import com.aliyun.struct.snap.AliyunSnapVideoParam;
import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.google.gson.Gson;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ChangeBitmap;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.glide.Utils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.IdListBean;
import com.xingyuyou.xingyuyou.bean.VideoBean;
import com.xingyuyou.xingyuyou.bean.community.TagBean;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.Call;

public class VideoActivity extends AppCompatActivity  {
    String[] eff_dirs;
    VideoQuality videoQuality ;
    public static final int REQUEST_RECORD = 2001;
    private static final int REQUEST_IMAGE = 2;
    private static final int TYPE_FOOTER = 21;
    private static final int REQUEST_CODE = 3;
    private Toolbar mToolbar;
    private CustomDialog mDialog;
    private RelativeLayout mRlTagMore;
    private EditText mStTitle;
    private EditText mEtContent;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mImageList = new ArrayList();
    private String mPostTags;
    private RelativeLayout mRlCommMore;
    private String mPostCommId;
    private Map map = new HashMap<String, String>();
    private static final int CODE_PERSON = 1;
    private PopupWindow mPopWindow;
    private String vod_id = "";
    private String uploadAuth = "";
    private String  UploadAddress="";
    private String accessKeyId = "LTAIcwmgiilah62O";
    private String accessKeySecret = "N9BlJendEXzCRfAgkGrn72TGExN9Ff";
    private String secretToken = null;
    private String expireTime = null;
    private String[] PostCommClassName;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                mPopWindow.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);

            }
        }
    };
    private String mPostTagsReturn;
    private List<TagBean> mPostTagsList;
    private String[] mPostTagsString;
    //private ImageView post_aite;
    private List<IdListBean> idListBeen =new ArrayList<>();
    /**
     * 存储@的cid、name对
     */
    private Map<String, String> cidNameMap = new HashMap<String, String>();

    private String nameStr;
    private ArrayList<String> idStr=new ArrayList<>();
    List<Map<String, Object>> list_map = new ArrayList<Map<String, Object>>();
    private VODUploadClient uploader;
    private ImageView myImageView;
    private String imagePath;
    private View v_dialog;
    private AlertDialog normalDialog;
    private TextView progress_dialog;
    private ProgressDialog processDialog;
    private Handler handler;
/*    Handler handler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int progress=msg.arg1;
                progress_dialog= (TextView) v_dialog.findViewById(R.id.progress_dialog);
                progress_dialog.setText("正在上传"+progress);
            }
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //捕获那个不知道原因的异常  上面实现的UncaughtExceptionHandler就是为这个
        // Thread.setDefaultUncaughtExceptionHandler(this);
        setContentView(R.layout.activity_video);
        initAssetPath();
        //  post_aite = (ImageView) findViewById(R.id.post_aite);
        initView();
        intentData();
        initToolbar();
        initTagMore();
        initCommMore();
        popupWin();

        handler = new Handler(new MsgCallBack());
        //  mEtContent.setFilters(new InputFilter[] { new MyInputFilter() });
    /*      post_aite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoActivity.this, PersonActivity.class);
                startActivityForResult(intent, CODE_PERSON);
            }
        });*/

        // 打开日志
        OSSLog.enableLog();
        uploader = new VODUploadClientImpl(getApplicationContext());

        VODUploadCallback callback = new VODUploadCallback() {
            /**
             * 上传成功回调
             */
            @Override
            public void onUploadSucceed(UploadFileInfo uploadFileInfo) {
                dealEditData();
                //  Toast.makeText(VideoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                //postMessage("上传成功");
            }
            /**
             * 上传失败
             */

            @Override
            public void onUploadFailed(UploadFileInfo uploadFileInfo, String s, String s1) {
                Toast.makeText(VideoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                finish();
            }
            /**
             * 回调上传进度
             */
            @Override
            public void onUploadProgress(UploadFileInfo uploadFileInfo, long uploadedSize, long totalSize) {



                postProgress(uploadedSize,totalSize);

            }
            /**
             * 上传凭证过期后，会回调这个接口
             * 可在这个回调中获取新的上传，然后调用resumeUploadWithAuth继续上传
             */
            @Override
            public void onUploadTokenExpired() {
                System.out.println("上传凭证过期后");
            }
            /**
             * 上传过程中，状态由正常切换为异常时触发
             */
            @Override
            public void onUploadRetry(String s, String s1) {
            }
            /**
             * 上传过程中，从异常中恢复时触发
             */
            @Override
            public void onUploadRetryResume() {
            }
            /**
             * 文件开始上传时触发
             */
            @Override
            public void onUploadStarted(UploadFileInfo uploadFileInfo) {
                uploader.setUploadAuthAndAddress(uploadFileInfo,uploadAuth,UploadAddress);
                //  Toast.makeText(VideoActivity.this, "开始上传", Toast.LENGTH_SHORT).show();
            }
        };
        uploader.init(accessKeyId, accessKeySecret, callback);
//        }

    }



    class MsgCallBack implements Handler.Callback
    {

        @Override
        public boolean handleMessage(Message msg)
        {
            switch (msg.what) {
                case 1:
                    int p=msg.arg1;
                    String progress= "正在上传"+msg.arg1+"%";
                    progress_dialog.setText(progress);
                    if(p==100) {

                        normalDialog.dismiss();
                    }
                    break;
                case 2:
                    Toast.makeText(VideoActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
                    break;


                default:
                    break;
            }
            return true;
        }

    }
    public void postMessage(String msg)
    {
        Message.obtain(handler, 2, 0, 0, msg).sendToTarget();
    }
    public void postProgress(long uploadedSize, long totalSize)
    {
        int progress = (int) (uploadedSize * 100 / totalSize);
        Message message = Message.obtain();
        message.what = 1;
        message.arg1 = progress;
        handler.sendMessage(message);
    }


    //拉起录制界面
    private void initView() {
/*       processDialog = new ProgressDialog(VideoActivity.this);
        processDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        processDialog.setMax(100);
        processDialog.setTitle("正在上传");
        processDialog.setIcon(R.drawable.ic_launcher);
        processDialog.setCancelable(false);*/
        v_dialog = View.inflate(VideoActivity.this, R.layout.video_progress_dialog,null);
        normalDialog = new AlertDialog.Builder(VideoActivity.this,R.style.MyCommonDialog).create();
        normalDialog.setView(v_dialog);
        normalDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp=normalDialog.getWindow().getAttributes();
        lp.dimAmount=0.4f;
        normalDialog.getWindow().setAttributes(lp);
        progress_dialog= (TextView) v_dialog.findViewById(R.id.progress_dialog);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRlTagMore = (RelativeLayout) findViewById(R.id.rl_tag_more);
        mRlCommMore = (RelativeLayout) findViewById(R.id.rl_comm_more);
        mStTitle = (EditText) findViewById(R.id.et_title);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        myImageView= (ImageView) findViewById(R.id.myImageView);
        myImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AliyunVideoRecorder.startRecordForResult(VideoActivity.this,REQUEST_RECORD,aliyunSnap());
            }
        });
    }



    private void initAssetPath(){
        String path = StorageUtils.getCacheDirectory(this).getAbsolutePath() + File.separator+ Utils.QU_NAME + File.separator;
        eff_dirs = new String[]{
                null,
                path + "filter/chihuang",
                path + "filter/fentao",
                path + "filter/hailan",
                path + "filter/hongrun",
                path + "filter/huibai",
                path + "filter/jingdian",
                path + "filter/maicha",
                path + "filter/nonglie",
                path + "filter/rourou",
                path + "filter/shanyao",
                path + "filter/xianguo",
                path + "filter/xueli",
                path + "filter/yangguang",
                path + "filter/youya",
                path + "filter/zhaoyang"
        };
    }
    private void intentData(){
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        Bitmap bmp=(Bitmap) b.getParcelable("bitmap");
        myImageView.setImageBitmap(bmp);
        String p=b.getString("path");
        path=p;
        ChangeBitmap changeBitmap=new ChangeBitmap();
        imagePath = changeBitmap.saveBitmap(VideoActivity.this,bmp);
    }
    private void popupWin() {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_post_announcement_layout, null);
        mPopWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setBackgroundDrawable(getResources().getDrawable(R.color.custom_dark_gray));
        mPopWindow.setOutsideTouchable(true);
        ImageView iv_post_announcement = (ImageView) popupView.findViewById(R.id.iv_post_announcement);
        iv_post_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopWindow.dismiss();
            }
        });
        mHandler.sendEmptyMessageDelayed(2, 200);
    }
    //选择社区
    private void initCommMore() {
        mRlCommMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoActivity.this, SelectCommTagActivity.class);
                intent.putExtra("type","2");
                startActivity(intent);
            }
        });
    }

    //选择标签
    private void initTagMore() {
        mRlTagMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoActivity.this, SelectTagActivity.class);
                intent.putExtra("mPostTagsListReturn", (Serializable) mPostTagsList);
                intent.putExtra("type","2");
                startActivity(intent);
            }
        });
    }
    private void initToolbar() {
        mToolbar.setTitle("发布视频");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.post_activity_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ab_send:
                        OkHttpUtils.post()//
                                .addParams("vod_title","视频")
                                .addParams("vod_name","2017.9.28.mp4")
                                .addParams("vod_describe","测试")
                                .addParams("vod_label","测试")
                                .url("http://xingyuyou.com/aliyun/aliyun-php-sdk-vod/aliyun.php?")
                                .tag(this)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        // Log.e("hot", e.toString() + ":e");
                                        Toast.makeText(VideoActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onResponse(String response, int id) {
                                        if(StringUtils.isEmpty(mStTitle.getText().toString().trim())) {
                                            Toast.makeText(VideoActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                                        }else
                                        if (StringUtils.isEmpty((String) map.get("PostTags"))) {
                                            Toast.makeText(VideoActivity.this, "请选择标签", Toast.LENGTH_SHORT).show();
                                        }
                                        else if (StringUtils.isEmpty((String) map.get("GsonNameId"))) {
                                            Toast.makeText(VideoActivity.this, "请选发帖社区", Toast.LENGTH_SHORT).show();
                                        }else {
                                            normalDialog.show();
                                            // processDialog.show();
                                            Gson gson = new Gson();
                                            VideoBean videoBean = gson.fromJson(response, VideoBean.class);
                                            vod_id = videoBean.VideoId;
                                            uploadAuth = videoBean.UploadAuth;
                                            UploadAddress = videoBean.UploadAddress;
                                            uploader.addFile(path, getVodInfo());
                                            uploader.start();
                                        }
                                    }
                                });


                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }


    private VodInfo getVodInfo() {
        VodInfo vodInfo = new VodInfo();
        vodInfo.setTitle("标题" + 0);
        vodInfo.setDesc("描述." + 0);
        vodInfo.setCateId(0);
        vodInfo.setIsProcess(true);
        vodInfo.setCoverUrl(path);
        List<String> tags = new ArrayList<>();
        tags.add("标签" + 0);
        vodInfo.setTags(tags);
        if (isVodMode()) {
            vodInfo.setIsShowWaterMark(false);
            vodInfo.setPriority(7);
        } else {
            vodInfo.setUserData("自定义数据" + 0);
        }
        return vodInfo;
    }


    private boolean isVodMode() {
        return (null != uploadAuth && uploadAuth.length() > 0);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        if (!StringUtils.isEmpty(intent.getStringExtra("PostTags"))) {
            mPostTagsReturn = intent.getStringExtra("PostTags");
            mPostTagsList = (ArrayList<TagBean>) intent.getSerializableExtra("PostTagsList");
            map.put("PostTags", mPostTagsReturn);
            mPostTagsString = intent.getStringArrayExtra("PostTagsString");
            TextView tv_select_tag = (TextView) findViewById(R.id.tv_select_tag);
            TextView tv_tag_one = (TextView) findViewById(R.id.tv_tag_one);
            tv_tag_one.setText("");
            tv_tag_one.setVisibility(View.INVISIBLE);
            TextView tv_tag_two = (TextView) findViewById(R.id.tv_tag_two);
            tv_tag_two.setText("");
            tv_tag_two.setVisibility(View.INVISIBLE);
            TextView tv_tag_three = (TextView) findViewById(R.id.tv_tag_three);
            tv_tag_three.setText("");
            tv_tag_three.setVisibility(View.INVISIBLE);
            TextView tv_tag_four = (TextView) findViewById(R.id.tv_tag_four);
            tv_tag_four.setText("");
            tv_tag_four.setVisibility(View.INVISIBLE);
            TextView tv_tag_five = (TextView) findViewById(R.id.tv_tag_five);
            tv_tag_five.setText("");
            tv_tag_five.setVisibility(View.INVISIBLE);
            for (int i = 0; i < mPostTagsString.length; i++) {
                if (i == 0) {
                    tv_select_tag.setVisibility(View.GONE);
                    tv_tag_one.setText(mPostTagsString[0]);
                    tv_tag_one.setVisibility(View.VISIBLE);
                }
                if (i == 1) {
                    tv_tag_two.setText(mPostTagsString[1]);
                    tv_tag_two.setVisibility(View.VISIBLE);
                }
                if (i == 2) {
                    tv_tag_three.setText(mPostTagsString[2]);
                    tv_tag_three.setVisibility(View.VISIBLE);
                }
                if (i == 3) {
                    tv_tag_four.setText(mPostTagsString[3]);
                    tv_tag_four.setVisibility(View.VISIBLE);
                }
                if (i == 4) {
                    tv_tag_five.setText(mPostTagsString[4]);
                    tv_tag_five.setVisibility(View.VISIBLE);
                }
            }
        }

        if (!StringUtils.isEmpty(intent.getStringExtra("GsonNameId"))) {
            map.put("GsonNameId", intent.getStringExtra("GsonNameId"));
            PostCommClassName=intent.getStringArrayExtra("PostCommClassName");
            TextView tv_select_commu = (TextView) findViewById(R.id.tv_select_commu);
            TextView tv_tag_commu = (TextView) findViewById(R.id.tv_tag_commu);
            tv_tag_commu.setText("");
            tv_tag_commu.setVisibility(View.INVISIBLE);
            TextView tv_tag_commu2 = (TextView) findViewById(R.id.tv_tag_commu2);
            tv_tag_commu2.setText("");
            tv_tag_commu2.setVisibility(View.INVISIBLE);
            TextView tv_tag_commu3 = (TextView) findViewById(R.id.tv_tag_commu3);
            tv_tag_commu3.setText("");
            tv_tag_commu3.setVisibility(View.INVISIBLE);

            for(int i = 0; i < PostCommClassName.length; i++){
                if (i == 0) {
                    tv_select_commu.setVisibility(View.GONE);
                    tv_tag_commu.setVisibility(View.VISIBLE);
                    tv_tag_commu.setText(PostCommClassName[0]);
                }
                if (i == 1) {
                    tv_tag_commu2.setVisibility(View.VISIBLE);
                    tv_tag_commu2.setText(PostCommClassName[1]);
                }
                if (i == 2) {
                    tv_tag_commu3.setVisibility(View.VISIBLE);
                    tv_tag_commu3.setText(PostCommClassName[2]);
                }


            }

        }
    }
    /**
     * 负责处理编辑数据提交等事宜
     */
    private void dealEditData() {

        // params.put("fid", "0");
        //params.put("uid", "258");
/*        String s1 = StringUtils.isEmpty(mEtContent.getText().toString().trim()) == true ? "" : mEtContent.getText().toString().trim();
        if(idListBeen.size()!=0){
            String replies_content = s1.trim();
            if(replies_content.trim()!=null||replies_content.length()!=0){
                String[] split = replies_content.split(" ");
                for (int j = 0; j <split.length; j++) {
                    if(split[j].trim().length()!=0){
                        String substring = split[j].substring(0,1);
//                            存在
                        if(substring.equals("@")||substring.equals("＠")){
                            Map<String, Object> map = new HashMap<String, Object>();
                            String s2 = split[j].toString();
                            String s = cidNameMap.get(s2);
                            map.put("re_uid",s);
                            list_map.add(map);
                        }
                    }
                }
            }
        }*/
        JSONArray array = new JSONArray(list_map);
        Map<String, String> params = new HashMap<String, String>();
        params.put("fid",(String) map.get("GsonNameId"));
        params.put("uid", UserUtils.getUserId());
        params.put("subject", mStTitle.getText().toString().trim());
        params.put("message", StringUtils.isEmpty(mEtContent.getText().toString().trim()) == true ? "" : mEtContent.getText().toString().trim());
        params.put("vod_id",vod_id);
        params.put("post_type", "2");
        params.put("tags", (String) map.get("PostTags"));
        params.put("relation", array.toString());
        PostFormBuilder post = OkHttpUtils.post();
        File file = new File(imagePath);
        if (file.exists()) {
            //File file1 = new File(getExternalCacheDir() + "/tempCompress" + i + ".jpg");
            //NativeUtil.compressBitmap(mImageList.get(i), file1.getAbsolutePath());
            //以上是压缩代码
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            String s = "posts_image";
            post.addFile(s, file.getName(), file);
        }

        post.url(XingYuInterface.POST_POSTS_TEST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        //  mDialog.dismissDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        // mDialog.dismissDialog();
                        Intent intent = new Intent("updateFragment");
                        LocalBroadcastManager.getInstance(VideoActivity.this)
                                .sendBroadcast(intent);
                        Toast.makeText(VideoActivity.this, "发帖成功", Toast.LENGTH_SHORT).show();
                        VideoActivity.this.finish();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        //  mDialog.setProgressDialog(((int) (progress * 100)));
                    }
                });
    }
    private String path="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if(requestCode ==  2002){
            if(resultCode == Activity.RESULT_OK && data!= null){
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE,0);
                if(type ==  AliyunVideoRecorder.RESULT_TYPE_CROP){
                    String path = data.getStringExtra(AliyunVideoCrop.RESULT_KEY_CROP_PATH);
                    Toast.makeText(this,"文件路径为 "+ path + " 时长为 " +
                            data.getLongExtra(AliyunVideoCrop.RESULT_KEY_DURATION,0),Toast.LENGTH_SHORT).show();
                }else if(type ==  AliyunVideoRecorder.RESULT_TYPE_RECORD){
                    Toast.makeText(this,"文件路径为 "+
                            data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH),Toast.LENGTH_SHORT).show();
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"用户取消录制",Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode ==  REQUEST_RECORD){
            if(resultCode == Activity.RESULT_OK && data!= null){
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE,0);
                if(type ==  AliyunVideoRecorder.RESULT_TYPE_CROP){
                    path = data.getStringExtra(AliyunVideoCrop.RESULT_KEY_CROP_PATH);
                    if(path.endsWith(".mp4")){
                        //设置图片
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
                        media.setDataSource(path);
                        Bitmap bitmap = media.getFrameAtTime();
                        ChangeBitmap changeBitmap=new ChangeBitmap();
                        imagePath = changeBitmap.saveBitmap(VideoActivity.this,bitmap);
                        myImageView.setImageBitmap(bitmap);
                        //       Toast.makeText(VideoActivity.this,"用户取消裁剪1",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(VideoActivity.this,"请选择视频上传",Toast.LENGTH_SHORT).show();
                    }
                }else
                if(type ==  AliyunVideoRecorder.RESULT_TYPE_RECORD){
                    path=data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH);
                    //设置图片
                    if(path.endsWith(".mp4")){
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
                        media.setDataSource(path);
                        Bitmap bitmap = media.getFrameAtTime();
                        ChangeBitmap changeBitmap=new ChangeBitmap();
                        imagePath = changeBitmap.saveBitmap(VideoActivity.this,bitmap);
                        myImageView.setImageBitmap(bitmap);
                    }else {
                        Toast.makeText(VideoActivity.this,"请选择视频上传",Toast.LENGTH_SHORT).show();
                    }

                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"用户取消录制",Toast.LENGTH_SHORT).show();
            }
        }
    }

 /*   @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e("weiwei", "uncaughtException: " + throwable.toString());
    }*/
    //    拉起录制
    public AliyunSnapVideoParam aliyunSnap(){
        AliyunSnapVideoParam recordParam = new AliyunSnapVideoParam.Builder()
                //设置录制分辨率，目前支持360p，480p，540p，720p
                .setResulutionMode(AliyunSnapVideoParam.RESOLUTION_360P)
                //设置视频比例，目前支持1:1,3:4,9:16
                .setRatioMode(AliyunSnapVideoParam.RATIO_MODE_9_16)
                .setRecordMode(AliyunSnapVideoParam.RECORD_MODE_AUTO) //设置录制模式，目前支持按录，点录和混合模式
                .setFilterList(eff_dirs) //设置滤镜地址列表,具体滤镜接口接收的是一个滤镜数组
                .setBeautyLevel(80) //设置美颜度
                .setBeautyStatus(true) //设置美颜开关
                .setCameraType(CameraType.FRONT) //设置前后置摄像头
                .setFlashType(FlashType.ON) // 设置闪光灯模式
                .setNeedClip(true) //设置是否需要支持片段录制
                .setMaxDuration(30000) //设置最大录制时长 单位毫秒
                .setMinDuration(2000) //设置最小录制时长 单位毫秒
                .setVideQuality(videoQuality) //设置视频质量
                .setGop(5) //设置关键帧间隔
              //  .setCropMode(ScaleMode.PS)
                .build();

        return recordParam;
    }


    AliyunSnapVideoParam mCropParam = new AliyunSnapVideoParam.Builder()
            .setFrameRate(25) //设置帧率
            .setGop(5) //设置关键帧间隔
            .setCropMode(ScaleMode.PS) //设置裁剪模式，目前支持有黑边和无黑边两种
            .setVideQuality(videoQuality) //设置裁剪质量
            .setCropMode(ScaleMode.PS)
            .setResulutionMode(AliyunSnapVideoParam.RESOLUTION_360P) //设置分辨率，目前支持360p，480p，540p，720p
            .setRatioMode(AliyunSnapVideoParam.RATIO_MODE_9_16)//设置裁剪比例 目前支持1:1,3:4,9:16
            .setNeedRecord(true)//设置是否需要开放录制入口
            .setMinVideoDuration(4000) //设置过滤的视频最小长度 单位毫秒
            .setMaxVideoDuration(29 * 1000) //设置过滤的视频最大长度 单位毫秒
            .setMinCropDuration(3000) //设置视频最小裁剪时间 单位毫秒
            .build();
/*
    private class ImageAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(VideoActivity.this).inflate(R.layout.item_post_image, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((ItemViewHolder) holder).mClosePic.setVisibility(View.GONE);
            ((ItemViewHolder) holder).mPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toCheckPermission(Manifest.permission.CAMERA);
                    AliyunVideoRecorder.startRecordForResult(VideoActivity.this,REQUEST_RECORD,aliyunSnap());

                }
            });

        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                //最后一个,应该加载Footer
                return TYPE_FOOTER;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private ImageView mClosePic;
            private ImageView mPostImage;

            public ItemViewHolder(View itemView) {
                super(itemView);
               *//* if (itemView == mFooterView) {
                    return;
                }*//*
                mClosePic = (ImageView) itemView.findViewById(R.id.iv_close);
                mPostImage = (ImageView) itemView.findViewById(R.id.iv_post_image);
            }
        }
    }*/

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
                            .setMessage("就是胶囊需要这个权限，浏览您的设备图片、拍照等...")
                            .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(VideoActivity.this, new String[]{permission}, REQUEST_CODE);

                                }
                            })
                            .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(VideoActivity.this, "您已禁止该权限，如需请在设置中重新开启。", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE);
                }
            } else {
                MultiImageSelector.create()
                        .showCamera(true)
                        .count(5-mImageList.size())
                        .multi()
                        .start(VideoActivity.this, REQUEST_IMAGE);
            }
        }else {
            //进入到这里代表没有权限.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //已经禁止提示了
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("申请权限")
                        .setMessage("就是胶囊需要这个权限，下载游戏，发表图片等等...")
                        .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

                            }
                        })
                        .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(VideoActivity.this, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }


    }

    private void setAtImageSpan(String nameStr) {
        String content = String.valueOf(mEtContent.getText());
        if (content.endsWith("@") || content.endsWith("＠")) {
            content = content.substring(0, content.length() - 1);
        }
        String tmp = content;

        SpannableString ss = new SpannableString(tmp);

        if (nameStr != null) {
            String[] names = nameStr.split(" ");
            if (names != null && names.length > 0) {
                for (String name : names) {
                    if (name != null && name.trim().length() > 0) {
                        final Bitmap bmp = getNameBitmap(name);
                        // 这里会出现删除过的用户，需要做判断，过滤掉
                        if (tmp.indexOf(name) >= 0
                                && (tmp.indexOf(name) + name.length()) <= tmp
                                .length()) {
                            // 把取到的要@的人名，用DynamicDrawableSpan代替
                            ss.setSpan(
                                    new DynamicDrawableSpan(
                                            DynamicDrawableSpan.ALIGN_BASELINE) {
                                        @Override
                                        public Drawable getDrawable() {
                                            // TODO Auto-generated method stub
                                            BitmapDrawable drawable = new BitmapDrawable(
                                                    getResources(), bmp);
                                            drawable.setBounds(0, 0,
                                                    bmp.getWidth(),
                                                    bmp.getHeight());
                                            return drawable;
                                        }
                                    }, tmp.indexOf(name),
                                    tmp.indexOf(name) + name.length(),
                                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }
        }
        mEtContent.setTextKeepState(ss);
    }

    /**
     * 把返回的人名，转换成bitmap
     *
     * @param name
     * @return
     */
    private Bitmap getNameBitmap(String name) {

		/* 把@相关的字符串转换成bitmap 然后使用DynamicDrawableSpan加入输入框中 */
        name = "" + name;
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.other_pink));
        paint.setAntiAlias(true);
        paint.setTextSize(23);
        Rect rect = new Rect();
        paint.getTextBounds(name, 0, name.length(), rect);
        // 获取字符串在屏幕上的长度
        int width = (int) (paint.measureText(name));

        final Bitmap bmp = Bitmap.createBitmap(width, rect.height(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawText(name, rect.left, rect.height() - rect.bottom, paint);

        return bmp;
    }
    @Override
    protected void onResume() {
        super.onResume();
        DCAgent.resume(this);
        DCPage.onEntry("PostingActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("PostingActivity");
    }
    //监听edittext
    private class MyInputFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // TODO Auto-generated method stub
            if (source.toString().equalsIgnoreCase("@")
                    || source.toString().equalsIgnoreCase("＠")) {
                goAt();
            }

            return source;
        }
    }
    private void goAt() {
        StringBuffer tmp = new StringBuffer();
        // 把选中人的id已空格分隔，拼接成字符串
        for (Map.Entry<String, String> entry : cidNameMap.entrySet()) {
            tmp.append(entry.getKey() + " ");
        }

        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra(PersonActivity.KEY_SELECTED, tmp.toString());
        startActivityForResult(intent, CODE_PERSON);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意授权
                } else {
                    //用户拒绝授权
                    Toast.makeText(this, "您已拒绝调用相机权限，会导致无法拍摄图片", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private boolean isSTSMode() {
        if (!isVodMode()) {
            return (null != secretToken && secretToken.length() > 0 &&
                    null != expireTime && expireTime.length() > 0);
        }
        return false;
    }
}
