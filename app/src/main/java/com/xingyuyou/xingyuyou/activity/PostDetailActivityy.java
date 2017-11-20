package com.xingyuyou.xingyuyou.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.DynamicDrawableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.player.AliyunErrorCode;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.AliyunVidSource;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayer.utils.VcPlayerLog;
import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.EncryptUtils;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.Utils.Formatter;
import com.xingyuyou.xingyuyou.Utils.ImageUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.KeyboardUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.PlayAuthUtil;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.Post;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.RetrofitServiceManager;
import com.xingyuyou.xingyuyou.Utils.ScreenStatusController;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.EmotionKeyboard;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.GlobalOnItemClickManager;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.SpanStringUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.UIThreadUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.glide.GlideLoader;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.EmotionTabAdapter;
import com.xingyuyou.xingyuyou.adapter.PostCommoListAdapter;
import com.xingyuyou.xingyuyou.base.BaseActivity;
import com.xingyuyou.xingyuyou.bean.IdListBean;
import com.xingyuyou.xingyuyou.bean.VideoPingBean;
import com.xingyuyou.xingyuyou.bean.community.PostCommoBean;
import com.xingyuyou.xingyuyou.bean.community.PostDetailBean;
import com.xingyuyou.xingyuyou.bean.theme.IsTopWellBean;
import com.xingyuyou.xingyuyou.bean.user.PostBEan;
import com.xingyuyou.xingyuyou.fragment.BigEmotionFragment;
import com.xingyuyou.xingyuyou.fragment.ChangeFidDialogFragment;
import com.xingyuyou.xingyuyou.fragment.EmotionFragment;
import com.xingyuyou.xingyuyou.fragment.NormalDialogFragment;
import com.xingyuyou.xingyuyou.weight.CustomPopupWindow;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

import static com.aliyun.vodplayer.media.IAliyunVodPlayer.PlayerState.Idle;
import static com.aliyun.vodplayer.media.IAliyunVodPlayer.PlayerState.Stopped;
import static com.xingyuyou.xingyuyou.App.mContext;
import static com.xingyuyou.xingyuyou.R.id.civ_user_photo;
import static com.xingyuyou.xingyuyou.activity.PersonActivity.KEY_CID;
import static com.xingyuyou.xingyuyou.activity.PersonActivity.KEY_NAME;
import static java.lang.Integer.parseInt;


public class PostDetailActivityy extends BaseActivity {
    private ScreenStatusController mScreenStatusController = null;
    private List<PostCommoBean> mCommoList = new ArrayList<>();
    private List<PostCommoBean> mCommoAdapterList = new ArrayList<>();
    private List<PostCommoBean> mCommoAdapterListTemp = new ArrayList<>();
    private List<String> list_insetName=new ArrayList<>();
    private List<IdListBean> idListBeen =new ArrayList<>();
    private PostDetailBean mPostDetailBean;
    private PostCommoListAdapter mPostCommoListAdapter;
    private EditText post_editText;
    private TextView
            jump_sure;
    public static final int REQUEST_CODE = 1000;
    private ImageView detail_jump;
    private int i_select;
    int select;
    private int ediNum;
    private AliyunVodPlayerView mAliyunVodPlayerView = null;

    private String RequestId;
    private String PlayAuth;
    private AliyunPlayAuth mPlayAuth = null;
    List<Map<String, Object>> list_map = new ArrayList<Map<String, Object>>();

    //图片合集
    private ArrayList<String> mCommoThumbImageList = new ArrayList<>();
    private ArrayList<String> mCommoImageList = new ArrayList<>();
    private ArrayList<Integer> mCommoImageSizeList = new ArrayList<>();

    private FrameLayout extendView, emotionView;
    private LinearLayout contentView;
    private ImageView extendButton, emotionButton;
    private Button btnSend;
    private ImageView iv_user_medal;
    private  int  progroess_Num=0;
    private TextView mTvContent;
    private LinearLayout mRootImage;
    private Toolbar mToolbar;
    private ImageView mIvUserPhoto;
    private TextView mUserName;
    private TextView mPostTime;
    private TextView mCollectNum;
    private TextView mJiaonangNum;
    private EditText mEditText;
    private LinearLayout mLinearLayout;
    private LinearLayout mLinearLayout2;
    private Button mButton;
    private LinearLayout mEditParent;
    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private int PAGENUMUP = 1;
    private int PAGENUMDOWN = PAGENUMUP;
    private static final int REQUEST_IMAGE = 2;
    private static final int TYPE_FOOTER = 21;
    private ArrayList<String> mImageList = new ArrayList();
    private CustomDialog mDialog;
    private ListView mCommoListView;
    private EmotionKeyboard emotionKeyboard;
    private RadioGroup rgTipPoints;
    private RadioButton rbPoint;
    private static final int emsNumOfEveryFragment = 20;//每页的表情数量
    boolean isLoading = false;
    boolean NoData = false;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout mTopView;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private TextView mTvTitle;
    private ArrayList<String> mPost_images;
    private String mResponse;
    private String mResponse1;
    private String mResponseSend;
    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mBroadcastReceiver;
    private ArrayList<PostCommoBean.ChildBean> list;
    private TextView text_post_concern;
    private boolean isFirst = true;
    private SwipeRefreshLayout detail_swiperefreshlayout;
    private View headerView;
    private View hView;
    private SharedPreferences spp;
    private PostBEan postBEan;
    private int pageNUM;
    private RelativeLayout relativeLayout_postdetail;
    boolean visibility_Flag=true;
    private static final int CODE_PERSON = 1;
    int video=1;
    private int progressInt=0;
    /**
     * 存储@的cid、name对
     */
    private Map<String, String> cidNameMap = new HashMap<String, String>();

    private GlobalOnItemClickManager globalOnItemClickListener;
    private PostDetailBean postBean;
    /**
     * 选中的@的人的cid,进入@列表时，需要传递过去
     */
    private String selectedCids;
    private String nameStr;
    private ArrayList<String> idStr=new ArrayList<>();
    private ArrayList<String> tmpCidStr;
    private int replay_tag=0;
    private String vod_id;
    private int isOver=0;
    //创建播放器
  //  private AliyunVodPlayer mAliyunVodPlayer;
    private SurfaceView surfaceView;
    private AliyunVodPlayer aliyunVodPlayer;
    //用来记录前后台切换时的状态，以供恢复。
    private IAliyunVodPlayer.PlayerState mPlayerState;
    private AliyunVidSource mVidSource = null;
    private AliyunLocalSource mLocalSource = null;
    public boolean mAutoPlay = false;
    private boolean inSeek = false;
    private boolean mMute= false;
    private List<String> logStrs = new ArrayList<>();
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SS");
    private static final String TAG = PostDetailActivityy.class.getSimpleName();
    private TextView positionTxt;
    private TextView durationTxt;
    private SeekBar progressBar;
    private static final int video_visit = 9;
    private static final int video_gone = 10;
    private static final int video_fristgone = 8;
    private RelativeLayout rrr;
    private ImageView playBtn;
    private ImageView rotate;
    private ImageView video_back;
    private String posts_image;
    private ImageView vodeo_background;
    private TextView text_vodeo_head;
    private TextView peopleNum;
    private TextView playNum;
    private TextView video_content;
    //播放结束为ture    默认为false
    private boolean isCompleted = false;
    private String mAuthinfo = null;
    private String mVid = null;
    private boolean replay = false;
    private ImageView mBt_add_image;
    private ImageView mImg_reply_layout_emotion;
    private LinearLayout mEmotion_layout;
    private FrameLayout mExtend_layout;
    private InputMethodManager mInputManager;
    private EditText mEdit_text;
    private ViewPager mPager;
    private ImageView mIv_tab_small_emotion;
    private ImageView mIv_tab_big_emotion;
    private LinearLayout mExtend_layout_button;
    private View v_dialog;
    private AlertDialog normalDialog;
    private TextView progress_dialog;
    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mResponse = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(mResponse);
                    JSONObject ja = jo.getJSONObject("data");
                    Gson gson = new Gson();
                    mPostDetailBean = gson.fromJson(ja.toString(), PostDetailBean.class);
                    getPostIsTopOrWell(mPostDetailBean.getId());
                    if(mPostDetailBean.getVod_id()!=null&&mPostDetailBean.getVod_id().length()>0){
                        setshipinValues();
                        posts_image = getIntent().getStringExtra("posts_image");
                        Glide.with(PostDetailActivityy.this).load(mPostDetailBean.getThumbnail_image().get(0).getThumbnail_image())
                                .into(vodeo_background);
                    }else {
                        setValues();
                    }
                    Log.e("testa", "handleMessage: "+mPostDetailBean.toString());
                    detail_swiperefreshlayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (msg.what == 2) {
                mResponse1 = (String) msg.obj;
                if (mResponse1.contains("\"data\":null")) {
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("爱评论的小婊贝们运气不会差哦（*>.<*）~ @");
                    NoData = true;
                    return;
                }

                JSONObject jo = null;
                try {
                    jo = new JSONObject(mResponse1);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mCommoList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostCommoBean>>() {
                            }.getType());
                    mCommoAdapterList.addAll(mCommoList);
                    mPostCommoListAdapter.notifyDataSetChanged();
                    isLoading = false;
                    //图片合集
                    getAllPics(mCommoList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                detail_swiperefreshlayout.setRefreshing(false);

                if (isFirst) {
                    SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                    int index = sp.getInt("index"+getIntent().getStringExtra("post_id"), 0);
                    mCommoListView.setSelection(index %20-1);
                    mCommoListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        private int mLvIndext;
                        boolean scrollStatus;
                        @Override
                        public void onScrollStateChanged(AbsListView absListView, int i) {
                            switch (i) {
                                // 滚动之前,手还在屏幕上  记录滚动前的下标
                                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                                    //view.getLastVisiblePosition()得到当前屏幕可见的第一个item在整个listview中的下标
                                    mLvIndext = absListView.getLastVisiblePosition();
                                    break;
                                //滚动停止
                                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                                    //记录滚动停止后 记录当前item的位置
                                    int scrolled = absListView.getLastVisiblePosition();
                                    //滚动后下标大于滚动前 向下滚动了
                                    if (scrolled > mLvIndext) {
                                        scrollStatus = false;
                                    }
                                    //向上滚动了
                                    else {
                                        scrollStatus = true;
                                    }
                                    break;
                            }
                        }
                        @Override
                        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                            if (i + i1 == i2) {
                                if (!isLoading) {
                                    isLoading = true;
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            PAGENUMDOWN++;
                                            initCommoData(PAGENUMDOWN,2);
                                        }
                                    }, 200);
                                }
                            }
                        }
                    });
                    isFirst = false;
                }
            }

            //false
            if (msg.what == 3) {
                mCommoAdapterListTemp.clear();
                if (PAGENUMUP == 1) {
                    mCommoListView.addHeaderView(hView);
                }
                mResponse1 = (String) msg.obj;
                if (mResponse1.contains("\"data\":null")) {
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("爱评论的小婊贝们运气不会差哦（*>.<*）~ @");
                    NoData = true;
                    return;
                }
                mResponseSend = mResponse1;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(mResponse1);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mCommoList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostCommoBean>>() {
                            }.getType());
                    mCommoAdapterListTemp.addAll(mCommoAdapterList);
                    mCommoAdapterList.clear();
                    mCommoAdapterList.addAll(mCommoList);
                    mCommoAdapterList.addAll(mCommoAdapterListTemp);

                    mPostCommoListAdapter.notifyDataSetChanged();
                    isLoading = false;
                    //图片合集
                    getAllPics(mCommoList);
                    detail_swiperefreshlayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 4) {
                mResponse1 = (String) msg.obj;
                if (mResponse1.contains("\"data\":null")) {
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("爱评论的小婊贝们运气不会差哦（*>.<*）~ @");
                    NoData = true;
                    return;
                }
                JSONObject jo = null;
                try {
                    jo = new JSONObject(mResponse1);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mCommoList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostCommoBean>>() {
                            }.getType());
                    mCommoAdapterList.addAll(mCommoList);
                    mPostCommoListAdapter.notifyDataSetChanged();
                    isLoading = false;
                    //图片合集
                    getAllPics(mCommoList);

                    //跳楼
                    if(select==1){
                        if(i_select==1){
                   /*         if(post_editText.getText().length()==0){
                                mCommoListView.setSelection(postBEan.count_pages%20);
                            }else {
                                mCommoListView.setSelection(Integer.parseInt(post_editText.getText().toString())%20);
                            }*/
                            mCommoListView.setSelection(ediNum%20);

                        }else {
                   /*         if(post_editText.getText().length()==0){
                                mCommoListView.setSelection(postBEan.count_pages%20-1);
                            }else {
                                Toast.makeText(PostDetailActivityy.this,Integer.parseInt(post_editText.getText().toString())+"",Toast.LENGTH_SHORT).show();
                                mCommoListView.setSelection(Integer.parseInt(post_editText.getText().toString())%20);
                            }*/
                            mCommoListView.setSelection(ediNum% 20 - 1);
                        }
                    }else if(select==2) {
                        mCommoListView.setSelection(mCommoListView.getBottom());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                detail_swiperefreshlayout.setRefreshing(false);
                if (isFirst) {
                    mCommoListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        private int mLvIndext;
                        boolean scrollStatus;
                        @Override
                        public void onScrollStateChanged(AbsListView absListView, int i) {
                            switch (i) {
                                // 滚动之前,手还在屏幕上  记录滚动前的下标
                                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                                    //view.getLastVisiblePosition()得到当前屏幕可见的第一个item在整个listview中的下标
                                    mLvIndext = absListView.getLastVisiblePosition();
                                    break;
                                //滚动停止
                                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                                    //记录滚动停止后 记录当前item的位置
                                    int scrolled = absListView.getLastVisiblePosition();
                                    //滚动后下标大于滚动前 向下滚动了
                                    if (scrolled > mLvIndext) {
                                        scrollStatus = false;
                                    }
                                    //向上滚动了
                                    else {
                                        scrollStatus = true;
                                    }
                                    break;
                            }
                        }
                        @Override
                        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                            if (i + i1 == i2) {
                                if (!isLoading) {
                                    isLoading = true;
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            PAGENUMDOWN++;
                                            initCommoData(PAGENUMDOWN,2);
                                        }
                                    }, 200);
                                }
                            }
                        }
                    });
                    isFirst = false;
                }
            }


    //连点消失
            if(msg.what==video_fristgone){
                video_gone();
            }


            if(msg.what==video_gone){
                video_gone();
            }

        }
    };
    private ImageView chongbo;
    private VideoPingBean videoPingBean;
    private ImageView video_share;
    private ProgressBar progressBar_onloading;
    private TextView mTv_cancel_top;
    private TextView mTv_cancel_well;
    private CustomPopupWindow mPopWindow;
    private ImageView mIv_review;
    private ImageView mIv_banzhu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail_activityy);
        //初始化键盘
        initKeyBoardView();
        //初始化界面
        initVideo();
        View v=View.inflate(PostDetailActivityy.this,R.layout.alivc_dialog_replay,null);
        v.setVisibility(View.GONE);
        initView();
        //初始化toolbar
        initToolBar();
        String vod_id= getIntent().getStringExtra("vod_id");
        isVodeo(vod_id);
        //请求评论内容
        initData();
        //初始化数据
        if (isFirst) {
            SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
            int pageSum = sp.getInt("pageSum"+getIntent().getStringExtra("post_id"), 1);
            int index = sp.getInt("index", 1);
            initCommoData(pageSum, 2);
            PAGENUMUP = pageSum;
            PAGENUMDOWN = pageSum;
            //mCommoListView.smoothScrollToPosition(top);
            if(pageSum==1){
                mCommoListView.addHeaderView(hView);
            }
        }
        //适配数据
        initCommoListView();
       // handler = new Handler(new MsgCallBack());
        //alivc_dialog_replay
        //设置缓冲监听状态

    }
    private void getPostIsTopOrWell(String tid){
        //获取帖子是否加精置顶
        RetrofitServiceManager.getInstance()
                .create(Post.class)
                .IsTopOrWell(UserUtils.getUserId(),tid, getIntent().getStringExtra("fid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IsTopWellBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull IsTopWellBean isTopWellBean) {

                        if (isTopWellBean.getData().getIs_top().equals("1")) {
                            mTv_cancel_top.setText("取消置顶");

                        }else {
                            mTv_cancel_top.setText("置顶");
                        }
                        if (isTopWellBean.getData().getIs_well().equals("1")) {
                            mTv_cancel_well.setText("取消加精");
                        }else {
                            mTv_cancel_well.setText("加精");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
//初始化视频控件
    private void initVideo(){
        //初始化缓冲状态下dialog
 /*       v_dialog = View.inflate(PostDetailActivityy.this, R.layout.video_progress_dialog,null);
        normalDialog = new AlertDialog.Builder(PostDetailActivityy.this,R.style.MyCommonDialog).create();
        normalDialog.setView(v_dialog);
        normalDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp=normalDialog.getWindow().getAttributes();
        lp.dimAmount=0.4f;
        normalDialog.getWindow().setAttributes(lp);
        progress_dialog= (TextView) v_dialog.findViewById(R.id.progress_dialog);*/
        progressBar_onloading = (ProgressBar) findViewById(R.id.loading_process_dialog_progressBar);


        //初始化视频控件
        chongbo = (ImageView) findViewById(R.id.chongbo);
        rrr = (RelativeLayout) findViewById(R.id.rrr);
        progressBar=   (SeekBar) findViewById(R.id.progress);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        rotate = (ImageView) findViewById(R.id.rotate);
        aliyunVodPlayer = new AliyunVodPlayer(this);
        video_back = (ImageView) findViewById(R.id.video_back);
        positionTxt = (TextView) findViewById(R.id.currentPosition);
        durationTxt = (TextView) findViewById(R.id.totalDuration);
        vodeo_background = (ImageView) findViewById(R.id.vodeo_background);
        video_share = (ImageView) findViewById(R.id.video_share);

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(TAG, "surfaceCreated");
                aliyunVodPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d(TAG, "surfaceChanged");
                aliyunVodPlayer.surfaceChanged();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d(TAG, "surfaceDestroyed");
             //   aliyunVodPlayer.stop();
            }
        });
        initVodPlayer();
        if(mVidSource != null) {
            aliyunVodPlayer.prepareAsync(mVidSource);
        } else if(mPlayAuth != null) {
            aliyunVodPlayer.prepareAsync(mPlayAuth);
        } else if(mLocalSource != null) {
            aliyunVodPlayer.prepareAsync(mLocalSource);
        }
        //seekbar滑动监听
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser
                        && aliyunVodPlayer != null
                        ) {
                    aliyunVodPlayer.seekTo(progress);
                    logStrs.add(format.format(new Date()) + " seek开始");
                    if(isCompleted) {
                        //播放完成了，不能seek了
                        inSeek = false;
                    }else{
                        inSeek = true;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        //缓冲
        aliyunVodPlayer.setOnLoadingListener(new IAliyunVodPlayer.OnLoadingListener() {
            @Override
            public void onLoadStart() {
                Log.d("qqqq1", "播放进度更新--- 开始");
                playBtn.setVisibility(View.GONE);
                progressBar_onloading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadEnd() {
                playBtn.setVisibility(View.GONE);
                Log.d("qqqq2", "结束 ");
            //    normalDialog.dismiss();
                progressBar_onloading.setVisibility(View.GONE);
            }

            @Override
            public void onLoadProgress(int i) {
                Log.d("qqqq3", i+"whj");
                playBtn.setVisibility(View.GONE);
            //    Toast.makeText(PostDetailActivityy.this,"网络不佳",Toast.LENGTH_SHORT).show();
/*
                String progress= "缓冲进度"+i+"%";
                progress_dialog.setText(progress);*/

                progressBar_onloading.setVisibility(View.VISIBLE);

            }
        });

        //点击切换横竖屏
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //变成竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    //变成横屏了
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });


        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playBtn.getVisibility()==View.VISIBLE){
                    Message msg = new Message();
                    msg.what = video_fristgone;
                    handler.sendMessage(msg);
                    handler.removeMessages(video_gone);
                }else {
                    video_visit();
                    handler.sendEmptyMessageDelayed(video_gone, 3000);
                }
            }
        });
        //视频中的返回键
        video_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //变成竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    //变成横屏了
                   finish();
                }
            }
        });


        chongbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isCompleted = false;
                inSeek = false;
                //replay();
       /*        aliyunVodPlayer.stop();
                aliyunVodPlayer.release();*/
                aliyunVodPlayer.replay();
                showVideoProgressInfo();
                playBtn.setImageResource(R.mipmap.video_stop);
                playBtn.setTag("1");
                chongbo.setVisibility(View.GONE);
                replay_tag=0;
            }
        });



        //设置视频分享
        video_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePost();
            }
        });

    }



/*    class MsgCallBack implements Handler.Callback
    {

        @Override
        public boolean handleMessage(Message msg)
        {
            switch (msg.what) {
                case 99:
                    int p=msg.arg1;
                    String progress= "缓冲进度"+msg.arg1+"%";
                    progress_dialog.setText(progress);
                    if(p==100) {
                        normalDialog.dismiss();
                    }
                    break;
                case 98:
                    normalDialog.show();
                  //  Toast.makeText(PostDetailActivityy.this, (String)msg.obj, Toast.LENGTH_LONG).show();
                    break;


                default:
                    break;
            }
            return true;
        }

    }*/
    public void postProgress(int progress)
    {
       /* int progress = (int) (uploadedSize * 100 / totalSize);*/
        Message message = Message.obtain();
        message.what = 99;
        message.arg1 = progress;
        handler.sendMessage(message);
    }


    public void showDialog()
    {
       /* int progress = (int) (uploadedSize * 100 / totalSize);*/
        Message message = Message.obtain();
        message.what = 98;
        handler.sendMessage(message);
    }
    //设置进度条可见
    private void video_visit(){
        if(replay_tag==0){
            playBtn.setVisibility(View.VISIBLE);
        }else if(replay_tag==1){
            playBtn.setVisibility(View.GONE);
        }

        rotate.setVisibility(View.VISIBLE);
        positionTxt.setVisibility(View.VISIBLE);
        durationTxt.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

    }


    //设置进度条不可见
    private void video_gone(){
        playBtn.setVisibility(View.GONE);
        rotate.setVisibility(View.GONE);
        positionTxt.setVisibility(View.GONE);
        durationTxt.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

    }


    private void void_pingzheng(String vod_id){
        OkHttpUtils.post()
                .addParams("vod_id",vod_id)
                .url("http://xingyuyou.com/aliyun/aliyun-php-sdk-vod/aliyun_play.php?")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {


                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson=new Gson();
                        videoPingBean = gson.fromJson(response, VideoPingBean.class);
                        setPlaySource(videoPingBean.PlayAuth);
                    }
                });
    }
    private void updatePostCommoList() {
        mLocalBroadcastManager = LocalBroadcastManager
                .getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("updatePostCommoList");
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("updatePostCommoList")) {
                    PostCommoBean.ChildBean childBean = (PostCommoBean.ChildBean) intent.getSerializableExtra("childBean");
                    if (mCommoAdapterList.get(intent.getIntExtra("position", 0)).getChild() == null) {
                        list = new ArrayList<>();
                        list.add(childBean);
                        mCommoAdapterList.get(intent.getIntExtra("position", 0)).setChild(list);
                    } else if (mCommoAdapterList.get(intent.getIntExtra("position", 0)).getChild().size() == 1) {
                        list = new ArrayList<>();
                        list.add(mCommoAdapterList.get(intent.getIntExtra("position", 0)).getChild().get(0));
                        list.add(childBean);
                        mCommoAdapterList.get(intent.getIntExtra("position", 0)).setChild(list);
                        mPostCommoListAdapter.notifyDataSetChanged();
                    }
                }
                mPostCommoListAdapter.notifyDataSetChanged();
            }

        };
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    //********************************************以下是初始化代码**************************************************
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("帖子详情");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.inflateMenu(R.menu.post_share_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ab_share:
                        sharePost();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 分享帖子
     */
    private void sharePost() {
        if (!UserUtils.logined()) {
            IntentUtils.startActivity(PostDetailActivityy.this, LoginActivity.class);
            return;
        }
        if (mPostDetailBean == null)
            return;
        UMImage thumb = new UMImage(PostDetailActivityy.this,
                mPostDetailBean.getThumbnail_image().get(0).getThumbnail_image());
        String url = "http://xingyuyou.com/app.php/Share/index";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url);
        stringBuilder.append("/tid/");
        stringBuilder.append(mPostDetailBean.getId());
        stringBuilder.append("/uid/");
        stringBuilder.append(UserUtils.getUserId());
        stringBuilder.append("/key/");
        stringBuilder.append(EncryptUtils.encryptMD5ToString("xingyuyou" + mPostDetailBean.getId() + UserUtils.getUserId()));
        UMWeb web = new UMWeb(stringBuilder.toString());
        web.setTitle(mPostDetailBean.getSubject());//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(mPostDetailBean.getMessage());//描述
        new ShareAction(PostDetailActivityy.this).withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setCallback(umShareListener).open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(PostDetailActivityy.this, platform + " 分享成功", Toast.LENGTH_SHORT).show();
            OkHttpUtils.post()
                    .addParams("pid", getIntent().getStringExtra("post_id"))
                    .addParams("uid", UserUtils.getUserId())
                    .addParams("share_from", String.valueOf(platform.toSnsPlatform().mPlatform))
                    .url(XingYuInterface.SHARE_COMPLETE)
                    .tag(this)//
                    .build()//
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }


                        @Override
                        public void onResponse(String response, int id) {

                        }
                    });
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(PostDetailActivityy.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(PostDetailActivityy.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private void showNormalDialogFragment(String type_values,String type,String type_houtai){
        NormalDialogFragment normalDialogFragment = new NormalDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fid", getIntent().getStringExtra("fid"));
        bundle.putString("tid", mPostDetailBean.getId());
        bundle.putString("type", type);
        bundle.putString("type_values", type_values);
        bundle.putString("type_houtai", type_houtai);
        normalDialogFragment.setArguments(bundle);
        normalDialogFragment.show(getSupportFragmentManager(), "normalDialogFragment");
    }
    private void initView() {
        mPopWindow = new CustomPopupWindow(this);
        TextView mTv_post_operation = (TextView) mPopWindow.getContentView().findViewById(R.id.tv_post_operation);
        mTv_cancel_well = (TextView) mPopWindow.getContentView().findViewById(R.id.tv_cancel_well);
        mTv_cancel_top = (TextView) mPopWindow.getContentView().findViewById(R.id.tv_cancel_top);
        TextView tv_delete_post = (TextView) mPopWindow.getContentView().findViewById(R.id.tv_delete_post);
        TextView tv_remove_post = (TextView) mPopWindow.getContentView().findViewById(R.id.tv_remove_post);
        mTv_post_operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFidDialogFragment changeFidDialogFragment = new ChangeFidDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fid", getIntent().getStringExtra("fid"));
                bundle.putString("tid", mPostDetailBean.getId());
                changeFidDialogFragment.setArguments(bundle);
                changeFidDialogFragment.show(getSupportFragmentManager(), "ChangeFidDialogFragment");
            }
        });
        mTv_cancel_well.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialogFragment("本操作会加精该帖哦~","2","1");
            }
        });
        mTv_cancel_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialogFragment("本操作会置顶该帖哦~","1","1");
            }
        });
        tv_delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialogFragment("确认要删除此贴么？","1","2");
            }
        });
        tv_remove_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialogFragment("要将此贴移出本专区么？","2","2");
            }
        });
        mIv_review = (ImageView) findViewById(R.id.iv_review);
        mIv_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.showUp(mIv_review);
            }
        });
        playBtn = (ImageView) findViewById(R.id.playBtn);
        mEdit_text.setFilters(new InputFilter[] { new MyInputFilter() });
        //跳楼功能空间
        relativeLayout_postdetail = (RelativeLayout) findViewById(R.id.relativeLayout_postdetail);
        post_editText = (EditText) findViewById(R.id.post_editText);
        jump_sure = (TextView
                ) findViewById(R.id.jump_sure);
        detail_jump = (ImageView) findViewById(R.id.detail_jump);
        //跳楼功能
        detail_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(visibility_Flag){
                    relativeLayout_postdetail.setVisibility(View.VISIBLE);
                    detail_jump.setImageResource(R.mipmap.ic_open);
                    visibility_Flag=false;
                }else {
                    detail_jump.setImageResource(R.mipmap.ic_close);
                    relativeLayout_postdetail.setVisibility(View.GONE);
                    visibility_Flag=true;
                }
            }
        });
        jump_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳
                jumpBuild();
            }
        });
        post_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    // do something
                    jumpBuild();
                }
                return true;
            }
        });
        View loadingData = View.inflate(PostDetailActivityy.this, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        //底部收藏点赞数量
        mCollectNum = (TextView) findViewById(R.id.tv_collect_num);
        mJiaonangNum = (TextView) findViewById(R.id.tv_jiaonang_num);
        //底部两个editText视图parent
        mEditText = (EditText) findViewById(R.id.bottom_edit_text);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_edit_parent);
        mCommoListView = (ListView) findViewById(R.id.listView);
        mCommoListView.setDividerHeight(0);
        mCommoListView.addFooterView(loadingData);
        detail_swiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.detail_swiperefreshlayout);
        detail_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGENUMUP--;
                if(PAGENUMUP>=1){
                    initCommoData(PAGENUMUP, 3);
                }else {
                    //Toast.makeText(PostDetailActivityy.this,"当前已经是第一页",Toast.LENGTH_LONG).show();
                    detail_swiperefreshlayout.setRefreshing(false);
                }
            }
        });
    }
    public void dialogShow() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(PostDetailActivityy.this);
        normalDialog.setTitle("确定不再关注ta了吗?");
        normalDialog.setPositiveButton("我确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //   setBooderPink();
                        if (mPostDetailBean.getRelation() == 1) {
                            initConcern("1");
                        } else if (mPostDetailBean.getRelation() == 2) {
                            initConcern("2");
                        }
                        Toast.makeText(PostDetailActivityy.this, "已取消关注（*>.<*）~ @", Toast.LENGTH_SHORT).show();
                    }
                });
        normalDialog.setNegativeButton("再想想",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        normalDialog.show();
    }

    private void initCardHeader(){
        iv_user_medal = (ImageView) headerView.findViewById(R.id.iv_user_medal);
        mIv_banzhu = (ImageView) headerView.findViewById(R.id.iv_banzhu);
        mTvTitle = (TextView) headerView.findViewById(R.id.tv_title);
        mTvContent = (TextView) headerView.findViewById(R.id.tv_content);
        mUserName = (TextView) headerView.findViewById(R.id.tv_user_name);
        mPostTime = (TextView) headerView.findViewById(R.id.tv_post_time);
        mRootImage = (LinearLayout) headerView.findViewById(R.id.ll_root_image);
        mIvUserPhoto = (ImageView) headerView.findViewById(civ_user_photo);
        //headerView.setVisibility(View.GONE);
        text_post_concern = (TextView) headerView.findViewById(R.id.text_post_concern);
         //点击关注
        text_post_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //不能关注自己
                if(UserUtils.getNickName().equals(mPostDetailBean.getNickname())){
                    Toast.makeText(PostDetailActivityy.this,"亲不能关注自己",Toast.LENGTH_SHORT).show();
                }else {
                    if (mPostDetailBean.getRelation() == 0) {
                        initConcern("0");
                    } else if (mPostDetailBean.getRelation() == 1 || mPostDetailBean.getRelation() == 2) {
                        dialogShow();
                    }
                }
            }
        });


        //第一个布局
        //点击头像跳转
        mIvUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(PostDetailActivityy.this, LoginActivity.class);
                    return;
                } else {
                    if (UserUtils.getUserId().equals(mPostDetailBean.getUid())) {
                        startActivity(new Intent(PostDetailActivityy.this, UserPageActivity.class));
                    } else {
                        Intent intent1 = new Intent(PostDetailActivityy.this, OtherPageActivity.class);
                        intent1.putExtra("re_uid", mPostDetailBean.getUid());
                        startActivity(intent1);
                    }
                }
            }
        });
    }
    /**
     * 获取文章内容
     */
    @Override
    public void initData() {
        OkHttpUtils.post()
                .addParams("pid", getIntent().getStringExtra("post_id"))
                .addParams("uid", UserUtils.getUserId())
                .addParams("fid",
                        getIntent().getStringExtra("fid") != null ? getIntent().getStringExtra("fid") : "0")
                .url(XingYuInterface.GET_POSTS_INFO)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(final String response, int id) {
                        handler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }


    private void isVodeo(String  vod_id){
        if(vod_id!=null&&vod_id.length()>0){
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            rrr.setVisibility(View.VISIBLE);

            //加载视频头布局
            void_pingzheng(vod_id);
            headerView = View.inflate(PostDetailActivityy.this, R.layout.video_head, null);
            initCardHeader();
            hView=headerView;
            initvoidHead();
            mToolbar.setVisibility(View.GONE);
        }else {
            //加载帖子头布局
            headerView = View.inflate(PostDetailActivityy.this, R.layout.part_post_header, null);
            initCardHeader();
            hView=headerView;
        }
    }

    private void initvoidHead(){
        text_vodeo_head = (TextView) headerView.findViewById(R.id.text_vodeo_head);
        peopleNum = (TextView) headerView.findViewById(R.id.peopleNum);
        playNum = (TextView) headerView.findViewById(R.id.playNum);
        video_content = (TextView) headerView.findViewById(R.id.video_content);
    }
    /**
     * 获取评论内容
     *
     * @param PAGENUM
     */
    private void initCommoData(int PAGENUM, final int isLoadMore) {
        OkHttpUtils.post()//
                .addParams("page", String.valueOf(PAGENUM))
                .addParams("tid", getIntent().getStringExtra("post_id"))
                .addParams("uid", UserUtils.getUserId())
                .url(XingYuInterface.GET_FORUMS_LIST)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                        detail_swiperefreshlayout.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        handler.obtainMessage(isLoadMore , response).sendToTarget();
                        Gson gson=new Gson();
                        postBEan = gson.fromJson(response, PostBEan.class);
                        pageNUM=postBEan.count_pages;
                        post_editText.setHint(postBEan.count_pages+"");
               /*         if(postBEan.count_pages%20!=0){
                            pageNUM=postBEan.count_pages+1;
                        }else if(postBEan.count_pages%20==0){
                            pageNUM=postBEan.count_pages;
                        }*/
                    }
                });
    }

    //*********************************************以下是软键盘设置代码*****************************************************





    private void initKeyBoardView() {
        mLinearLayout2 = (LinearLayout) findViewById(R.id.ll_emotion_parent1);
        ArrayList<Fragment> mFragments = new ArrayList<>();
        //获取软键盘管理类
        mInputManager = (InputMethodManager) getSystemService
                (Context.INPUT_METHOD_SERVICE);
        //editText
        mEdit_text = (EditText) findViewById(R.id.edit_text);
        mEdit_text.addTextChangedListener(new ButtonBtnWatcher());
        mEdit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmotion_layout.setVisibility(View.GONE);
                mExtend_layout.setVisibility(View.GONE);
            }
        });
        //表情选择
        mIv_tab_small_emotion = (ImageView) findViewById(R.id.iv_tab_small_emotion);
        mIv_tab_big_emotion = (ImageView) findViewById(R.id.iv_tab_big_emotion);
        //删除表情
        ImageView iv_tab_delete_emotion = (ImageView) findViewById
                (R.id.iv_tab_delete_emotion);
        iv_tab_delete_emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEdit_text.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_DEL));
            }
        });
        mEdit_text.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_DEL));
        //初始化两个表情fragment
        EmotionFragment emotionFragment = new EmotionFragment();
        BigEmotionFragment bigEmotionFragment = new BigEmotionFragment();
        emotionFragment.BindEditText(mEdit_text);
        bigEmotionFragment.BindEditText(mEdit_text);
        mFragments.add(emotionFragment);
        mFragments.add(bigEmotionFragment);
        EmotionTabAdapter emotionTabAdapter = new EmotionTabAdapter
                (getSupportFragmentManager(), mFragments);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(emotionTabAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mIv_tab_small_emotion.setBackgroundResource(R.color.custom_gray);
                    mIv_tab_big_emotion.setBackgroundResource(R.color.white);
                } else {
                    mIv_tab_small_emotion.setBackgroundResource(R.color.white);
                    mIv_tab_big_emotion.setBackgroundResource(R.color.custom_gray);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //点击切换表情布局
        mIv_tab_small_emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(0);
            }
        });
        mIv_tab_big_emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(1);
            }
        });
        //选择布局按钮
        mBt_add_image = (ImageView) findViewById(R.id.bt_add_image);
        mImg_reply_layout_emotion = (ImageView) findViewById
                (R.id.img_reply_layout_emotion);
        //布局
        mEmotion_layout = (LinearLayout) findViewById(R.id.emotion_layout);
        mExtend_layout = (FrameLayout) findViewById(R.id.extend_layout);

        mImg_reply_layout_emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftInput();
                mEmotion_layout.setVisibility(View.VISIBLE);
                mExtend_layout.setVisibility(View.GONE);
            }
        });
        mBt_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftInput();
                mEmotion_layout.setVisibility(View.GONE);
                mExtend_layout.setVisibility(View.VISIBLE);
                if (mImageList.size()==0){
                    mExtend_layout_button.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        });
        //扩展功能父布局
        mExtend_layout_button = (LinearLayout) findViewById(R.id.extend_layout_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_all_image);
        setUpExtendView();
        //选择图片
        Button btn_replay_layout_pic = (Button) findViewById(R.id.btn_replay_layout_pic);
        btn_replay_layout_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExtend_layout_button.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        //@功能
        Button btn_replay_layout_at = (Button) findViewById(R.id.btn_replay_layout_at);
        btn_replay_layout_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivityy.this, PersonActivity.class);
                startActivityForResult(intent, CODE_PERSON);
            }
        });
        //提交评论
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReply();
            }
        });
    }
    private void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(mEdit_text.getWindowToken(), 0);
    }
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


    /**
     * 软键盘文本内容监听
     */
    class ButtonBtnWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(mEdit_text.getText().toString())) { //有文本内容，按钮为可点击状态
                btnSend.setBackgroundResource(R.drawable.shape_button_reply_button_clickable);
                btnSend.setTextColor(getResources().getColor(R.color.light_white));
            } else { // 无文本内容，按钮为不可点击状态
                btnSend.setBackgroundResource(R.drawable.shape_button_reply_button_unclickable);
                btnSend.setTextColor(getResources().getColor(R.color.reply_button_text_disable));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    /**
     * 设置扩展布局下的视图
     */
    private void setUpExtendView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PostDetailActivityy.this, LinearLayoutManager.HORIZONTAL, false));
        mImageAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mImageAdapter);

    }


    //***********************************************以下是评论列表****************************************************
    private void initCommoListView() {
        mPostCommoListAdapter = new PostCommoListAdapter(PostDetailActivityy.this, mCommoAdapterList);
        mCommoListView.setAdapter(mPostCommoListAdapter);
        int index = mCommoListView.getFirstVisiblePosition();
        View v = mCommoListView.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();
        mCommoListView.setSelectionFromTop(index, top);



       /* //一会调试
        mCommoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                int pageSum = sp.getInt("pageSum"+getIntent().getStringExtra("post_id"),1);
                if( mCommoListView.getHeaderViewsCount()==1){
                    if(i!=0);
                    startActivityToPostReplyCommo(i - 1);
                }else if(mCommoListView.getHeaderViewsCount()==0){
                    startActivityToPostReplyCommo(i);
                }
            }
        });*/

        //跳转到详情页
        mPostCommoListAdapter.setOnMyClickLitener(new PostCommoListAdapter.OnMyItemClickLitener() {
            @Override
            public void onItemClick(View view, int i) {
                startActivityToPostReplyCommo(i);
            }
        });

        //点击头像跳转
        mPostCommoListAdapter.imageSetOnclick(new PostCommoListAdapter.ImageInterface() {
            @Override
            public void onclick(int position) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(PostDetailActivityy.this, LoginActivity.class);
                    return;
                } else {
                    if (UserUtils.getUserId().equals(mCommoAdapterList.get(position).getUid())) {
                        startActivity(new Intent(PostDetailActivityy.this, UserPageActivity.class));
                    } else {
                        Intent intent = new Intent(PostDetailActivityy.this, OtherPageActivity.class);
                        intent.putExtra("re_uid", mCommoAdapterList.get(position).getUid());
                        startActivity(intent);
                    }
                }
            }
        });



       //跳到大图
        mPostCommoListAdapter.setOnItemClickLitener(new PostCommoListAdapter.OnImageItemClickLitener() {
            @Override
            public void onItemClick(View view, int position_i, int position_j) {
                Intent intent = new Intent(PostDetailActivityy.this, PhotoViewPostCommoActivity.class);
                int indexOf = mCommoThumbImageList.indexOf(mCommoAdapterList.get(position_i)
                        .getThumbnail_image().get(position_j).getThumbnail_image());
                intent.putExtra("indexOf", indexOf);
                intent.putStringArrayListExtra("mCommoImageList", mCommoImageList);
                intent.putIntegerArrayListExtra("mCommoImageSizeList", mCommoImageSizeList);
                intent.putStringArrayListExtra("mCommoThumbImageList", mCommoThumbImageList);
                startActivity(intent);
            }
        });
        //获取广播传过来的数据
        updatePostCommoList();
    }
    private void startActivityToPostReplyCommo(int position) {
        if(position==-1){
            return;
        }
        if (mCommoAdapterList.size() == position)
            return;
/*        if (mCommoAdapterList.get(position).getFloor_num() != null) {*/
        try {
            Gson gson = new Gson();
            String json = gson.toJson(mCommoAdapterList.get(position), PostCommoBean.class);
            Intent intent = new Intent(PostDetailActivityy.this, PostReplyCommoActivity.class);
            intent.putExtra("item_list", json);
            intent.putExtra("position", position);
            PostDetailActivityy.this.startActivity(intent);
        }catch (Exception e) {
            e.printStackTrace();
        }
        }
   // }
    //*********************************************以下是赋值代码***************************************************

    private void setshipinValues(){
        if (mPostDetailBean.getRights_is().equals("2") && mPostDetailBean.getOperation_is() == 1) {
            mIv_review.setVisibility(View.VISIBLE);
        }
        //勋章
        if (mPostDetailBean.getMedal().equals("2")){
            iv_user_medal.setVisibility(View.VISIBLE);
        }else {
            iv_user_medal.setVisibility(View.GONE);
        }
        //版主
        if (mPostDetailBean.getUser_rights_is().equals("2")){
            mIv_banzhu.setVisibility(View.VISIBLE);
        }else {
            mIv_banzhu.setVisibility(View.GONE);
        }
        mCollectNum.setText(mPostDetailBean.getPosts_collect());
        //mCommNum.setText(mPostDetailBean.getPosts_forums());
        mJiaonangNum.setText(mPostDetailBean.getPosts_laud());
        //更新楼主
        mPostCommoListAdapter.setUid(mPostDetailBean.getUid());
        mPostCommoListAdapter.notifyDataSetChanged();
        //收藏状态
        if (mPostDetailBean.getCollect_status().equals("1")) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_action_like);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mCollectNum.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_action_love_empty);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mCollectNum.setCompoundDrawables(null, drawable, null, null);
        }
        if (mPostDetailBean.getLaud_status().equals("1")) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_zan_fill);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mJiaonangNum.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_zan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mJiaonangNum.setCompoundDrawables(null, drawable, null, null);
        }
        if (UIThreadUtils.isMainThread())
            Glide.with(mContext)
                    .load(mPostDetailBean.getHead_image())
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .transform(new GlideCircleTransform(PostDetailActivityy.this))
                    .into(mIvUserPhoto);

        //关注类
        if (mPostDetailBean.getRelation() == 0) {
            text_post_concern.setText("关注ta");
        } else if (mPostDetailBean.getRelation() == 1) {
            setBooder("已关注");
        } else if (mPostDetailBean.getRelation() == 2) {
            setBooder("已互粉");
        }
        mPostTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mPostDetailBean.getDateline() + "000")));
        mUserName.setText(mPostDetailBean.getNickname());
       text_vodeo_head.setText(mPostDetailBean.getSubject());

        //评论条数
        playNum.setText(mPostDetailBean.getPosts_forums()+"条");
        if(mPostDetailBean.getMessage().length()>0){
            video_content.setText(mPostDetailBean.getMessage());
        }else {
            video_content.setVisibility(View.GONE);
        }

        //播放次数
        peopleNum.setText(mPostDetailBean.getVod_number()+"人");
        mEditText.setFocusable(false);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(PostDetailActivityy.this, LoginActivity.class);
                    return;
                }
                mLinearLayout.setVisibility(View.GONE);
                mLinearLayout2.setVisibility(View.VISIBLE);
                //开启软键盘
                KeyboardUtils.showSoftInput(mEdit_text);
            }
        });
        //点击按钮发表评论
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendReply();
            }
        });
        mCollectNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(PostDetailActivityy.this, LoginActivity.class);
                    return;
                }
                getCollect(mPostDetailBean.getId());
                if (mPostDetailBean.getCollect_status().equals("1")) {
                    //更新列表界面收藏状态
                    Intent intent = new Intent();
                    switch (getIntent().getIntExtra("fragmentType", 0)) {
                        case 0:
                            break;
                        case 1:
                            intent.setAction("HotFragmentUpdateCollectStatus");
                            break;
                        case 2:
                            intent.setAction("BestFragmentUpdateCollectStatus");
                            break;
                        case 3:
                            intent.setAction("NewFragmentUpdateCollectStatus");
                            break;
                        case 8:
                            intent.setAction("SortPostUpdateCollectStatus");
                            break;
                        case 9:
                            intent.setAction("GodPostUpdateCollectStatus");
                            break;
                    }
                    intent.putExtra("cancelCollect", 0);
                    intent.putExtra("position", getIntent().getIntExtra("position", 0));
                    LocalBroadcastManager.getInstance(PostDetailActivityy.this)
                            .sendBroadcast(intent);

                    mCollectNum.setText(String.valueOf((parseInt(mPostDetailBean.getPosts_collect()) - 1)));
                    mPostDetailBean.setPosts_collect(String.valueOf((parseInt(mPostDetailBean.getPosts_collect()) - 1)));
                    mPostDetailBean.setCollect_status("0");
                    Toast.makeText(PostDetailActivityy.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_action_love_empty);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectNum.setCompoundDrawables(null, drawable, null, null);
                } else {
                    //更新列表界面收藏状态
                    Intent intent = new Intent();
                    switch (getIntent().getIntExtra("fragmentType", 0)) {
                        case 0:
                            break;
                        case 1:
                            intent.setAction("HotFragmentUpdateCollectStatus");
                            break;
                        case 2:
                            intent.setAction("BestFragmentUpdateCollectStatus");
                            break;
                        case 3:
                            intent.setAction("NewFragmentUpdateCollectStatus");
                            break;
                        case 8:
                            intent.setAction("SortPostUpdateCollectStatus");
                            break;
                        case 9:
                            intent.setAction("GodPostUpdateCollectStatus");
                            break;
                    }
                    intent.putExtra("cancelCollect", 1);
                    intent.putExtra("position", getIntent().getIntExtra("position", 0));
                    LocalBroadcastManager.getInstance(PostDetailActivityy.this)
                            .sendBroadcast(intent);
                    mCollectNum.setText(String.valueOf((parseInt(mPostDetailBean.getPosts_collect()) + 1)));
                    mPostDetailBean.setPosts_collect(String.valueOf((parseInt(mPostDetailBean.getPosts_collect()) + 1)));
                    mPostDetailBean.setCollect_status("1");
                    Toast.makeText(PostDetailActivityy.this, "收藏", Toast.LENGTH_SHORT).show();
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_action_like);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectNum.setCompoundDrawables(null, drawable, null, null);

                }

            }
        });

        mJiaonangNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(PostDetailActivityy.this, LoginActivity.class);
                    return;
                }
                getLaud(mPostDetailBean.getId(), mPostDetailBean.getUid());
                if (mPostDetailBean.getLaud_status().equals("1")) {
                    //更新列表界面点赞状态
                    Intent intent = new Intent();
                    switch (getIntent().getIntExtra("fragmentType", 0)) {
                        case 0:
                            break;
                        case 1:
                            intent.setAction("HotFragmentUpdateZanStatus");
                            break;
                        case 2:
                            intent.setAction("BestFragmentUpdateZanStatus");
                            break;
                        case 3:
                            intent.setAction("NewFragmentUpdateZanStatus");
                            break;
                        case 8:
                            intent.setAction("SortPostUpdateZanStatus");
                            break;
                        case 9:
                            intent.setAction("GodPostUpdateZanStatus");
                            break;
                    }
                    intent.putExtra("cancelZan", 0);
                    intent.putExtra("position", getIntent().getIntExtra("position", 0));
                    LocalBroadcastManager.getInstance(PostDetailActivityy.this)
                            .sendBroadcast(intent);
                    mJiaonangNum.setText(String.valueOf((parseInt(mPostDetailBean.getPosts_laud()) - 1)));
                    mPostDetailBean.setPosts_laud(String.valueOf((parseInt(mPostDetailBean.getPosts_laud()) - 1)));
                    mPostDetailBean.setLaud_status("0");
                    Toast.makeText(PostDetailActivityy.this, "取消点赞", Toast.LENGTH_SHORT).show();
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_zan);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mJiaonangNum.setCompoundDrawables(null, drawable, null, null);
                } else {
                    //更新列表界面点赞状态
                    Intent intent = new Intent();
                    switch (getIntent().getIntExtra("fragmentType", 0)) {
                        case 0:
                            break;
                        case 1:
                            intent.setAction("HotFragmentUpdateZanStatus");
                            break;
                        case 2:
                            intent.setAction("BestFragmentUpdateZanStatus");
                            break;
                        case 3:
                            intent.setAction("NewFragmentUpdateZanStatus");
                            break;
                        case 8:
                            intent.setAction("SortPostUpdateZanStatus");
                            break;
                        case 9:
                            intent.setAction("GodPostUpdateZanStatus");
                            break;
                    }
                    intent.putExtra("cancelZan", 1);
                    intent.putExtra("position", getIntent().getIntExtra("position", 0));
                    LocalBroadcastManager.getInstance(PostDetailActivityy.this)
                            .sendBroadcast(intent);
                    mJiaonangNum.setText(String.valueOf((parseInt(mPostDetailBean.getPosts_laud()) + 1)));
                    mPostDetailBean.setPosts_laud(String.valueOf((parseInt(mPostDetailBean.getPosts_laud()) + 1)));
                    mPostDetailBean.setLaud_status("1");
                    Toast.makeText(PostDetailActivityy.this, "点赞", Toast.LENGTH_SHORT).show();
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_zan_fill);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mJiaonangNum.setCompoundDrawables(null, drawable, null, null);
                }
            }
        });
    }
    private void setValues() {
        if (mPostDetailBean.getRights_is().equals("2") && mPostDetailBean.getOperation_is() == 1) {
            mIv_review.setVisibility(View.VISIBLE);
        }
        //勋章
        if (mPostDetailBean.getMedal().equals("2")){
            iv_user_medal.setVisibility(View.VISIBLE);
        }else {
            iv_user_medal.setVisibility(View.GONE);
        }
        //版主
        if (mPostDetailBean.getUser_rights_is().equals("2")){
            mIv_banzhu.setVisibility(View.VISIBLE);
        }else {
            mIv_banzhu.setVisibility(View.GONE);
        }
        //更新楼主
        mPostCommoListAdapter.setUid(mPostDetailBean.getUid());
        mPostCommoListAdapter.notifyDataSetChanged();
        //收藏状态
        if (mPostDetailBean.getCollect_status().equals("1")) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_action_like);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mCollectNum.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_action_love_empty);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mCollectNum.setCompoundDrawables(null, drawable, null, null);
        }
        if (mPostDetailBean.getLaud_status().equals("1")) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_zan_fill);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mJiaonangNum.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_zan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mJiaonangNum.setCompoundDrawables(null, drawable, null, null);
        }
        if (UIThreadUtils.isMainThread())
            Glide.with(mContext)
                    .load(mPostDetailBean.getHead_image())
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .transform(new GlideCircleTransform(PostDetailActivityy.this))
                    .into(mIvUserPhoto);

        //关注类
        if (mPostDetailBean.getRelation() == 0) {
            text_post_concern.setText("关注ta");
        } else if (mPostDetailBean.getRelation() == 1) {
            setBooder("已关注");
        } else if (mPostDetailBean.getRelation() == 2) {
            setBooder("已互粉");
        }

        mTvTitle.setText(SpanStringUtils.getEmotionContent(PostDetailActivityy.this,mTvTitle,mPostDetailBean.getSubject()));
        mToolbar.setTitle(mPostDetailBean.getSubject());
        mUserName.setText(mPostDetailBean.getNickname());
        mPostTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mPostDetailBean.getDateline() + "000")));
        mTvContent.setText(mPostDetailBean.getMessage());
        mCollectNum.setText(mPostDetailBean.getPosts_collect());
        //mCommNum.setText(mPostDetailBean.getPosts_forums());
        mJiaonangNum.setText(mPostDetailBean.getPosts_laud());

        //判断是否是gif图   帖子头部
        for (int i = 0; i < mPostDetailBean.getThumbnail_image().size(); i++) {
            final int finalI = i;

            //final ImageView imageView = new ImageView(PostDetailActivityy.this);
            final SubsamplingScaleImageView imageView =  new SubsamplingScaleImageView(PostDetailActivityy.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(ConvertUtils.dp2px(0), ConvertUtils.dp2px(5), ConvertUtils.dp2px(0), ConvertUtils.dp2px(5));
            imageView.setLayoutParams(lp);

            if(mPostDetailBean.getThumbnail_image().get(i).getThumbnail_image().indexOf("gif")!=-1){

                View view = LayoutInflater.from(PostDetailActivityy.this).inflate(R.layout.activity_gifimage, null);
                final ImageView photoView = (ImageView) view.findViewById(R.id.gif_imageview);
                Glide.with(mContext)
                        .load(mPostDetailBean.getThumbnail_image().get(i).getThumbnail_image())
                        .asBitmap()
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(photoView);

                mRootImage.addView(view);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PostDetailActivityy.this, PhotoViewPostDetailActivity.class);
                        intent.putExtra("position", finalI);
                        intent.putExtra("postDetailBean", mResponse);
                        startActivity(intent);
                    }
                });

            }else {

                Glide.with(mContext)
                        .load(mPostDetailBean.getThumbnail_image().get(i).getThumbnail_image())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                //imageView.setImageBitmap(resource);
                                imageView.setImage(ImageSource.bitmap(resource));
                            }
                        });


                //点击查看大图
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PostDetailActivityy.this, PhotoViewPostDetailActivity.class);
                        intent.putExtra("position", finalI);
                        intent.putExtra("postDetailBean", mResponse);
                        startActivity(intent);
                    }
                });

                mRootImage.addView(imageView);
            }

        }



        mEditText.setFocusable(false);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(PostDetailActivityy.this, LoginActivity.class);
                    return;
                }
                mLinearLayout.setVisibility(View.GONE);
                mLinearLayout2.setVisibility(View.VISIBLE);
                //开启软键盘
                KeyboardUtils.showSoftInput(mEdit_text);
            }
        });

        //点击按钮发表评论
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReply();
            }
        });
        mCollectNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(PostDetailActivityy.this, LoginActivity.class);
                    return;
                }
                getCollect(mPostDetailBean.getId());
                if (mPostDetailBean.getCollect_status().equals("1")) {
                    //更新列表界面收藏状态
                    Intent intent = new Intent();
                    switch (getIntent().getIntExtra("fragmentType", 0)) {
                        case 0:
                            break;
                        case 1:
                            intent.setAction("HotFragmentUpdateCollectStatus");
                            break;
                        case 2:
                            intent.setAction("BestFragmentUpdateCollectStatus");
                            break;
                        case 3:
                            intent.setAction("NewFragmentUpdateCollectStatus");
                            break;
                        case 8:
                            intent.setAction("SortPostUpdateCollectStatus");
                            break;
                        case 9:
                            intent.setAction("GodPostUpdateCollectStatus");
                            break;
                    }
                    intent.putExtra("cancelCollect", 0);
                    intent.putExtra("position", getIntent().getIntExtra("position", 0));
                    LocalBroadcastManager.getInstance(PostDetailActivityy.this)
                            .sendBroadcast(intent);

                    mCollectNum.setText(String.valueOf((parseInt(mPostDetailBean.getPosts_collect()) - 1)));
                    mPostDetailBean.setPosts_collect(String.valueOf((parseInt(mPostDetailBean.getPosts_collect()) - 1)));
                    mPostDetailBean.setCollect_status("0");
                    Toast.makeText(PostDetailActivityy.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_action_love_empty);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectNum.setCompoundDrawables(null, drawable, null, null);
                } else {
                    //更新列表界面收藏状态
                    Intent intent = new Intent();
                    switch (getIntent().getIntExtra("fragmentType", 0)) {
                        case 0:
                            break;
                        case 1:
                            intent.setAction("HotFragmentUpdateCollectStatus");
                            break;
                        case 2:
                            intent.setAction("BestFragmentUpdateCollectStatus");
                            break;
                        case 3:
                            intent.setAction("NewFragmentUpdateCollectStatus");
                            break;
                        case 8:
                            intent.setAction("SortPostUpdateCollectStatus");
                            break;
                        case 9:
                            intent.setAction("GodPostUpdateCollectStatus");
                            break;
                    }
                    intent.putExtra("cancelCollect", 1);
                    intent.putExtra("position", getIntent().getIntExtra("position", 0));
                    LocalBroadcastManager.getInstance(PostDetailActivityy.this)
                            .sendBroadcast(intent);


                    mCollectNum.setText(String.valueOf((parseInt(mPostDetailBean.getPosts_collect()) + 1)));
                    mPostDetailBean.setPosts_collect(String.valueOf((parseInt(mPostDetailBean.getPosts_collect()) + 1)));
                    mPostDetailBean.setCollect_status("1");
                    Toast.makeText(PostDetailActivityy.this, "收藏", Toast.LENGTH_SHORT).show();
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_action_like);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectNum.setCompoundDrawables(null, drawable, null, null);
                }

            }
        });

        mJiaonangNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(PostDetailActivityy.this, LoginActivity.class);
                    return;
                }
                getLaud(mPostDetailBean.getId(), mPostDetailBean.getUid());
                if (mPostDetailBean.getLaud_status().equals("1")) {
                    //更新列表界面点赞状态
                    Intent intent = new Intent();
                    switch (getIntent().getIntExtra("fragmentType", 0)) {
                        case 0:
                            break;
                        case 1:
                            intent.setAction("HotFragmentUpdateZanStatus");
                            break;
                        case 2:
                            intent.setAction("BestFragmentUpdateZanStatus");
                            break;
                        case 3:
                            intent.setAction("NewFragmentUpdateZanStatus");
                            break;
                        case 8:
                            intent.setAction("SortPostUpdateZanStatus");
                            break;
                        case 9:
                            intent.setAction("GodPostUpdateZanStatus");
                            break;
                    }
                    intent.putExtra("cancelZan", 0);
                    intent.putExtra("position", getIntent().getIntExtra("position", 0));
                    LocalBroadcastManager.getInstance(PostDetailActivityy.this)
                            .sendBroadcast(intent);
                    mJiaonangNum.setText(String.valueOf((parseInt(mPostDetailBean.getPosts_laud()) - 1)));
                    mPostDetailBean.setPosts_laud(String.valueOf((parseInt(mPostDetailBean.getPosts_laud()) - 1)));
                    mPostDetailBean.setLaud_status("0");
                    Toast.makeText(PostDetailActivityy.this, "取消点赞", Toast.LENGTH_SHORT).show();
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_zan);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mJiaonangNum.setCompoundDrawables(null, drawable, null, null);
                } else {
                    //更新列表界面点赞状态
                    Intent intent = new Intent();
                    switch (getIntent().getIntExtra("fragmentType", 0)) {
                        case 0:
                            break;
                        case 1:
                            intent.setAction("HotFragmentUpdateZanStatus");
                            break;
                        case 2:
                            intent.setAction("BestFragmentUpdateZanStatus");
                            break;
                        case 3:
                            intent.setAction("NewFragmentUpdateZanStatus");
                            break;
                        case 8:
                            intent.setAction("SortPostUpdateZanStatus");
                            break;
                        case 9:
                            intent.setAction("GodPostUpdateZanStatus");
                            break;
                    }
                    intent.putExtra("cancelZan", 1);
                    intent.putExtra("position", getIntent().getIntExtra("position", 0));
                    LocalBroadcastManager.getInstance(PostDetailActivityy.this)
                            .sendBroadcast(intent);
                    mJiaonangNum.setText(String.valueOf((parseInt(mPostDetailBean.getPosts_laud()) + 1)));
                    mPostDetailBean.setPosts_laud(String.valueOf((parseInt(mPostDetailBean.getPosts_laud()) + 1)));
                    mPostDetailBean.setLaud_status("1");
                    Toast.makeText(PostDetailActivityy.this, "点赞", Toast.LENGTH_SHORT).show();
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_zan_fill);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mJiaonangNum.setCompoundDrawables(null, drawable, null, null);
                }
            }
        });
    }

    /**12
     * 点赞
     *
     * @param tid
     */
    public void getLaud(String tid, String re_uid) {
        Log.e("re_uid", "getLaud: " + re_uid);
        OkHttpUtils.post()//
                .addParams("tid", tid)
                .addParams("uid", UserUtils.getUserId())
                .addParams("re_uid", re_uid)
                .url(XingYuInterface.GET_LAUD)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                    }
                });

    }
    /**
     * 收藏
     *
     * @param tid
     */
    public void getCollect(final String tid) {
        OkHttpUtils.post()//
                .addParams("tid", tid)
                .addParams("uid", UserUtils.getUserId())
                .url(XingYuInterface.GET_COLLECT)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                    }
                });
    }
    /**
     * 回帖
     */
    private void sendReply() {
        if (StringUtils.isEmpty(mEdit_text.getText().toString().trim()) && mImageList.size() == 0) {
            Toast.makeText(this, "评论内容为空,至少一张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        //关闭键盘
        //关闭键盘
        KeyboardUtils.hideSoftInput(this);
        mDialog = new CustomDialog(PostDetailActivityy.this, "正在回帖中...");
        mDialog.showDialog();
        String s1 = StringUtils.isEmpty(mEdit_text.getText().toString().trim()) == true ? "" : mEdit_text.getText().toString().trim();
        if(idListBeen.size()!=0){
            String replies_content = s1.trim();
            if(replies_content.trim()!=null||replies_content.length()!=0){
                String[] split = replies_content.split(" ");
                for (int j = 0; j <split.length; j++) {
                    if(split[j].trim().length()!=0){
                        String substring = split[j].substring(0,1);
//                      存在
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
        }
        JSONArray array = new JSONArray(list_map);
        Map<String, String> params = new HashMap<String, String>();
        params.put("pid", "0");
        params.put("uid", UserUtils.getUserId());
        params.put("tid", mPostDetailBean.getId());
        params.put("re_uid", mPostDetailBean.getUid());
        params.put("replies_content", StringUtils.isEmpty(mEdit_text.getText().toString().trim()) == true ? "" : mEdit_text.getText().toString().trim());
        params.put("relation", array.toString());
        //隐藏键盘
        mEmotion_layout.setVisibility(View.GONE);
        mExtend_layout.setVisibility(View.GONE);
        PostFormBuilder post = OkHttpUtils.post();
        for (int i = 0; i < mImageList.size(); i++) {
            File file = new File(mImageList.get(i));
            if (file.exists()) {
                //File file1 = new File(getExternalCacheDir() + "/tempCompress" + i + ".jpg");
                //NativeUtil.compressBitmap(mImageList.get(i), file1.getAbsolutePath());
                //以上是压缩代码
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                String type = options.outMimeType;

                if (type.equals("image/webp")) {
                    // FileUtils.saveFile(FileUtils.imageSavePath+"/tempCompress" ,i + ".jpg",ImageUtils.getBitmap(file));
                    ImageUtils.save(ImageUtils.getBitmap(file), FileUtils.imageSavePath + "/tempCompress" + i + ".jpg", Bitmap.CompressFormat.JPEG);
                    String s = "posts_image";
                    post.addFile(s + i, file.getName(), new File(FileUtils.imageSavePath + "/tempCompress" + i + ".jpg"));
                } else {
                    String s = "posts_image";
                    post.addFile(s + i, file.getName(), file);
                }
            }
        }



/*        for (int i = 0; i < mImageList.size(); i++) {
            File file = new File(mImageList.get(i));
            if (file.exists()) {
                File file1 = new File(getExternalCacheDir() + "tempCompress" + i + ".jpg");
                NativeUtil.compressBitmap(mImageList.get(i), file1.getAbsolutePath());
                String s = "replies_image";
                post.addFile(s + i, file.getName(), file1);
            }
        }*/


        post.url(XingYuInterface.REPLIES)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mDialog.dismissDialog();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        list_map.clear();
                        mDialog.dismissDialog();
                        mCommoAdapterList.clear();
                        mImageList.clear();
                        if(pageNUM%20==0){
                            PAGENUMUP = pageNUM/20;
                            PAGENUMDOWN=pageNUM/20;
                        }else if(pageNUM%20!=0){
                            PAGENUMUP = pageNUM/20+1;
                            PAGENUMDOWN=pageNUM/20+1;
                        }
                        initCommoData(PAGENUMUP,4);
                        select=2;
                        if(pageNUM>20){
                            mCommoListView.removeHeaderView(hView);
                            //  headerView.setVisibility(View.INVISIBLE);
                        }else if(pageNUM<=20){
                            if(mCommoListView.getHeaderViewsCount()==0){
                                mCommoListView.addHeaderView(hView);
                                //   headerView.setVisibility(View.VISIBLE);
                            }
                        }
                        mImageAdapter.notifyDataSetChanged();
                        mPostCommoListAdapter.notifyDataSetChanged();
                        //待优化
                        mEdit_text.setText("");
                        // mCommoListView.isStackFromBottom();
                        // mCommoListView.scrollTo(0,mCommoListView.getBottom());
                    }
                });
    }

    //**********************************************以下是回复图片设置代码****************************************************


    private class ImageAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(mImageList.size()==0){
                View layout = LayoutInflater.from(PostDetailActivityy.this).inflate(R.layout.item_post_commo_image, parent, false);
                return new ItemViewHolder(layout);
            }else {
                View layout = View.inflate(PostDetailActivityy.this,R.layout.item_post_commo_rela,null);
                return new ItemViewHolder(layout);
            }

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == TYPE_FOOTER) {
                ((ItemViewHolder) holder).mClosePic.setVisibility(View.GONE);
                ((ItemViewHolder) holder).post_aite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PostDetailActivityy.this, PersonActivity.class);
                        startActivityForResult(intent, CODE_PERSON);
                    }
                });
                ((ItemViewHolder) holder).mPostImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mImageList.size() >= 5) {
                            Toast.makeText(PostDetailActivityy.this, "只能发布五张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //第一步调用第三方框架
                            /*      MultiImageSelector.create()
                                .showCamera(true)
                                .count(5)
                                .multi()
                                .start(PostDetailActivityy.this, REQUEST_IMAGE);*/

                        ImageConfig imageConfig
                                = new ImageConfig.Builder(
                                // GlideLoader 可用自己用的缓存库
                                new GlideLoader())
                                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                                .steepToolBarColor(getResources().getColor(R.color.black))
                                // 标题的背景颜色 （默认黑色）
                                .titleBgColor(getResources().getColor(R.color.black))
                                // 提交按钮字体的颜色  （默认白色）
                                .titleSubmitTextColor(getResources().getColor(R.color.white))
                                // 标题颜色 （默认白色）
                                .titleTextColor(getResources().getColor(R.color.white))
                                // 开启多选   （默认为多选）  (单选 为 singleSelect)
                                //                         .singleSelect()
                                .crop()
                                // 多选时的最大数量   （默认 9 张）
                                .mutiSelectMaxSize(5)
                                // 已选择的图片路径
                                .pathList(mImageList)
                                // 拍照后存放的图片路径（默认 /temp/picture）
                                .filePath("/ImageSelector/Pictures")
                                // 开启拍照功能 （默认开启）
                                .showCamera()
                                .requestCode(REQUEST_CODE)
                                .build();

                        ImageSelector.open(PostDetailActivityy.this, imageConfig);//开启图片选择器
                    }
                });
            }
            if (holder instanceof ItemViewHolder) {
                if (mImageList.size() != 0) {
                    if (getItemViewType(position) != TYPE_FOOTER) {
                        Glide.with(PostDetailActivityy.this)
                                .load(mImageList.get(position))
                                .thumbnail(0.1f)
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .into(((ItemViewHolder) holder).mPostImage);
                        ((ItemViewHolder) holder).mClosePic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mImageList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
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
            return mImageList.size() == 0 ? 1 : mImageList.size() + 1;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private ImageView mClosePic;
            private ImageView mPostImage;
            private ImageView post_aite;
            public ItemViewHolder(View itemView) {
                super(itemView);
                mClosePic = (ImageView) itemView.findViewById(R.id.iv_close);
                mPostImage = (ImageView) itemView.findViewById(R.id.iv_post_image);
                post_aite= (ImageView) itemView.findViewById(R.id.post_aite);

            }
        }
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        //第二部添加回传过来的图片路径
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                mImageList.clear();
                mImageList.addAll(pathList);
                mImageAdapter.notifyDataSetChanged();
            }
        }

        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

        //选择@人时回传过来的值
        switch (requestCode) {
            case CODE_PERSON:
                //获取来的id和name集合
                ArrayList<String> tmpCidStr =  data.getStringArrayListExtra(KEY_CID);
                ArrayList<String> tmpNameStr = data.getStringArrayListExtra(KEY_NAME);
                ArrayList<String> tmpName= data.getStringArrayListExtra("KEY_");
                List<String> list_edi=new ArrayList<>();
                Collections.reverse(tmpNameStr);
                if (tmpCidStr != null && tmpCidStr.size() > 0) {
                    for (int i = 0; i < tmpCidStr.size(); i++) {
                        if (tmpName.size() > i) {
                            cidNameMap.put(tmpName.get(i),tmpCidStr.get(i) );
                        }
                    }
                }
/*
                   String replies_content = edittext.getText().toString();
                   if(replies_content.trim()!=null||replies_content.length()!=0) {
                       String[] split = replies_content.split(" ");
                       for (int j = 0; j < split.length; j++) {
                           if (split[j].trim().length() != 0) {
                               String substring = split[j].substring(0, 1);
//                      存在
                               if (substring.equals("@") || substring.equals("＠")) {
                                   list_edi.add(split[j]);
                               }
                           }
                       }
                   }
                   tmpNameStr.removeAll(list_edi);*/
                if(idListBeen.size()>0){
                    for (int i = 0; i < tmpNameStr.size(); i++) {
                        IdListBean idListBean = new IdListBean();
                        idListBean.setId(tmpCidStr.get(i));
                        idListBean.setName(tmpNameStr.get(i));
                        idListBeen.add(idListBean);
                    }
                    int curIndexx = mEdit_text.getSelectionStart();
                    for(String s:tmpNameStr){
                        mEdit_text.getText().insert(curIndexx,s);
                        nameStr=s+nameStr;
                    }
                    SpannableString ss = new SpannableString(SpanStringUtils.getEmotionpost(PostDetailActivityy.this,mEdit_text,mEdit_text.getText().toString()));
                    mEdit_text.setText(ss);
                    if (curIndexx >= 1) {

                        mEdit_text.getText().replace(curIndexx-1, curIndexx, "");
                    }
                    mEdit_text.setSelection(mEdit_text.getText().length());
                    //  setAtImageSpan(nameStr);
                    //第二次
                }else {
                    Collections.reverse(tmpNameStr);
                    for (int i = 0; i < tmpNameStr.size(); i++) {
                        IdListBean idListBean = new IdListBean();
                        idListBean.setId(tmpCidStr.get(i));
                        idListBean.setName(tmpNameStr.get(i));
                        idListBeen.add(idListBean);
                    }
                    int curIndex = mEdit_text.getSelectionStart();
                    for(String s:tmpNameStr){
                        mEdit_text.getText().insert(curIndex,s);
                        nameStr=s+nameStr;
                    }
                    SpannableString ss = new SpannableString(SpanStringUtils.getEmotionpost(PostDetailActivityy.this,mEdit_text,mEdit_text.getText().toString()));
                    //  setAtImageSpan(nameStr);
                    //第二次
                    mEdit_text.setText(ss);
                    if (curIndex >= 1) {
                        mEdit_text.getText().replace(curIndex-1, curIndex, "");
                    }
                    mEdit_text.setSelection(mEdit_text.getText().length());
                }
                break;
        }
    }

    private void getAllPics(List<PostCommoBean> mCommoList) {
        for (int i = 0; i < mCommoList.size(); i++) {
            if (mCommoList.get(i).getThumbnail_image() != null && mCommoList.get(i).getThumbnail_image().size() != 0) {
                for (int j = 0; j < mCommoList.get(i).getThumbnail_image().size(); j++) {
                    mCommoImageList.add(mCommoList.get(i).getImgarr().get(j).getPosts_image());
                    mCommoImageSizeList.add(mCommoList.get(i).getImgarr().get(j).getPosts_image_size());
                    mCommoThumbImageList.add(mCommoList.get(i).getThumbnail_image().get(j).getThumbnail_image());
                }
            }
        }
    }

    public void setBooder(String text) {
        text_post_concern.setTextColor(android.graphics.Color.parseColor("#7A7A7A"));
        Resources resources = PostDetailActivityy.this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.join);
        text_post_concern.setBackgroundDrawable(drawable);
        text_post_concern.setText(text);
    }

    public void setBooderPink() {
        text_post_concern.setTextColor(android.graphics.Color.parseColor("#ffffff"));
        Resources resources = PostDetailActivityy.this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.jion_style);
        text_post_concern.setBackgroundDrawable(drawable);
    }

    public void initConcern(final String relation) {
        OkHttpUtils.post()
                .addParams("uid", UserUtils.getUserId())
                .addParams("re_uid", mPostDetailBean.getUid())
                .addParams("relation", relation)
                .addParams("type", "1")
                .url(XingYuInterface.GET_OTHERCONCERN)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        if (relation.equals("1") || relation.equals("2")) {
                            initData();
                            setBooderPink();
                        } else if (relation.equals("0")) {
                            initData();
                            text_post_concern.setTextColor(android.graphics.Color.parseColor("#7A7A7A"));
                            Resources resources = PostDetailActivityy.this.getResources();
                            Drawable drawable = resources.getDrawable(R.drawable.join);
                            text_post_concern.setBackgroundDrawable(drawable);
                        }
                    }
                });
    }

    public void inputEditext(int  page){
        i_select = page/20+1;
        mCommoAdapterList.clear();
        mImageList.clear();
        if(i_select >1){
            mCommoListView.removeHeaderView(hView);
        }else if(i_select==1){
            if(mCommoListView.getHeaderViewsCount()==1){
            }else {
                mCommoListView.addHeaderView(hView);
            }
        }
        initCommoData(i_select,4);
        PAGENUMDOWN= i_select;
        PAGENUMUP= i_select;
        select=1;
        KeyboardUtils.hideSoftInput(PostDetailActivityy.this);
    }


    public void jumpBuild(){
        if(post_editText.getText().length()==0){
            ediNum=postBEan.count_pages;
            inputEditext(postBEan.count_pages);
        }else {
            if(Integer.parseInt(post_editText.getText().toString())>=postBEan.count_pages){
                ediNum = postBEan.count_pages;
            }else if(Integer.parseInt(post_editText.getText().toString())<postBEan.count_pages){
                ediNum = Integer.parseInt(post_editText.getText().toString());
            }
            inputEditext(ediNum);
        }
    }
    private void goAt() {
        StringBuffer tmp = new StringBuffer();
        // 把选中人的id已空格分隔，拼接成字符串
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra(PersonActivity.KEY_SELECTED, tmp.toString());
        startActivityForResult(intent, CODE_PERSON);
    }


    private void setAtImageSpan(String nameStr) {
        String content = String.valueOf(mEdit_text.getText());
        if (content.endsWith("@") || content.endsWith("＠")) {
            content = content.substring(0, content.length() - 1);
        }
        String tmp = content;
        SpannableString ss = new SpannableString(SpanStringUtils.getEmotionContentEdi(PostDetailActivityy.this,mEdit_text,tmp));
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
        mEdit_text.setTextKeepState(ss);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {                //转为竖屏了。
            //显示状态栏
            //this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            surfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            ViewGroup.LayoutParams rrrLayoutParams = rrr.getLayoutParams();
          //  rrrLayoutParams.height =(int) (ScreenUtils.getWight(this) * 9.0f / 16);
            rrrLayoutParams.height = (int) (ScreenUtils.getWidth(this) * 9.0f / 16);
            rrrLayoutParams.width =  ViewGroup.LayoutParams.MATCH_PARENT;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {                //转到横屏了。
            //隐藏状态栏
         // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            surfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
            //设置view的布局，宽高
/*            ViewGroup.LayoutParams surfaceViewLayoutParams = surfaceView.getLayoutParams();
            surfaceViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            surfaceViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;*/
            ViewGroup.LayoutParams rrrLayoutParams = rrr.getLayoutParams();
            rrrLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            rrrLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(PostDetailActivityy.this).onStop();
        DCAgent.pause(this);
        DCPage.onExit("PostDetailActivity");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mAliyunVodPlayerView != null) {
            boolean handler = mAliyunVodPlayerView.onKeyDown(keyCode, event);
            if (!handler) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    private void savePlayerState() {
    mPlayerState = aliyunVodPlayer.getPlayerState();
        if (aliyunVodPlayer.isPlaying()) {
            //然后再暂停播放器
            aliyunVodPlayer.pause();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        //onStop中记录下来的状态，在这里恢复使用
       savePlayerState();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
       mPostCommoListAdapter.notifyDataSetChanged();
       // mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
        if (mBroadcastReceiver != null) {
            try{
                unregisterReceiver(mBroadcastReceiver);
            }catch(IllegalArgumentException e){
                if (e.getMessage().contains("Receiver not registered")) {

                    // Ignore this exception. This is exactly what is desired
                } else {
                    // unexpected, re-throw
                    throw e;
                }
            }
        }
        View v = mCommoListView.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();
        if (mCommoListView.getCount()== 0) {

        } else {
            try {
                int index = mCommoListView.getFirstVisiblePosition();
                int pageSum = 1;
                if (mCommoAdapterList.get(index).getFloor_num() != null || parseInt(mCommoAdapterList.get(index).getFloor_num()) != 0) {
                    if (parseInt(mCommoAdapterList.get(index).getFloor_num()) / 20 >= 1) {
                        pageSum = parseInt(mCommoAdapterList.get(index).getFloor_num()) / 20 + 1;
                    }
                }
                spp = getSharedPreferences("user", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = spp.edit();
                edit.putInt("index" + getIntent().getStringExtra("post_id"), parseInt(mCommoAdapterList.get(index).getFloor_num()));
                edit.putInt("top" + getIntent().getStringExtra("post_id"), top);
                edit.putInt("pageSum" + getIntent().getStringExtra("post_id"), pageSum < 1 ? 1 : pageSum);
                //提交edit
                edit.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        aliyunVodPlayer.stop();
        aliyunVodPlayer.release();
        stopUpdateTimer();
        progressUpdateTimer = null;
  /*      if(vod_id!=null){
            mScreenStatusController.stopListen();
        }*/

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
   //   updatePlayerViewMode();
    }

    @Override
    protected void onResume () {
        super.onResume();
        playBtn.setImageResource(R.mipmap.video_start);
        playBtn.setTag(null);
        aliyunVodPlayer.pause();
        globalOnItemClickListener = GlobalOnItemClickManager.getInstance();
        globalOnItemClickListener.attachToEditText((EditText) findViewById(R.id.edit_text));
    }
    private void resumePlayerState() {
        if (mPlayerState == IAliyunVodPlayer.PlayerState.Paused) {
            aliyunVodPlayer.pause();
        } else if (mPlayerState == IAliyunVodPlayer.PlayerState.Started) {
            aliyunVodPlayer.start();
            playBtn.setImageResource(R.mipmap.video_start);
            playBtn.setTag(null);
        }
    }
    //设置视频资源
    private void setPlaySource(String path) {
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vodeo_background.setVisibility(View.GONE);
                if(progroess_Num==0){
                    progressBar_onloading.setVisibility(View.VISIBLE);
                    progroess_Num++;
                }
               // Toast.makeText(PostDetailActivityy.this,"开始请求播放",Toast.LENGTH_SHORT).show();
                if(video==1) {
                    mPlayerState = aliyunVodPlayer.getPlayerState();
                    if (mPlayerState == Idle || mPlayerState == Stopped) {
                        if (mVidSource != null) {
                            aliyunVodPlayer.prepareAsync(mVidSource);
                        } else if (mPlayAuth != null) {
                            aliyunVodPlayer.prepareAsync(mPlayAuth);
                        } else if (mLocalSource != null) {
                            aliyunVodPlayer.prepareAsync(mLocalSource);
                        }
                        mAutoPlay = true;
                    } else {
                        inSeek = false;
                        aliyunVodPlayer.start();
                    }
                    video++;
                }
                if(playBtn.getTag()==null) {
                    Message msg = new Message();
                    msg.what = video_fristgone;
                    handler.sendMessage(msg);
                    playBtn.setImageResource(R.mipmap.video_stop);
                    aliyunVodPlayer.resume();
                    playBtn.setTag("1");
                }else if(playBtn.getTag().equals("1")){
                    playBtn.setImageResource(R.mipmap.video_start);
                    playBtn.setTag(null);
                    aliyunVodPlayer.pause();
                    video_visit();
                }
               // handler.sendEmptyMessageDelayed(video_gone, 3000);
            }
        });
            AliyunPlayAuth.AliyunPlayAuthBuilder aliyunPlayAuthBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
            aliyunPlayAuthBuilder.setVid(vod_id);
            aliyunPlayAuthBuilder.setPlayAuth(path);
//            qualityList.put(IAliyunVodPlayer.QualityValue.QUALITY_FLUENT,"流畅");
//            qualityList.put(IAliyunVodPlayer.QualityValue.QUALITY_LOW,"标清");
//            qualityList.put(IAliyunVodPlayer.QualityValue.QUALITY_STAND,"高清");
//            qualityList.put(IAliyunVodPlayer.QualityValue.QUALITY_HIGH,"超清");
//            qualityList.put(IAliyunVodPlayer.QualityValue.QUALITY_2K,"2k");
//            qualityList.put(IAliyunVodPlayer.QualityValue.QUALITY_4K,"4k");
//            qualityList.put(IAliyunVodPlayer.QualityValue.QUALITY_ORIGINAL,"原画");
            aliyunPlayAuthBuilder.setQuality(IAliyunVodPlayer.QualityValue.QUALITY_STAND);
            //aliyunVodPlayer.setAuthInfo(aliyunPlayAuthBuilder.build());
            mPlayAuth = aliyunPlayAuthBuilder.build();



    }
    private void replay() {
        //replay有两种形式，
        //一种是保留播放器，在onstopped的回调函数内prepare
        //在surface不变的情况下，建议用这种
        if(true) {
            AliyunVodPlayer tmpVodPlayer = aliyunVodPlayer;
            if (tmpVodPlayer != null) {
                logStrs.add(format.format(new Date()) + " 请求停止播放");
                tmpVodPlayer.stop();
                if (mPlayAuth != null) {
                    refreshPlaySource();//如果是用的auth模式，注意过期时间。
                }
                replay = true;
            }
        }
        }

    private void refreshPlaySource()
    {
        mAuthinfo = null;
        new Thread(new Runnable() {

            //            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                mAuthinfo = PlayAuthUtil.getPlayAuth(PostDetailActivityy.this,mVid);
            }
        }).start();

        int tryCount = 0;
        while (mAuthinfo == null || mAuthinfo.isEmpty()) {
            try {
                Thread.sleep(1000);
                tryCount++;
                if(tryCount > 5) {
                //    Toast.makeText(PostDetailActivityy.this, "通过网络请求authinfo失败", Toast.LENGTH_LONG).show();
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        AliyunPlayAuth.AliyunPlayAuthBuilder aliyunPlayAuthBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
        aliyunPlayAuthBuilder.setVid(mVid);
        aliyunPlayAuthBuilder.setPlayAuth(mAuthinfo);
        aliyunPlayAuthBuilder.setQuality(aliyunVodPlayer.getCurrentQuality());
        //aliyunVodPlayer.setAuthInfo(aliyunPlayAuthBuilder.build());
        mPlayAuth = aliyunPlayAuthBuilder.build();
    }
   //无皮肤播放器
   private void initVodPlayer() {
       aliyunVodPlayer = new AliyunVodPlayer(this);
       aliyunVodPlayer.setVolume(100);
       String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save_cache";
       aliyunVodPlayer.setPlayingCache(true,sdDir,60 * 60 /*时长, s */,300 /*大小，MB*/);
       aliyunVodPlayer.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
           @Override
           public void onPrepared() {
                // Toast.makeText(PostDetailActivityy.this, "准备成功", Toast.LENGTH_SHORT).show();
               // 准备成功之后可以调用start方法开始播放
               inSeek = false;
               aliyunVodPlayer.start();
               showVideoProgressInfo();

           }
       });


       aliyunVodPlayer.setOnFirstFrameStartListener(new IAliyunVodPlayer.OnFirstFrameStartListener() {
           @Override
           public void onFirstFrameStart() {
               Map<String, String> debugInfo = aliyunVodPlayer.getAllDebugInfo();
               long createPts = 0;
               if(debugInfo.get("create_player") != null) {
                   String time = debugInfo.get("create_player");
                   createPts = (long)Double.parseDouble(time);
                   logStrs.add(format.format(new Date(createPts)) +" 播放创建成功");
               }
               if(debugInfo.get("open-url") != null) {
                   String time = debugInfo.get("open-url");
                   long openPts = (long)Double.parseDouble(time) + createPts;
                   logStrs.add(format.format(new Date(openPts)) +" url请求成功");
               }
               if(debugInfo.get("find-stream") != null) {
                   String time = debugInfo.get("find-stream");
                   VcPlayerLog.d(TAG + "lfj0914","find-Stream time =" + time + " , createpts = " + createPts);
                   long findPts = (long)Double.parseDouble(time) + createPts;
                   logStrs.add(format.format(new Date(findPts)) +" 请求流成功");
               }
               if(debugInfo.get("open-stream") != null) {
                   String time = debugInfo.get("open-stream");
                   VcPlayerLog.d(TAG + "lfj0914","open-Stream time =" + time + " , createpts = " + createPts);
                   long openPts = (long)Double.parseDouble(time) + createPts;
                   logStrs.add(format.format(new Date(openPts)) +" 开始传输码流");
               }
               logStrs.add(format.format(new Date()) +" 第一帧播放完成");
           }
       });
       aliyunVodPlayer.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
           @Override
           public void onError(int arg0, int arg1, String msg) {

               if(arg0 == AliyunErrorCode.ALIVC_ERR_INVALID_INPUTFILE.getCode()){
                   //当播放本地报错4003的时候，可能是文件地址不对，也有可能是没有权限。
                   //如果是没有权限导致的，就做一个权限的错误提示。其他还是正常提示：
                   int storagePermissionRet = ContextCompat.checkSelfPermission(PostDetailActivityy.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                   if(storagePermissionRet != PackageManager.PERMISSION_GRANTED){
                 //      Toast.makeText(PostDetailActivityy.this, "失败！！！！原因：无本地存储访问权限", Toast.LENGTH_SHORT).show();
                       return;
                   }
               }

          //  Toast.makeText(PostDetailActivityy.this, "失败！！！！原因：" + msg, Toast.LENGTH_SHORT).show();
           }
       });
       aliyunVodPlayer.setOnCompletionListener(new IAliyunVodPlayer.OnCompletionListener() {
           @Override
           public void onCompletion() {
               chongbo.setVisibility(View.VISIBLE);
               isCompleted = true;
               showVideoProgressInfo();
               stopUpdateTimer();
               replay_tag=2;

           }
       });
       aliyunVodPlayer.setOnSeekCompleteListener(new IAliyunVodPlayer.OnSeekCompleteListener() {
           @Override
           public void onSeekComplete() {
               logStrs.add(format.format(new Date()) + " seek结束");
               inSeek = false;
           }
       });


       aliyunVodPlayer.setOnStoppedListner(new IAliyunVodPlayer.OnStoppedListener() {
           @Override
           public void onStopped() {
               logStrs.add(format.format(new Date()) + "播放停止");
               if(replay) {
                   logStrs.add(format.format(new Date()) + " 准备请求码流");
                   if(mVidSource != null) {
                       aliyunVodPlayer.prepareAsync(mVidSource);
                   } else if(mPlayAuth != null) {
                       aliyunVodPlayer.prepareAsync(mPlayAuth);
                   } else if(mLocalSource != null) {
                       aliyunVodPlayer.prepareAsync(mLocalSource);
                   }
                   mAutoPlay = true;
               }
               replay = false;
           }
       });



       aliyunVodPlayer.setOnBufferingUpdateListener(new IAliyunVodPlayer.OnBufferingUpdateListener() {
           @Override
           public void onBufferingUpdate(int percent) {
               // updateBufferingProgress(percent);
           }
       });


       aliyunVodPlayer.setDisplay(surfaceView.getHolder());

       aliyunVodPlayer.setOnChangeQualityListener(new IAliyunVodPlayer.OnChangeQualityListener() {
           @Override
           public void onChangeQualitySuccess(String finalQuality) {
               Log.d(TAG, "切换清晰度成功");
               logStrs.add(format.format(new Date()) + " 切换清晰度成功");
               showVideoProgressInfo();
           }

           @Override
           public void onChangeQualityFail(int code, String msg) {
               Log.d(TAG, "切换清晰度失败。。。" + code + " ,  " + msg);
               logStrs.add(format.format(new Date()) + " 切换清晰度失败");
           }
       });



       aliyunVodPlayer.enableNativeLog();





   }

    private Handler progressUpdateTimer = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            showVideoProgressInfo();
        }
    };


    private void stopUpdateTimer() {
        progressUpdateTimer.removeMessages(0);
    }


    //缓冲长度
    private void updateBufferingProgress(int percent) {
        int duration = (int) aliyunVodPlayer.getDuration();
        int secondaryProgress = (int) (duration * percent * 1.0f / 100);
        Log.d("hahahha", "duration =" + duration + " , buffer percent = " +percent + " , secondaryProgress =" + secondaryProgress);
      //  progressBar.setSecondaryProgress(secondaryProgress);
    }



    private void showVideoProgressInfo() {
        if((aliyunVodPlayer.getPlayerState().equals(AliyunVodPlayer.PlayerState.Started)
                || aliyunVodPlayer.getPlayerState().equals(AliyunVodPlayer.PlayerState.Replay))
                && !inSeek) {
            int curPosition = (int) aliyunVodPlayer.getCurrentPosition();
            positionTxt.setText(Formatter.formatTime(curPosition));
            int duration = (int) aliyunVodPlayer.getDuration();
            durationTxt.setText(Formatter.formatTime(duration));
            Log.d(TAG, "lfj0918 duration = " + duration + " , curPosition = " + curPosition );
            progressBar.setMax(duration);
            progressBar.setProgress(curPosition);
        }
        startUpdateTimer();
    }

    private void startUpdateTimer() {
        progressUpdateTimer.removeMessages(0);
        progressUpdateTimer.sendEmptyMessageDelayed(0, 1000);

    }



}


