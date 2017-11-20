package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.DynamicDrawableSpan;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
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
import com.xingyuyou.xingyuyou.bean.IdListBean;
import com.xingyuyou.xingyuyou.bean.community.PostCommoBean;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.xingyuyou.xingyuyou.activity.PersonActivity.KEY_CID;
import static com.xingyuyou.xingyuyou.activity.PersonActivity.KEY_NAME;

public class PostReplyCommoActivity extends AppCompatActivity {
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
    private PostCommoBean mPostCommoBean;
    private CommoToCommoAdapter mToCommoAdapter;
    List<PostCommoBean.ChildBean> mCommoList = new ArrayList();
    List<PostCommoBean.ChildBean> mCommoAdapterList = new ArrayList();
    List<String> list_complite=new ArrayList<>();
    private List<IdListBean> idListBeen =new ArrayList<>();
    List<Map<String, String>> list_id=new ArrayList<Map<String, String>>();
    /**
     * 存储@的cid、name对
     */
    private Map<String, String> cidNameMap = new HashMap<String, String>();
    List<Map<String, String>> list_map = new ArrayList<Map<String, String>>();
    String strName;
    private static final int CODE_PERSON = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String response = (String) msg.obj;
            if (msg.what == 1) {
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mCommoList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostCommoBean.ChildBean>>() {
                            }.getType());
                    mCommoAdapterList.addAll(mCommoList);
                    mToCommoAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private String nameStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reply_commo);
        initData();
        initToolBar();
        initKeyBoardView();
        initView();
    }

    private void initData() {
        String itemList = getIntent().getStringExtra("item_list");
        try {
            JSONObject jo = new JSONObject(itemList);
            Gson gson = new Gson();
            mPostCommoBean = gson.fromJson(jo.toString(), PostCommoBean.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        initCommoData(mPostCommoBean.getId());
    }

    private void initCommoData(String id) {
        OkHttpUtils.post()//
                .addParams("forums_id", id)
                .url(XingYuInterface.GET_FORUMS_INFO)
                .tag(this)//
                .build()//
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
                    IntentUtils.startActivity(PostReplyCommoActivity.this, LoginActivity.class);
                    return;
                }else {
                    if (UserUtils.getUserId().equals(mPostCommoBean.getUid())){
                        startActivity(new Intent(PostReplyCommoActivity.this, UserPageActivity.class));
                    }else {
                        Intent intent=new Intent(PostReplyCommoActivity.this, OtherPageActivity.class);
                        intent.putExtra("re_uid",mPostCommoBean.getUid());
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
        LinearLayoutManager mLinearLayoutManager=   new LinearLayoutManager(this);
        mLinearLayoutManager.setSmoothScrollbarEnabled(true);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mToCommoAdapter = new CommoToCommoAdapter();
        mRecyclerView.setAdapter(mToCommoAdapter);
    }


    private void setValues() {
        if (!(mPostCommoBean.getThumbnail_image() == null) && mPostCommoBean.getThumbnail_image().size() != 0) {
            for (int j = 0; j < mPostCommoBean.getThumbnail_image().size(); j++) {
                ImageView imageView = new ImageView(PostReplyCommoActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(ConvertUtils.dp2px(0), ConvertUtils.dp2px(5), ConvertUtils.dp2px(0), ConvertUtils.dp2px(5));
                imageView.setLayoutParams(lp);
                imageView.setAdjustViewBounds(true);
                Glide.with(getApplication())
                        .load(mPostCommoBean.getThumbnail_image().get(j).getThumbnail_image())
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(imageView);
                mLlRootImage.addView(imageView);
            }
        }


        Glide.with(getApplication())
                .load(mPostCommoBean.getHead_image())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(getApplication()))
                .into(mIvUserPhoto);
        mUserName.setText(mPostCommoBean.getNickname());
        mPostTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mPostCommoBean.getDateline() + "000")));
        mCommoContent.setText(SpanStringUtils.getEmotionContent(getApplication(), mCommoContent, mPostCommoBean.getReplies_content()));
        mFloorNum.setText(mPostCommoBean.getFloor_num() + "楼");
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
        edittext.setFilters(new InputFilter[] { new MyInputFilter() });
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


    private void goAt() {
        StringBuffer tmp = new StringBuffer();
        // 把选中人的id已空格分隔，拼接成字符串
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra(PersonActivity.KEY_SELECTED, tmp.toString());
        startActivityForResult(intent, CODE_PERSON);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
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
                    int curIndexx = edittext.getSelectionStart();
                    for(String s:tmpNameStr){
                        edittext.getText().insert(curIndexx,s);
                        nameStr=s+nameStr;
                    }
                    setAtImageSpan(nameStr);
                    if (curIndexx >= 1) {
                        edittext.getText().replace(curIndexx-1, curIndexx, "");
                    }
                }else {
                    Collections.reverse(tmpNameStr);
                    for (int i = 0; i < tmpNameStr.size(); i++) {
                        IdListBean idListBean = new IdListBean();
                        idListBean.setId(tmpCidStr.get(i));
                        idListBean.setName(tmpNameStr.get(i));
                        idListBeen.add(idListBean);
                    }
                    int curIndex = edittext.getSelectionStart();
                    for(String s:tmpNameStr){
                        edittext.getText().insert(curIndex,s);
                        nameStr=s+nameStr;
                    }
                    setAtImageSpan(nameStr);
                    if (curIndex >= 1) {
                        edittext.getText().replace(curIndex - 1, curIndex, "");
                    }
                }
                break;
        }
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
        KeyboardUtils.hideSoftInput(PostReplyCommoActivity.this);
        emotionKeyboard.interceptBackPress();
        mDialog = new CustomDialog(PostReplyCommoActivity.this, "正在回复中...");
        mDialog.showDialog();


        String s1 = StringUtils.isEmpty(edittext.getText().toString().trim()) == true ? "" : edittext.getText().toString().trim();
        if(idListBeen.size()!=0){
            String replies_content = s1.trim();
            if(replies_content.trim()!=null||replies_content.length()!=0){
                String[] split = replies_content.split(" ");
                for (int j = 0; j <split.length; j++) {
                    if(split[j].trim().length()!=0){
                        String substring = split[j].substring(0,1);
//                      存在
                        if(substring.equals("@")||substring.equals("＠")){
                            Map<String, String> map = new HashMap<String, String>();
                            String s2 = split[j].toString();
                            String s = cidNameMap.get(s2);
                            map.put("re_uid",s);
                            list_map.add(map);
                        }
                    }
                }

            }
        }
        list_map.addAll(list_id);
        JSONArray array = new JSONArray(list_map);
        Log.d("qqqq",array.toString());
        Map<String, String> params = new HashMap<String, String>();
        params.put("pid", mPostCommoBean.getId());
        params.put("uid", UserUtils.getUserId());
        params.put("tid", mPostCommoBean.getTid());
        params.put("replies_content", edittext.getText().toString().trim());
        params.put("re_uid", mPostCommoBean.getUid());
        params.put("relation", array.toString());
        PostFormBuilder post = OkHttpUtils.post();
        post.url(XingYuInterface.REPLIES)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        mDialog.dismissDialog();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mDialog.dismissDialog();
                        //待优化
                        //成功返回数据
                        list_map.clear();
                        PostCommoBean.ChildBean childBean = new PostCommoBean.ChildBean();
                        childBean.setNickname(UserUtils.getNickName());
                        childBean.setReplies_content(edittext.getText().toString().trim());
                        childBean.setDateline(String.valueOf(TimeUtils.getNowTimeMills()).substring(0, String.valueOf(TimeUtils.getNowTimeMills()).length() - 3));
                        mCommoAdapterList.add(childBean);
                        mToCommoAdapter.notifyDataSetChanged();
                        edittext.setText("");
                        //发送一条广播更新帖子回帖列表的评论
                       // updatePostCommoList(childBean);
                    }
                });
    }

    private void updatePostCommoList(PostCommoBean.ChildBean childBean) {
        Intent intent = new Intent();
        intent.setAction("updatePostCommoList");
        intent.putExtra("childBean",childBean);
        intent.putExtra("position",getIntent().getIntExtra("position",0));
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(intent);
    }

    private class CommoToCommoAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(PostReplyCommoActivity.this).inflate(R.layout.item_commo_to_commo_list, parent, false);
            return new ItemCommoViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
         String   nickname = mCommoAdapterList.get(position).getNickname();
            String    replies_content1 = " : "+ mCommoAdapterList.get(position).getReplies_content();
            String   dateline =TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoAdapterList.get(position).getDateline() + "000"));
            SpannableStringBuilder adpaterText = SpanStringUtils.getAdpaterText(nickname, replies_content1, "   "+dateline,((ItemCommoViewHolder) holder).mUser2Name, getApplication());
            ((ItemCommoViewHolder) holder).mUser2Name.setText(adpaterText);
            //((ItemCommoViewHolder) holder).mCommo2Content.setText(SpanStringUtils.getEmotionContent(getApplication(), ((ItemCommoViewHolder) holder).mCommo2Content," : "+ mCommoAdapterList.get(position).getReplies_content()));
 //         ((ItemCommoViewHolder) holder).mCommo2Time.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoAdapterList.get(position).getDateline() + "000")));
            ((ItemCommoViewHolder) holder).mRlItemCommoDetail.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    String content = " @" + mCommoAdapterList.get(position).getNickname() + " ";
                    list_complite.add(content);
                    int curIndex = edittext.getSelectionStart();
                    edittext.getText().insert(curIndex, content);
                    //开启键盘
                    KeyboardUtils.showSoftInput(edittext);
                    strName=content+strName;
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("re_uid",mCommoAdapterList.get(position).getUid());
                    list_id.add(map);
                    return true;
                }
            });

        }

        @Override
        public int getItemCount() {
            return mCommoAdapterList.size();
        }

        class ItemCommoViewHolder extends RecyclerView.ViewHolder {
            private TextView mUser2Name;
  /*          private TextView mCommo2Content;
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

    private void setAtImageSpan(String nameStr) {
        String content = String.valueOf(edittext.getText());
        if (content.endsWith("@") || content.endsWith("＠")) {
            content = content.substring(0, content.length() - 1);
        }
        String tmp = content;

        SpannableString ss = new SpannableString(SpanStringUtils.getEmotionContentEdi(PostReplyCommoActivity.this,edittext,tmp));

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
        edittext.setTextKeepState(ss);
    }
    //转成map
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
    public void onBackPressed() {
        if (!emotionKeyboard.interceptBackPress()) {
            super.onBackPressed();
        }
    }
}
