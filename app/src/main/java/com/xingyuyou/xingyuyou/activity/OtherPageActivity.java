package com.xingyuyou.xingyuyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.google.gson.Gson;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseUser;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xingyuyou.xingyuyou.Dao.APPConfig;
import com.xingyuyou.xingyuyou.Dao.HxEaseuiHelper;
import com.xingyuyou.xingyuyou.Dao.SharedPreferencesUtils;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.KeyboardUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.glide.ColorFilterTransformation;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.Dialog_adapter;
import com.xingyuyou.xingyuyou.adapter.OtherpagerAdapter;
import com.xingyuyou.xingyuyou.bean.user.OtherMessageBean;
import com.xingyuyou.xingyuyou.fragment.Fragment_card_otherpage;
import com.xingyuyou.xingyuyou.fragment.Fragment_comment_otherpage;
import com.xingyuyou.xingyuyou.fragment.Fragment_message_otherpage;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class OtherPageActivity extends AppCompatActivity {
    private ViewPager viewPager_below_activity;
    private TabLayout mTabLayout;
    private AppBarLayout mAppBarLayout;
    private TextView mTvTitle;
    private int tag = 1;
    private Toolbar mToolbar;
    //存放Fragment
    private ArrayList<Fragment> fragmentArrayList;
    //管理Fragment
    private FragmentManager fragmentManager;
    public Context context;
    private String mResponse;
    private OtherMessageBean.DataBean otherBean;
    Handler handler = new Handler() {
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
                    otherBean = gson.fromJson(ja.toString(), OtherMessageBean.DataBean.class);
                    tv_title.setText(otherBean.nickname);
                    button_noConcern_other.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //                  UserDao dao=new UserDao(App.mContext);
                            List<EaseUser> users=new ArrayList<EaseUser>();
                            //         if(HxEaseuiHelper.getInstance().demoModel.getContactList().size()>0){
                            Map<String, EaseUser> contactLists = HxEaseuiHelper.getInstance().demoModel.getContactList();
                            if(contactLists.size()!=0||contactLists!=null){
                                for(String str:contactLists.keySet()){
                                    EaseUser easeUser= contactLists.get(str);
                                    users.add(easeUser);
                                }
                            }
                            //存入数据库
                            EaseUser easeUser=new EaseUser(otherBean.emchat_id);
                            easeUser.setAvatar(otherBean.head_image);
                            easeUser.setNick(otherBean.nickname);
                            easeUser.setNickname(otherBean.nickname);
                            users.add(easeUser);
//                            dao.saveContactList(users);
                            HxEaseuiHelper.getInstance().demoModel.saveContactList(users);
                            HxEaseuiHelper.getInstance().demoModel.setContactSynced(true);
                            //   Map<String, EaseUser> contactList = HxEaseuiHelper.getInstance().demoModel.getContactList();
                            Intent intent = new Intent(OtherPageActivity.this, ECChatActivity.class);
                            // EaseUI封装的聊天界面需要这两个参数，聊天者的username，以及聊天类型，单聊还是群聊
                            SharedPreferencesUtils.setParam(OtherPageActivity.this, APPConfig.USER_NAME,UserUtils.getNickName());
                            //设置要发送出去的头像
                            SharedPreferencesUtils.setParam(OtherPageActivity.this,APPConfig.USER_HEAD_IMG,UserUtils.getUserPhoto());
                            intent.putExtra("userId", otherBean.emchat_id);
                            intent.putExtra("name",otherBean.nickname);
                            intent.putExtra("headImage",otherBean.head_image);
                            intent.putExtra("chatType", EMMessage.ChatType.Chat);
                            intent.putExtra("re_uid",otherBean.emchat_id);
                            startActivity(intent);
                        }
                        //   }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (tag == 1) {
                    setData(otherBean);
                    FansNumber(otherBean);
                    setRelation(otherBean);
                    tag = 2;
                } else if (tag == 2) {
                    setRelation(otherBean);
                }

            }

        }
    };
    private TextView text_name_other;
    private TextView text_number_other;
    private TextView text_sign_other;
    private TextView text_capsuleNumber_other;
    private TextView text_fansNumber_other;
    private ImageView image_head_other;
    private TextView text_lookNumber_other;
    private ImageView image_sex_other;
    private String re_uid;
    private ImageView iv_other_page_bg;
    private TextView tv_title;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout line_back_other;
    private Button button_concern_other;
    private TextView text_concem_other;
    private TextView text_fans_other;
    private RelativeLayout re_other;
    private ImageView iv_toolbar_back;
    private ImageView iv_share;
    private ImageView iv_setting;
    private AlertDialog.Builder normalDialog;
    private Button button_noConcern_other;
    private String[] mTitles = {"帖子", "评论", "留言板"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSupportActionBar(mToolbar);
        //初始化控件
        initView();
        //初始化数据
        initData();

        //初始化toolbar
        initToolBar();
        //请求数据
        //initImageView();
        //初始化Fragment
        InitFragment();
        //初始化ViewPager
        InitViewPager();
    }
    //初始化toolbar
    private void initToolBar() {
/*      NestedScrollView mNestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        mNestedScrollView.setFillViewport(true);*/
        iv_toolbar_back = (ImageView) findViewById(R.id.iv_toolbar_back);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        iv_toolbar_back = (ImageView) findViewById(R.id.iv_toolbar_back);
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normalDialog = new AlertDialog.Builder(OtherPageActivity.this);
                final AlertDialog dialog = normalDialog.create();
                report(dialog,OtherPageActivity.this,re_uid);
            }
        });
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareUM();
            }
        });
    }

    private void initData() {
        //uid他人ID   re_uid自己的ID
        Intent intent = getIntent();
        re_uid = intent.getStringExtra("re_uid");
        OkHttpUtils.post()
                .addParams("uid", re_uid)
                .addParams("re_uid", UserUtils.getUserId())
                .url(XingYuInterface.GET_OTHERMESSAGE)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        handler.obtainMessage(1, response).sendToTarget();

                    }
                });
    }


    @Override
    protected void onResume() {
        DCAgent.resume(this);
        DCPage.onEntry("OtherPageActivity");
        /**
         * 设置为竖屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
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


    private void InitFragment() {
        fragmentArrayList = new ArrayList<Fragment>();
        Fragment_card_otherpage fragment_card_otherpage = new Fragment_card_otherpage();
        Fragment_comment_otherpage fragment_comment_otherpage = new Fragment_comment_otherpage();
        Fragment_message_otherpage fragment_message_otherpage = new Fragment_message_otherpage();
        fragmentArrayList.add(fragment_card_otherpage);
        fragmentArrayList.add(fragment_comment_otherpage);
        fragmentArrayList.add(fragment_message_otherpage);
        fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("uid", re_uid);
        fragment_card_otherpage.setArguments(bundle);
        fragment_comment_otherpage.setArguments(bundle);
        fragment_message_otherpage.setArguments(bundle);
        viewPager_below_activity.setAdapter(new OtherpagerAdapter(fragmentManager, mTitles,fragmentArrayList));
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //初始verticalOffset为0，不能参与计算。
   /*             if (verticalOffset <= -line_back_other.getHeight() / 2) {

                    text_name_other.setVisibility(View.INVISIBLE);
                    text_number_other.setVisibility(View.INVISIBLE);
                    text_sign_other.setVisibility(View.INVISIBLE);
                    re_other.setVisibility(View.INVISIBLE);
                    //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
                    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    tv_title.setText("");
                    text_name_other.setVisibility(View.VISIBLE);
                    text_number_other.setVisibility(View.VISIBLE);
                    text_sign_other.setVisibility(View.VISIBLE);
                    re_other.setVisibility(View.VISIBLE);
                }*/

                tv_title.setAlpha(Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange());
                text_name_other.setAlpha((appBarLayout.getTotalScrollRange()-Math.abs(verticalOffset*1.0f))/appBarLayout.getTotalScrollRange());
                text_number_other.setAlpha((appBarLayout.getTotalScrollRange()-Math.abs(verticalOffset*1.0f))/appBarLayout.getTotalScrollRange());
                text_sign_other.setAlpha((appBarLayout.getTotalScrollRange()-Math.abs(verticalOffset*1.0f))/appBarLayout.getTotalScrollRange());
                re_other.setAlpha((appBarLayout.getTotalScrollRange()-Math.abs(verticalOffset*1.0f))/appBarLayout.getTotalScrollRange());
            }
        });
    }

    private void initView() {
        //滑动控件
        button_noConcern_other = (Button) findViewById(R.id.button_noConcern_other);
        re_other = (RelativeLayout) findViewById(R.id.re_other);
        line_back_other = (LinearLayout) findViewById(R.id.line_back_other);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_other_page_bg = (ImageView) findViewById(R.id.iv_other_page_bg);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvTitle = (TextView) mToolbar.findViewById(R.id.tv_title);
        text_fans_other = (TextView) findViewById(R.id.text_fans_other);
        text_concem_other = (TextView) findViewById(R.id.text_concem_other);
        button_concern_other = (Button) findViewById(R.id.button_concern_other);
        viewPager_below_activity = (ViewPager) findViewById(R.id.viewPager_below_activity);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(viewPager_below_activity);
        text_name_other = (TextView) findViewById(R.id.text_name_other);
        text_number_other = (TextView) findViewById(R.id.text_number_other);
        text_sign_other = (TextView) findViewById(R.id.text_sign_other);
        text_fansNumber_other = (TextView) findViewById(R.id.text_fansNumber_other);
        text_capsuleNumber_other = (TextView) findViewById(R.id.text_CapsuleNumber_other);
        text_lookNumber_other = (TextView) findViewById(R.id.text_lookNumber_other);
        image_sex_other = (ImageView) findViewById(R.id.image_sex_other);
        //按钮
        image_head_other = (ImageView) findViewById(R.id.image_head_other);
        button_concern_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otherBean.follow_status == 0) {
                    OnclickButton(0);
                } else if (otherBean.follow_status == 1 || otherBean.follow_status == 2) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(OtherPageActivity.this);
                    TextView tv = new TextView(OtherPageActivity.this);
                    normalDialog.setTitle("确定不再关注ta了吗?");
                    normalDialog.setPositiveButton("我确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (otherBean.follow_status == 1) {
                                        OnclickButton(1);
                                    } else if (otherBean.follow_status == 2) {
                                        OnclickButton(2);
                                    }
                                    Toast.makeText(OtherPageActivity.this, "已取消关注（*>.<*）~ @", Toast.LENGTH_SHORT).show();
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

            }
        });



    }


    //添加数据
    private void setData(final OtherMessageBean.DataBean otherMessageBean) {

        text_name_other.setText(otherMessageBean.nickname);
        text_number_other.setText("胶囊号:" + otherMessageBean.account);
        text_sign_other.setText(otherMessageBean.explain);
        if (otherMessageBean.explain == "") {
            text_sign_other.setText("他很懒什么都没有留下..");
        } else {
            text_sign_other.setText(otherMessageBean.explain);
        }


        //点击查看大图
        image_head_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherPageActivity.this, OtherBigpictureActivity.class);
                intent.putExtra("image", otherMessageBean.head_image);
                startActivity(intent);
            }
        });


        text_lookNumber_other.setText(otherMessageBean.follow_num);
        text_capsuleNumber_other.setText(otherMessageBean.user_integral);
        //是男是女
        if (otherMessageBean.sex.length() == 0) {
            image_sex_other.setVisibility(View.GONE);
        } else if (Integer.parseInt(otherMessageBean.sex) == 1) {
            image_sex_other.setImageResource(R.mipmap.ic_action_girl);
        } else {
            image_sex_other.setImageResource(R.mipmap.ic_action_boy);
        }
        Glide.with(getApplicationContext())
                .load(otherMessageBean.head_image)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(getApplicationContext()))
                .into(image_head_other);


        //遮罩
        Glide.with(getApplication())
                .load(otherMessageBean.other_back_ground)
                .bitmapTransform(new ColorFilterTransformation(this, R.color.black))
                .priority(Priority.HIGH)
                .into(iv_other_page_bg);
        //点击粉丝关注列表

        text_fans_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("re_uid", re_uid);
                intent.putExtra("follow_status", otherMessageBean.follow_status + "");
                intent.putExtra("type", "2");
                intent.putExtra("tag", "2");
                intent.setClass(OtherPageActivity.this, ConcernListActivity.class);
                startActivity(intent);
            }
        });

        //跳转关注列表
        text_concem_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("re_uid", re_uid);
                intent.putExtra("follow_status", otherMessageBean.follow_status + "");
                intent.putExtra("type", "1");
                intent.putExtra("tag", "2");
                intent.setClass(OtherPageActivity.this, FansListActivity.class);
                startActivity(intent);
            }
        });
    }


    public void setRelation(OtherMessageBean.DataBean otherMessageBean) {
        if (otherMessageBean.follow_status == 0) {
            button_concern_other.setText("关注ta");
        } else if (otherMessageBean.follow_status == 1) {
            button_concern_other.setText("已关注");
        } else if (otherMessageBean.follow_status == 2) {
            button_concern_other.setText("已互粉");
        }
    }

    public void FansNumber(final OtherMessageBean.DataBean otherMessageBean) {
        text_fansNumber_other.setText(otherMessageBean.fans_num);
    }

    public void OnclickButton(final int follow_status) {
            OkHttpUtils.post()
                .addParams("uid", UserUtils.getUserId())
                .addParams("re_uid", re_uid)
                .addParams("relation", String.valueOf(follow_status))
                .url(XingYuInterface.GET_OTHERCONCERN)
                .tag(this)//
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (follow_status == 1 || follow_status == 2) {
                            initData();
                            button_concern_other.setText("关注ta");
                        } else if (follow_status == 0) {
                            initData();
                        }

                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("OtherPageActivity");
    }
    private void shareUM() {
        UMImage thumb = new UMImage(OtherPageActivity.this, R.mipmap.icon);
        UMWeb web = new UMWeb("http://xingyuyou.com/app.php/Share/download");
        web.setTitle("人生如戏，全靠游戏");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("一个二次元的世界");//描述
        new ShareAction(OtherPageActivity.this).withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setCallback(umShareTestListener).open();
    }
    private UMShareListener umShareTestListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            Toast.makeText(OtherPageActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(OtherPageActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(OtherPageActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    public void  report(final AlertDialog dialog, final Context context, final String uid){
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
                if (mAdapter.getIndex() == -1) {
                    Toast.makeText(context, "请选择一项举报理由", Toast.LENGTH_SHORT).show();
                } else {
                    KeyboardUtils.hideSoftInput((Activity) context);
                    OkHttpUtils.post()
                            .addParams("uid", UserUtils.getUserId())
                            .addParams("re_uid", uid)
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
}
