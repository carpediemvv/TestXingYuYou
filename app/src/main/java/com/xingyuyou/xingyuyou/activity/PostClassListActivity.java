package com.xingyuyou.xingyuyou.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.utils.StorageUtils;
import com.aliyun.demo.crop.AliyunVideoCrop;
import com.aliyun.demo.crop.MediaActivity;
import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.FlashType;
import com.aliyun.struct.snap.AliyunSnapVideoParam;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ChangeBitmap;
import com.xingyuyou.xingyuyou.Utils.EncryptUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.glide.Utils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.AOtherpagerAdapter;
import com.xingyuyou.xingyuyou.adapter.CommSortAdapter;
import com.xingyuyou.xingyuyou.bean.PostClassListHeadBean;
import com.xingyuyou.xingyuyou.bean.community.PostListBeanTest;
import com.xingyuyou.xingyuyou.bean.community.PostTopAndWellBean;
import com.xingyuyou.xingyuyou.bean.community.tabBean;
import com.xingyuyou.xingyuyou.fragment.ClassifyAllFragment;
import com.xingyuyou.xingyuyou.fragment.ClassifyNewFragment;
import com.xingyuyou.xingyuyou.fragment.ClassifyOtherFragment;
import com.xingyuyou.xingyuyou.fragment.ClassifyStrategyFragment;
import com.xingyuyou.xingyuyou.views.FinishProjectPopupWindows;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC;
import static com.xingyuyou.xingyuyou.R.id.tv_post_describe;
import static com.xingyuyou.xingyuyou.activity.VideoActivity.REQUEST_RECORD;

public class PostClassListActivity extends AppCompatActivity {

    // private RecyclerView mRecyclerView;
    private int PAGENUMBER = 1;
    private List<PostListBeanTest> mPostList = new ArrayList();
    private List<PostTopAndWellBean> mPostTopWellList = new ArrayList();
    private PostClassListHeadBean mPostClassListHeadBean = new PostClassListHeadBean();
    private List mPostAdapterList = new ArrayList();
    //  private PostListBEan postListBEan;
    boolean isLoading = false;
    private View.OnClickListener itemsOnClick;
    private FinishProjectPopupWindows mFinishProjectPopupWindow;
    private AlertDialog mAlertDialog;
    private VideoQuality videoQuality;
    private String imagePath;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==2){

                if (msg.obj.toString().contains("\"data\":null")) {
                    mOtherpagerAdapter = new AOtherpagerAdapter(fragmentManager, mTitles, fragmentArrayList);
                    viewPager_below_activity.setAdapter(mOtherpagerAdapter);
                   return;
                }
                String response = (String) msg.obj;
                Gson gson = new Gson();
                mTabBean = gson.fromJson(response, tabBean.class);
                for (int i = 0; i < mTabBean.getData().size(); i++) {
                    mTitles.add(1,mTabBean.getData().get(i).getMenu_name());
                    ClassifyOtherFragment classifyOtherFragment = new ClassifyOtherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("uid", getIntent().getStringExtra("list_id"));
                    bundle.putString("menu_id",mTabBean.getData().get(i).getId());
                    classifyOtherFragment.setArguments(bundle);
                    fragmentArrayList.add(1,classifyOtherFragment);
                }
                mOtherpagerAdapter = new AOtherpagerAdapter(fragmentManager, mTitles, fragmentArrayList);
                viewPager_below_activity.setAdapter(mOtherpagerAdapter);
                mOtherpagerAdapter.notifyDataSetChanged();
            }
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {
                    //Toast.makeText(PostClassListActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");

                }
                String response = (String) msg.obj;
                Gson gson = new Gson();
                //  postListBEan = gson.fromJson(response, PostListBEan.class);


                //第一个接口
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
    /*              JSONArray ja = jo.getJSONArray("data");
                    mPostList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostListBeanTest>>() {
                            }.getType());

                    //精品
                    ja = jo.getJSONArray("top_well");
                    gson = new Gson();
                    mPostTopWellList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostTopAndWellBean>>() {
                            }.getType());

                    if (CLEAR_DATA == true) {
                        mPostAdapterList.clear();
                        CLEAR_DATA = false;
                    }
                    if (PAGENUMBER == 1) {
                        mPostAdapterList.addAll(mPostTopWellList);
                    }
                    mPostAdapterList.addAll(mPostList);*/

                    JSONObject jb = jo.getJSONObject("forum_theme_info");
                    gson = new Gson();
                    mPostClassListHeadBean = gson.fromJson(jb.toString(),
                            new TypeToken<PostClassListHeadBean>() {
                            }.getType());
                    setHeadData(mPostClassListHeadBean);
      /*              if (CLEAR_DATA == true) {
                        mPostAdapterList.clear();
                        CLEAR_DATA = false;
                    }
                    if (PAGENUMBER == 1) {
                        mPostAdapterList.addAll(mPostClassListHeadBean);
                    }
                    mPostAdapterList.addAll(mPostList);*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (mCommHotAdapter != null)
                    mCommHotAdapter.notifyDataSetChanged();
            }
        }
    };
    private CommSortAdapter mCommHotAdapter;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    //  private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private Toolbar mToolbar;
    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mBr;
    private static boolean CLEAR_DATA = false;
    private TextView mTvPostNum;
    private TextView tv_class_name;
    private TextView tvPostDes;
    private TextView tv_post_content;
    String[] eff_dirs;
    String path;
    private ArrayList<Fragment> fragmentArrayList;
    private FragmentManager fragmentManager;
    ViewPager viewPager_below_activity;
    private ArrayList<String> mTitles = new ArrayList<>();
    private TabLayout mTabLayout;
    private AppBarLayout mAppBarLayout;
    private RelativeLayout header_sort_post_list;
    private TextView tv_title;
    private ImageView ivBg;
    private ImageView ivLabel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private tabBean mTabBean;
    private AOtherpagerAdapter mOtherpagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_class_list);
        initView();
        InitFragment();
        // initToolBar();
/*        Glide.with(getApplication())
                .load(getIntent().getStringExtra("getClass_head_image"))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(ivBg);
        Glide.with(getApplication())
                .load(getIntent().getStringExtra("getClass_virtual_image"))
                .transform(new GlideCircleTransform(PostClassListActivity.this))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(ivLabel);*/
        initAssetPath();
        // initSwipeRefreshLayout();
        initData(1);
        //updateStatus();
        //  updatePost();
        InitViewPager();
    }

    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {
        //让ViewPager缓存2个页面
        viewPager_below_activity.setOffscreenPageLimit(2);
        //设置默认打开第一页
        viewPager_below_activity.setCurrentItem(0);
    }


    //初始化fragment
    private void InitFragment() {
        mTitles.add("全部");
        mTitles.add("热门");
        mTitles.add("最新");
        fragmentArrayList = new ArrayList<Fragment>();
        ClassifyNewFragment classifynewfragment = new ClassifyNewFragment();
        ClassifyStrategyFragment classifystrategfragment = new ClassifyStrategyFragment();
        ClassifyAllFragment classifyallfragment = new ClassifyAllFragment();
        fragmentArrayList.add(classifyallfragment);
        fragmentArrayList.add(classifystrategfragment);
        fragmentArrayList.add(classifynewfragment);
        fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        tv_title.setText(getIntent().getStringExtra("posts_class_name"));
        bundle.putString("uid", getIntent().getStringExtra("list_id"));
        Log.e("fenqu", "InitFragment: " + getIntent().getStringExtra("list_id"));
        bundle.putString("attribute", getIntent().getStringExtra("attribute"));
        classifynewfragment.setArguments(bundle);
        classifystrategfragment.setArguments(bundle);
        classifyallfragment.setArguments(bundle);
       // mOtherpagerAdapter = new AOtherpagerAdapter(fragmentManager, mTitles, fragmentArrayList);
       // viewPager_below_activity.setAdapter(mOtherpagerAdapter);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // tv_title.setAlpha((appBarLayout.getTotalScrollRange()-Math.abs(verticalOffset*1.0f))/appBarLayout.getTotalScrollRange());
                ivLabel.setAlpha((appBarLayout.getTotalScrollRange() - Math.abs(verticalOffset * 1.0f)) / appBarLayout.getTotalScrollRange());
                mTvPostNum.setAlpha((appBarLayout.getTotalScrollRange() - Math.abs(verticalOffset * 1.0f)) / appBarLayout.getTotalScrollRange());
                tvPostDes.setAlpha((appBarLayout.getTotalScrollRange() - Math.abs(verticalOffset * 1.0f)) / appBarLayout.getTotalScrollRange());
                tv_class_name.setAlpha((appBarLayout.getTotalScrollRange() - Math.abs(verticalOffset * 1.0f)) / appBarLayout.getTotalScrollRange());
                tv_post_content.setAlpha((appBarLayout.getTotalScrollRange() - Math.abs(verticalOffset * 1.0f)) / appBarLayout.getTotalScrollRange());
                //初始verticalOffset为0，不能参与计算。
        /*        if (verticalOffset <= -header_sort_post_list.getHeight() / 2) {
                   // mToolbar.setTitle(getIntent().getStringExtra("posts_class_name"));
                    tv_title.setText(getIntent().getStringExtra("posts_class_name"));
                    //ivBg.setVisibility(View.INVISIBLE);
                    ivLabel.setAlpha(verticalOffset*1.0f);
                    mTvPostNum.setAlpha(verticalOffset*1.0f);
                    tvPostDes.setAlpha(verticalOffset*1.0f);
                    tv_class_name.setAlpha(verticalOffset*1.0f);
                    tv_post_content.setAlpha(verticalOffset*1.0f);*/
                //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
/*                    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                   collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.transparent));*/
                /*else {
                    tv_title.setText("");
                   // ivBg.setVisibility(View.VISIBLE);
                    ivLabel.setVisibility(View.VISIBLE);
                    mTvPostNum.setVisibility(View.VISIBLE);
                    tvPostDes.setVisibility(View.VISIBLE);
                    tv_post_content.setVisibility(View.VISIBLE);
                    tv_class_name.setVisibility(View.VISIBLE);
                }*/
            }
        });
    }

/*  ivBg = (ImageView) findViewById(R.id.iv_bg);
    ivLabel = (ImageView) findViewById(R.id.iv_class_label);
    mTvPostNum = (TextView)findViewById(R.id.tv_post_num);
    tvPostDes = (TextView) findViewById(tv_post_describe);
    tv_post_content = (TextView)findViewById(R.id.tv_post_content);
    tv_class_name = (TextView) findViewById(R.id.tv_class_name);*/
/*    private void updatePost() {
        mLocalBroadcastManager = LocalBroadcastManager
                .getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("updateFragment");
        mBr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PAGENUMBER = 1;
                                CLEAR_DATA = true;
                                initData(PAGENUMBER);
                                mRefreshLayout.setRefreshing(false);
                            }
                        }, 200);
                    }
                });
            }

        };
        mLocalBroadcastManager.registerReceiver(mBr, intentFilter);
    }*/

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getIntent().getStringExtra("posts_class_name"));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        tv_title = (TextView) findViewById(R.id.tv_title);
        header_sort_post_list = (RelativeLayout) findViewById(R.id.header_sort_post_list);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        mTabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.colorPrimary));
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager_below_activity = (ViewPager) findViewById(R.id.viewPager_below_activity);
        mTabLayout.setupWithViewPager(viewPager_below_activity);
        ImageView iv_toolbar_back = (ImageView) findViewById(R.id.iv_toolbar_back);
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //发帖
        final ImageView floatingActionButton = (ImageView) findViewById(R.id.fab_add_comment);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFinishProjectPopupWindow = new FinishProjectPopupWindows(PostClassListActivity.this, floatingActionButton, itemsOnClick);
                mFinishProjectPopupWindow.showAtLocation(PostClassListActivity.this.findViewById(R.id.fab_add_comment),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                floatingActionButton.setVisibility(View.GONE);
            }
        });
        itemsOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinishProjectPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.popupwindow_Button_longvideo:
                        if (UserUtils.logined()) {
                            startActivity(new Intent(PostClassListActivity.this, VideoLongActivity.class));
                        } else {
                            IntentUtils.startActivity(PostClassListActivity.this, LoginActivity.class);
                        }
                        break;
                    case R.id.popupwindow_Button_saveProject:
                        if (UserUtils.logined()) {
                            IntentUtils.startActivity(PostClassListActivity.this, PostingActivity.class);
                        } else {
                            IntentUtils.startActivity(PostClassListActivity.this, LoginActivity.class);
                        }
                        break;
                    case R.id.popupwindow_Button_abandonProject:
                        if (UserUtils.logined()) {
                            AliyunVideoRecorder.startRecordForResult(PostClassListActivity.this, REQUEST_RECORD, aliyunSnap());
                        } else {
                            IntentUtils.startActivity(PostClassListActivity.this, LoginActivity.class);
                        }
                        break;
                }
            }
        };
/*        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
       mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);*/
        mCommHotAdapter = new CommSortAdapter(this, mPostAdapterList);

        /*mCommHotAdapter.setOnItemLongClickLitener(new CommSortAdapter.OnItemLongClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostClassListActivity.this);
                View view1 = LayoutInflater.from(PostClassListActivity.this).inflate(R.layout.dialog_delete_post, null);
                TextView tv_delete = (TextView) view1.findViewById(R.id.tv_delete);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog.dismiss();
                        deletePost(((PostListBeanTest) mPostAdapterList.get(position - 1)).getId());
                        mPostAdapterList.remove(position - 1);
                        Toast.makeText(PostClassListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        //更新UI
                        if (mCommHotAdapter != null)
                            mCommHotAdapter.notifyDataSetChanged();
                    }
                });
                TextView tv_cancel = (TextView) view1.findViewById(R.id.tv_cancel);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog.dismiss();
                    }
                });
                builder.setView(view1);
                mAlertDialog = builder.create();
                mAlertDialog.show();
            }
        });*/


        //底部布局
        View loadingData = View.inflate(this, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        //头布局
        // View headerView = View.inflate(this, R.layout.header_sort_post_list, null);
        View headerVieww = View.inflate(this, R.layout.header_sort_post_listt, null);
        ivBg = (ImageView) findViewById(R.id.iv_bg);
        ivLabel = (ImageView) findViewById(R.id.iv_class_label);
        mTvPostNum = (TextView) findViewById(R.id.tv_post_num);
        tvPostDes = (TextView) findViewById(tv_post_describe);
        tv_post_content = (TextView) findViewById(R.id.tv_post_content);
        tv_class_name = (TextView) findViewById(R.id.tv_class_name);


        //点击加入社区
        tv_class_name.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mPostClassListHeadBean.getFollow_relation().equals("0")) {
                            setBooder();
                            initConcern("0");
                            mPostClassListHeadBean.setFollow_relation("1");
                        } else if (mPostClassListHeadBean.getFollow_relation().equals("1")) {
                            dialogShow();
                        }
                    }
                });




       /* mCommHotAdapter.setHeaderView(headerVieww);
        mCommHotAdapter.setFooterView(loadingData);
        mRecyclerView.setAdapter(mCommHotAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //   Log.i("Main", "用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                        Glide.with(getApplication()).pauseRequests();
                        //刷新
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //  Log.i("Main", "视图已经停止滑动");
                        Glide.with(getApplication()).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //  Log.i("Main", "手指没有离开屏幕，视图正在滑动");
                        Glide.with(getApplication()).resumeRequests();
                        break;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (!recyclerView.canScrollVertically(-1)) {
                    //T.show(mActivity,"已经到第一条");
                } else if (!recyclerView.canScrollVertically(1)) {
                    //T.show(mActivity,"到了最后一条");
                } else if (dy < 0) {
                    //T.show(mActivity,"正在向上滑动");
                } else if (dy > 0) {
                    // T.show(mActivity,"正在向下滑动");
                    if (lastVisibleItemPosition + 1 == mCommHotAdapter.getItemCount() - 5) {
                        //  Log.e("search", "loading executed");
                        if (!isLoading) {
                            isLoading = true;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    PAGENUMBER++;
                                    // Log.d("search", "load more completed");
                                    initData(PAGENUMBER);
                                    isLoading = false;
                                }
                            }, 200);
                        }
                    }
                }

            }
        });*/
    }

    private void setHeadData(PostClassListHeadBean postClassListHeadBean) {

        tv_title.setText(postClassListHeadBean.getClass_name());
        //是否加入
        if (postClassListHeadBean.getFollow_relation().equals("0")) {
            tv_class_name.setText("+加入");
        } else if (postClassListHeadBean.getFollow_relation().equals("1")) {
            setBooder();
        }

        String mPosts_num = postClassListHeadBean.getPosts_num();
        StringBuilder stringBuilder = new StringBuilder();
        mTvPostNum.setText("人数：" + postClassListHeadBean.getFollow_count());
        stringBuilder.append("帖子：");
        stringBuilder.append(mPosts_num);
        tvPostDes.setText(stringBuilder);
        Glide.with(getApplication())
                .load(postClassListHeadBean.getClass_virtual_image())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(ivBg);
        Glide.with(getApplication())
                .load(postClassListHeadBean.getClass_head_image())
                .transform(new GlideCircleTransform(PostClassListActivity.this))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(ivLabel);
        tv_post_content.setText(postClassListHeadBean.getDescribe());
    }

    //回调录制完成的路径
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2002) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int type = data.getIntExtra(MediaActivity.RESULT_TYPE, 0);
                if (type == MediaActivity.RESULT_TYPE_CROP) {
                    String path = data.getStringExtra(AliyunVideoCrop.RESULT_KEY_CROP_PATH);
                    Toast.makeText(this, "文件路径为 " + path + " 时长为 " +
                            data.getLongExtra(AliyunVideoCrop.RESULT_KEY_DURATION, 0), Toast.LENGTH_SHORT).show();
                } else if (type == MediaActivity.RESULT_TYPE_RECORD) {
                    Toast.makeText(this, "文件路径为 " +
                            data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH), Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "用户取消裁剪", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_RECORD) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE, 0);

                if (type == AliyunVideoRecorder.RESULT_TYPE_CROP) {
                    String path = data.getStringExtra(AliyunVideoCrop.RESULT_KEY_CROP_PATH);
                    if (path.endsWith(".mp4")) {
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
                        media.setDataSource(path);
                        Bitmap bitmap = media.getFrameAtTime();
                        ChangeBitmap changeBitmap = new ChangeBitmap();
                        imagePath = changeBitmap.saveBitmap(PostClassListActivity.this, bitmap);
                        Intent intent = new Intent(PostClassListActivity.this, VideoActivity.class);
                        Bundle b = new Bundle();
                        b.putParcelable("bitmap", bitmap);
                        b.putString("path", path);
                        intent.putExtras(b);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PostClassListActivity.this, "请选择视频上传", Toast.LENGTH_SHORT).show();
                    }
                } else if (type == AliyunVideoRecorder.RESULT_TYPE_RECORD) {
                    path = data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH);
                    if (path.endsWith(".mp4")) {
                        //设置图片
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
                        media.setDataSource(path);
                        Bitmap bitmap = media.getFrameAtTime(1500, OPTION_CLOSEST_SYNC);
                        ChangeBitmap changeBitmap = new ChangeBitmap();
                        imagePath = changeBitmap.saveBitmap(PostClassListActivity.this, bitmap);
                        Intent intent = new Intent(PostClassListActivity.this, VideoActivity.class);
                        Bundle b = new Bundle();
                        b.putParcelable("bitmap", bitmap);
                        b.putString("path", path);
                        intent.putExtras(b);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PostClassListActivity.this, "请选择视频上传", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    //初始化阿里云
    private void initAssetPath() {
        String path = StorageUtils.getCacheDirectory(PostClassListActivity.this).getAbsolutePath() + File.separator + Utils.QU_NAME + File.separator;
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

    public AliyunSnapVideoParam aliyunSnap() {
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
                // .setCropMode(ScaleMode.PS)
                .build();
        return recordParam;
    }

    private void deletePost(String tid) {
        OkHttpUtils.post()//
                .addParams("tid", tid)
                .addParams("uid", UserUtils.getUserId())
                .addParams("type", "1")
                .addParams("dateline", "1")
                .addParams("key", EncryptUtils.encryptMD5ToString(tid + "1" + "xingyy").toLowerCase())
                .url(XingYuInterface.MANAGE_AUTH_DELETE)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("shanchutiezi", "deletePost: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("shanchutiezi", "deletePost: " + response);
                    }
                });
    }
/*    private void initSwipeRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PAGENUMBER=1;
                        CLEAR_DATA = true;
                        initData(PAGENUMBER);
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }*/

    /**
     * 初始化数据
     */
    public void initData(int PAGENUMBER) {
        OkHttpUtils.post()//
                .addParams("page", String.valueOf(PAGENUMBER))
                .addParams("type", getIntent().getStringExtra("attribute"))
                .addParams("fid", getIntent().getStringExtra("list_id"))
                .addParams("uid", UserUtils.getUserId())
                .addParams("attribute", "1")
                .url(XingYuInterface.GET_POSTS_LISTT)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        handler.obtainMessage(1, response).sendToTarget();
                    }
                });
        OkHttpUtils.post()//
                .addParams("fid", getIntent().getStringExtra("list_id"))
                .url("http://xingyuyou.com/app.php/Theme/formu_theme_menu")
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        handler.obtainMessage(2, response).sendToTarget();
                    }
                });
    }

    private void updateStatus() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SortPostUpdateCollectStatus");
        intentFilter.addAction("SortPostUpdateZanStatus");
        BroadcastReceiver br1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("SortPostUpdateCollectStatus")) {
                    if (intent.getIntExtra("cancelCollect", 0) == 0) {
                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).setCollect_status(0);
                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1))
                                .setPosts_collect(String.valueOf((Integer.parseInt(
                                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).getPosts_collect())) - 1));
                    } else {
                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).setCollect_status(1);
                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1))
                                .setPosts_collect(String.valueOf((Integer.parseInt(
                                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).getPosts_collect())) + 1));
                    }
                }
                if (intent.getAction().equals("SortPostUpdateZanStatus")) {
                    if (intent.getIntExtra("cancelZan", 0) == 0) {
                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).setLaud_status(0);
                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1))
                                .setPosts_laud(String.valueOf((Integer.parseInt(
                                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).getPosts_laud())) - 1));
                    } else {
                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).setLaud_status(1);
                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1))
                                .setPosts_laud(String.valueOf((Integer.parseInt(
                                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).getPosts_laud())) + 1));
                    }
                }
                mCommHotAdapter.notifyItemChanged(intent.getIntExtra("position", 0));
            }

        };
        localBroadcastManager.registerReceiver(br1, intentFilter);
    }

    public void initConcern(final String relation) {
        OkHttpUtils.post()
                .addParams("uid", UserUtils.getUserId())
                .addParams("re_uid", getIntent().getStringExtra("list_id"))
                .addParams("relation", relation)
                .addParams("type", "2")
                .url(XingYuInterface.GET_OTHERCONCERN)
                .tag(this)
                .build()
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
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
        //mLocalBroadcastManager.unregisterReceiver(mBr);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DCAgent.resume(this);
        DCPage.onEntry("PostClassListActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("PostClassListActivity");
    }

    public void dialogShow() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(PostClassListActivity.this);
/*            mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            private Button negativeBtn ;
            private Button positiveBtn;
            @Override
            public void onShow(DialogInterface dialogInterface) {
                //设置button文本大小
                positiveBtn = ((AlertDialog) normalDialog).getButton(DialogInterface.BUTTON_POSITIVE);
                negativeBtn = ((AlertDialog) normalDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                positiveBtn.setTextSize(20);
                negativeBtn.setTextSize(20);
            }
        });*/
        try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object alertController = mAlert.get(normalDialog);
            Field mTitleView = alertController.getClass().getDeclaredField("mTitleView");
            mTitleView.setAccessible(true);
            TextView title = (TextView) mTitleView.get(alertController);
            title.setTextSize(200);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // Button  negativeBtn = ((AlertDialog)normalDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        normalDialog.setTitle("你真的想好要退出了吗?");
        normalDialog.setPositiveButton("我确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setBooderPink();
                        initConcern("1");
                        mPostClassListHeadBean.setFollow_relation("0");
                        Toast.makeText(PostClassListActivity.this, "已取消加入（*>.<*）~ @", Toast.LENGTH_SHORT).show();
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

    public void setBooder() {
        tv_class_name.setTextColor(getResources().getColor(R.color.white));
        tv_class_name.setBackgroundResource(R.drawable.bg_theme_circle_is_add);
        tv_class_name.setText("已加入");
    }

    public void setBooderPink() {
        tv_class_name.setBackgroundResource(R.drawable.bg_theme_circle_add);
        tv_class_name.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_class_name.setText("+加入");
    }
}
