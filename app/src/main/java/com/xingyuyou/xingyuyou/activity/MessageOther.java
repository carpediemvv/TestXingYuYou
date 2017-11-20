package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.KeyboardUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.EmotionAdapter;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.EmotionKeyboard;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.GlobalOnItemClickManager;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.SpanStringUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.other.OtherMessageBean;
import com.xingyuyou.xingyuyou.bean.other.Reply_Bean;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MessageOther extends AppCompatActivity {
    private TextView mTvContent;
    private TextView mUserName;
    private TextView mPostTime;
    private TextView mCommoContent;
    private TextView mFloorNum;
    private TextView mLoveNum;
    private LinearLayout mLlRootImage;
    private ImageView mIvLove;
    private ImageView mIvUserPhoto;
    private RecyclerView mRecyclerView;
    private EmotionKeyboard emotionKeyboard;
    private RadioGroup rgTipPoints;
    private RadioButton rbPoint;
    private static final int emsNumOfEveryFragment = 20;//每页的表情数量
    private FrameLayout extendView, emotionView;
    private NestedScrollView contentView;
    private ImageView extendButton, emotionButton;
    private EditText edittext;
    private Button btnSend;
    private Toolbar mToolbar;
    private CustomDialog mDialog;
    private OtherMessageBean.DataBean mPostCommoBean;
    private CommoToCommoAdapter mToCommoAdapter;
    private String position;
    ArrayList<Reply_Bean.DataBean> mCommoList = new ArrayList<>();
   ArrayList <Reply_Bean.DataBean> otherList = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String response = (String) msg.obj;
            JSONObject jo = null;
            if (msg.what == 1) {
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mCommoList = gson.fromJson(ja.toString(),
                            new TypeToken<List<Reply_Bean.DataBean>>() {
                            }.getType());
                    otherList.addAll(mCommoList);
                    mToCommoAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reply_commo);
        initData();
        initCommoData(1);
        initToolBar();
        initKeyBoardView();
        initView();
    }

    private void initData() {
        mPostCommoBean = (OtherMessageBean.DataBean) getIntent().getSerializableExtra("item_list");
        position = getIntent().getStringExtra("position");
    }

    //初始化toolbar
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        mUserName = (TextView) findViewById(R.id.tv_user_name);
        mPostTime = (TextView) findViewById(R.id.tv_post_time);
        mCommoContent = (TextView) findViewById(R.id.tv_commo_content);
        mFloorNum = (TextView) findViewById(R.id.tv_floor_num);
        mLoveNum = (TextView) findViewById(R.id.tv_love_num);
        mLlRootImage = (LinearLayout) findViewById(R.id.ll_root_image_item);

        mIvLove = (ImageView) findViewById(R.id.iv_love);
        mIvUserPhoto = (ImageView) findViewById(R.id.iv_user_photo);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mIvUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(MessageOther.this, LoginActivity.class);
                    return;
                } else {
                    if (UserUtils.getUserId().equals(mPostCommoBean.re_uid)) {
                        startActivity(new Intent(MessageOther.this, UserPageActivity.class));
                    } else {
                        Intent intent = new Intent(MessageOther.this, OtherPageActivity.class);
                        intent.putExtra("re_uid", mPostCommoBean.re_uid);
                        startActivity(intent);
                    }

                }
            }
        });
        //初始化
        initRecyclerView();
        setValues();
    }

    //****************************************楼中楼回复显示********************************************************
    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mToCommoAdapter = new CommoToCommoAdapter(otherList);
        mRecyclerView.setAdapter(mToCommoAdapter);

    }

    private void setValues() {
        Glide.with(getApplication())
                .load(mPostCommoBean.head_image)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(getApplication()))
                .into(mIvUserPhoto);
        mUserName.setText(mPostCommoBean.nickname);
        mPostTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mPostCommoBean.dateline + "000")));
        mCommoContent.setText(SpanStringUtils.getEmotionContent(getApplication(), mCommoContent, mPostCommoBean.content));
        //mFloorNum.setText(mPostCommoBean. + "楼");
        mIvLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    //*****************************************软键盘*****************************************************
    private void initKeyBoardView() {
        contentView = (NestedScrollView) findViewById(R.id.txt_main_content);
        extendButton = (ImageView) findViewById(R.id.bt_add_image);
        emotionButton = (ImageView) findViewById(R.id.img_reply_layout_emotion);
        edittext = (EditText) findViewById(R.id.edit_text);
        edittext.addTextChangedListener(new ButtonBtnWatcher());//动态监听EditText
        btnSend = (Button) findViewById(R.id.btn_send);
        extendView = (FrameLayout) findViewById(R.id.extend_layout);
        emotionView = (FrameLayout) findViewById(R.id.emotion_layout);
        //发送
        initButton();
        //绑定软键盘
        bindToEmotionKeyboard();
    }


    /**
     * 绑定软键盘
     */
    private void bindToEmotionKeyboard() {
        emotionKeyboard = EmotionKeyboard.with(this)
                .setExtendView(extendView)
                .setEmotionView(emotionView)
                .bindToContent(contentView)
                .bindToEditText(edittext)
                .bindToEmotionButton(emotionButton)
                .build();
        setUpEmotionViewPager();
        setUpExtendView();
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
            if (!TextUtils.isEmpty(edittext.getText().toString())) { //有文本内容，按钮为可点击状态
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
     * 设置表情布局下的视图
     */
    private void setUpEmotionViewPager() {
        int fragmentNum;
        /*获取ems文件夹有多少个表情  减1 是因为有个删除键
                         每页20个表情  总共有length个表情
                         先判断能不能整除  判断是否有不满一页的表情
		 */
        int emsTotalNum = getSizeOfAssetsCertainFolder("ems") - 1;//表情的数量(除去删除按钮)
        if (emsTotalNum % emsNumOfEveryFragment == 0) {
            fragmentNum = emsTotalNum / emsNumOfEveryFragment;
        } else {
            fragmentNum = (emsTotalNum / emsNumOfEveryFragment) + 1;
        }
        EmotionAdapter mViewPagerAdapter = new EmotionAdapter(getSupportFragmentManager(), fragmentNum);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(0);

        GlobalOnItemClickManager globalOnItemClickListener = GlobalOnItemClickManager.getInstance();
        globalOnItemClickListener.attachToEditText((EditText) findViewById(R.id.edit_text));

		/* 设置表情下的提示点 */
        setUpTipPoints(fragmentNum, mViewPager);
    }

    /**
     * 获取assets下某个指定文件夹下的文件数量
     */
    private int getSizeOfAssetsCertainFolder(String folderName) {
        int size = 0;
        try {
            size = getAssets().list(folderName).length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 设置扩展布局下的视图
     */
    private void setUpExtendView() {

    }

    /**
     * @param num 提示点的数量
     */
    private void setUpTipPoints(int num, ViewPager mViewPager) {
        rgTipPoints = (RadioGroup) findViewById(R.id.rg_reply_layout);
        for (int i = 0; i < num; i++) {
            rbPoint = new RadioButton(this);
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(30, 30);
            lp.setMargins(10, 0, 10, 0);
            rbPoint.setLayoutParams(lp);
            rbPoint.setId(i);//为每个RadioButton设置标记
            rbPoint.setButtonDrawable(getResources().getDrawable(R.color.transparent));//设置button为@null
            rbPoint.setBackgroundResource(R.drawable.emotion_tip_points_selector);
            rbPoint.setClickable(false);
            if (i == 0) { // 第一个点默认为选中，与其他点显示颜色不同
                rbPoint.setChecked(true);
            } else {
                rbPoint.setChecked(false);
            }
            rgTipPoints.addView(rbPoint);
        }
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                rgTipPoints.check(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //*****************************************回复****************************************************
    private void initButton() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReply();
            }
        });
    }


    /**
     * 回帖
     */
    private void sendReply() {
        if (StringUtils.isEmpty(edittext.getText().toString().trim())) {
            Toast.makeText(this, "评论内容为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //关闭键盘
        KeyboardUtils.hideSoftInput(MessageOther.this);
        emotionKeyboard.interceptBackPress();
        mDialog = new CustomDialog(MessageOther.this, "正在回复中...");
        mDialog.showDialog();
        final String trim = edittext.getText().toString().trim();
        OkHttpUtils.post()//
                .addParams("uid", mPostCommoBean.uid)
                .addParams("content", trim)
                .addParams("pid", mPostCommoBean.id)
                .addParams("re_uid", UserUtils.getUserId())
                .url(XingYuInterface.LEAVING_ADD)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                        mDialog.dismissDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mDialog.dismissDialog();
                        Reply_Bean.DataBean re_List = new Reply_Bean.DataBean();
                        re_List.setContent(trim);
                        re_List.setNickname(UserUtils.getNickName());
                        re_List.setDateline(String.valueOf(TimeUtils.getNowTimeMills()).substring(0, String.valueOf(TimeUtils.getNowTimeMills()).length()-3));
                        otherList.add(re_List);
                        edittext.setText("");
                        mToCommoAdapter.notifyDataSetChanged();
                        //发送一条广播更新帖子回帖列表的评论
                        updatePostCommoList(re_List);
                    }
                });
    }
    private void initCommoData(int PAGENUM) {
        OkHttpUtils.post()
                .addParams("lid",mPostCommoBean.id)
                .url(XingYuInterface.LEAVING_CONTENT)
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
    }

    private void updatePostCommoList(Reply_Bean.DataBean childBean) {
        Intent intent = new Intent();
        intent.setAction("updateCommoList");
        intent.putExtra("childBean",childBean);
        intent.putExtra("position", getIntent().getIntExtra("position", 0));
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(intent);
    }


    private class CommoToCommoAdapter extends RecyclerView.Adapter {
        private ArrayList<Reply_Bean.DataBean> relistBeen;

        public CommoToCommoAdapter(ArrayList<Reply_Bean.DataBean> relistBeen) {
            this.relistBeen = relistBeen;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(MessageOther.this).inflate(R.layout.item_commo_to_commo_list, parent, false);
            return new CommoToCommoAdapter.ItemCommoViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            String   nickname =relistBeen.get(position).nickname;
            String    replies_content1 =relistBeen.get(position).content;
            String   dateline =TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(relistBeen.get(position).dateline + "000"));
            SpannableStringBuilder adpaterText = SpanStringUtils.getAdpaterText(nickname+":", replies_content1, "   "+dateline,((CommoToCommoAdapter.ItemCommoViewHolder) holder).mUser2Name, getApplication());
            ((CommoToCommoAdapter.ItemCommoViewHolder) holder).mUser2Name.setText(adpaterText);
/*          ((CommoToCommoAdapter.ItemCommoViewHolder) holder).mCommo2Content.setText(SpanStringUtils.getEmotionContent(getApplication(), ((CommoToCommoAdapter.ItemCommoViewHolder) holder).mCommo2Content, relistBeen.get(position).content));
            //更改时间
            ((CommoToCommoAdapter.ItemCommoViewHolder) holder).mCommo2Time.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(relistBeen.get(position).dateline + "000")));*/
            ((CommoToCommoAdapter.ItemCommoViewHolder) holder).mRlItemCommoDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String content = "@" + relistBeen.get(position).nickname + ":" + " ";
                    SpannableString spannableString = new SpannableString(content);
                    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edittext.setText(spannableString);
                    edittext.setSelection(content.length());
                    //开启键盘
                    KeyboardUtils.showSoftInput(edittext);
                }
            });
        }
        @Override
        public int getItemCount() {
            return relistBeen.size();
        }

        class ItemCommoViewHolder extends RecyclerView.ViewHolder {
            private TextView mUser2Name;
/*           private TextView mCommo2Content;
            private TextView mCommo2Time;*/
            private RelativeLayout mRlItemCommoDetail;

            public ItemCommoViewHolder(View itemView) {
                super(itemView);
                mRlItemCommoDetail = (RelativeLayout) itemView.findViewById(R.id.item_commo_detail);
                mUser2Name = (TextView) itemView.findViewById(R.id.tv_user2_name);
/*                mCommo2Content = (TextView) itemView.findViewById(R.id.tv_commo2_content);
                mCommo2Time = (TextView) itemView.findViewById(R.id.tv_commo2_time);*/
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        DCAgent.resume(this);
        DCPage.onEntry("MessageOther");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("MessageOther");
    }
    @Override
    public void onBackPressed() {
        if (!emotionKeyboard.interceptBackPress()) {
            super.onBackPressed();
        }
    }
}


