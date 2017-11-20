package com.xingyuyou.xingyuyou.fragment;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.KeyboardUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.EmotionAdapter;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.EmotionKeyboard;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.GlobalOnItemClickManager;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.MessageOther;
import com.xingyuyou.xingyuyou.activity.OtherPageActivity;
import com.xingyuyou.xingyuyou.activity.UserPageActivity;
import com.xingyuyou.xingyuyou.adapter.CommHotAdapter;
import com.xingyuyou.xingyuyou.adapter.MessageAdapter;
import com.xingyuyou.xingyuyou.bean.other.OtherMessageBean;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/5.
 */

public class Fragment_message_otherpage extends Fragment {
    private int PAGENUMBER = 1;
    private CustomDialog mDialog;
    private AlertDialog mAlertDialog;
    boolean isLoading = false;
    private MessageAdapter mCommHotAdapter;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private LinearLayoutManager mLinearLayoutManager;
    private View view;
    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mBroadcastReceiver;
    private ArrayList<OtherMessageBean.DataBean> otherMessageBean= new ArrayList<>();
    private ArrayList<OtherMessageBean.DataBean> dataBean= new ArrayList<>();
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {
                   // Toast.makeText(getActivity(), "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    otherMessageBean = gson.fromJson(ja.toString(),
                            new TypeToken<List<OtherMessageBean.DataBean>>() {
                            }.getType());
                    dataBean.addAll(otherMessageBean);
                    if (dataBean.size()<20) {
                        //Toast.makeText(getActivity(), "已经没有更多数据", Toast.LENGTH_SHORT).show();
                        mPbNodata.setVisibility(View.GONE);
                        mTvNodata.setText("已经没有更多数据");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //更新UI
                if (mCommHotAdapter != null)
                    mCommHotAdapter.notifyDataSetChanged();
            }
        }
    };


    private RecyclerView listView_message;
    private FrameLayout extendView, emotionView;
    private LinearLayout contentView;
    private ImageView extendButton, emotionButton;
    private EditText edittext;
    private Button btnSend;
    private EmotionKeyboard emotionKeyboard;
    private RadioGroup rgTipPoints;
    private RadioButton rbPoint;
    private static final int emsNumOfEveryFragment = 20;//每页的表情数量
    private RecyclerView mRecyclerView;
    private static final int TYPE_FOOTER = 21;
    int  NUM=1;
    private LinearLayout mLl_emotion_parent;
    private LinearLayout mLl_edit_bottom;
    private ImageView mIv_back;
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message_otherpage, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initCommoData(1);
        initKeyBoardView();

    }


    //*********************************************以下是软键盘设置代码*****************************************************
    private void initKeyBoardView() {
        contentView = (LinearLayout) getActivity().findViewById(R.id.txt_main_content);
        mIv_back = (ImageView) view.findViewById(R.id.iv_back);
        emotionButton = (ImageView) getActivity().findViewById(R.id.img_reply_layout_emotion);
        edittext = (EditText) getActivity().findViewById(R.id.edit_text);
        edittext.addTextChangedListener(new ButtonBtnWatcher());//动态监听EditText
        btnSend = (Button) getActivity().findViewById(R.id.btn_send);
        extendView = (FrameLayout) getActivity().findViewById(R.id.extend_layout);
        emotionView = (FrameLayout) getActivity().findViewById(R.id.emotion_layout);
        //绑定软键盘
        bindToEmotionKeyboard();

        //发送评论点击事件
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            sendReply();

            }
        });
        //软键盘关闭事件
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭键盘
                mLl_emotion_parent.setVisibility(View.GONE);
                mLl_edit_bottom.setVisibility(View.VISIBLE);
                KeyboardUtils.hideSoftInput(getActivity());
                emotionKeyboard.interceptBackPress();
            }
        });
    }

    /**
     * 绑定软键盘
     */


    private void bindToEmotionKeyboard() {
        emotionKeyboard = EmotionKeyboard.with(getActivity())
                .setExtendView(extendView)
                .setEmotionView(emotionView)
                .bindToContent(contentView)
                .bindToEditText(edittext)
                .bindToEmotionButton(emotionButton)
                .build();
        setUpEmotionViewPager();
       // Log.e("weiwei", "bindToEmotionKeyboard: "+ emotionKeyboard.sp.getInt("soft_input_height", 750));
    }

    //留言
    private void sendReply() {
        if (StringUtils.isEmpty(edittext.getText().toString().trim())) {
            Toast.makeText(getActivity(), "评论内容为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //关闭键盘
        KeyboardUtils.hideSoftInput(getActivity());
        mDialog = new CustomDialog(getActivity(), "回复留言中...");
        mDialog.showDialog();
        emotionKeyboard.interceptBackPress();
        Bundle bundle = getArguments();
        String num="0";
        String trim = edittext.getText().toString().trim();
        OkHttpUtils.post()//
                .addParams("uid", bundle.getString("uid"))
                .addParams("content", trim)
                .addParams("pid", num)
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
                        dataBean.clear();
                        NUM = 1;
                        initCommoData(1);
                        edittext.setText("");
                       /* listView_message.setAdapter(mCommHotAdapter);*/
                    }
                });

        //获取广播传过来的数据
        //updatePostCommoList();
    }

/*    private void updatePostCommoList() {
        mLocalBroadcastManager = LocalBroadcastManager
                .getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("updatePostCommoList");
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("updateCommoList")) {
                    Reply_Bean.DataBean childBean = (Reply_Bean.DataBean) intent.getSerializableExtra("childBean");
                    if (otherMessageBean.data.get(0).relist == null) {
                        list = new ArrayList<>();
                        relistBean = new OtherMessageBean.DataBean.RelistBean();
                        relistBean.setContent(childBean.content);
                        relistBean.setNickname(childBean.nickname);
                        relistBean.setDateline(childBean.dateline);
                        list.add(relistBean);
                        otherMessageBean.data.get(intent.getIntExtra("position", 0)).setRelist(list);
                    } else if (otherMessageBean.data.get(intent.getIntExtra("position", 0)).relist .size()==1) {
                        list = new ArrayList<>();
                        list.add(otherMessageBean.data.get(intent.getIntExtra("position", 0)).relist.get(0));
                        list.add(relistBean);
                        otherMessageBean.data.get(intent.getIntExtra("position", 0)).setRelist(list);
                        mCommHotAdapter.notifyDataSetChanged();
                    }
                }
                mCommHotAdapter.notifyDataSetChanged();
            }
        };
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);
    }*/
    /**
     * 软键盘文本内容监听
     */
    class ButtonBtnWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          /*  if (!TextUtils.isEmpty(edittext.getText().toString())) { //有文本内容，按钮为可点击状态
                btnSend.setBackgroundResource(R.drawable.shape_button_reply_button_clickable);
                btnSend.setTextColor(getResources().getColor(R.color.light_white));
            } else { // 无文本内容，按钮为不可点击状态
                btnSend.setBackgroundResource(R.drawable.shape_button_reply_button_unclickable);
                btnSend.setTextColor(getResources().getColor(R.color.reply_button_text_disable));
            }*/
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
    private void startActivityToPostReplyCommo(int position){
/*        if (otherMessageBean.data.get(position).relist.size()== position)
            return;*/
        if (dataBean.get(position).relist!=null) {
            Intent intent = new Intent(getActivity(), MessageOther.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("item_list", dataBean.get(position));
            bundle.putString("uid", bundle.getString("uid"));
            intent.putExtras(bundle);
            intent.putExtra("position", position);
            startActivity(intent);
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
        EmotionAdapter mViewPagerAdapter = new EmotionAdapter(getActivity().getSupportFragmentManager(), fragmentNum);
        ViewPager mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(0);

        GlobalOnItemClickManager globalOnItemClickListener = GlobalOnItemClickManager.getInstance();
        globalOnItemClickListener.attachToEditText((EditText) getActivity().findViewById(R.id.edit_text));

		/* 设置表情下的提示点 */
        setUpTipPoints(fragmentNum, mViewPager);
    }

    /**
     * 获取assets下某个指定文件夹下的文件数量
     */
    private int getSizeOfAssetsCertainFolder(String folderName) {
        int size = 0;
        try {
            size = getActivity().getAssets().list(folderName).length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

   /* *//**
     * 设置扩展布局下的视图
     *//*
    private void setUpExtendView() {
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rl_all_image);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mImageAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mImageAdapter);
    }*/

    /**
     * @param num 提示点的数量
     */
    private void setUpTipPoints(int num, ViewPager mViewPager) {
        rgTipPoints = (RadioGroup) getActivity().findViewById(R.id.rg_reply_layout);
        for (int i = 0; i < num; i++) {
            rbPoint = new RadioButton(getActivity());
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

    /**
     * 获取评论内容
     *
     * @param PAGENUM
     */
    private void initCommoData(int PAGENUM) {
       Bundle bundle = getArguments();
        OkHttpUtils.post()//
                .addParams("uid",bundle.getString("uid"))
                .addParams("page", String.valueOf(PAGENUM))
                .url(XingYuInterface.LEAVING_MESSAGE)
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

    private void initView() {
        //点击显示留言输入框

        mLl_emotion_parent = (LinearLayout) view.findViewById(R.id.ll_emotion_parent);
        mLl_edit_bottom = (LinearLayout) view.findViewById(R.id.ll_edit_bottom);
        mLl_edit_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLl_emotion_parent.setVisibility(View.VISIBLE);
                mLl_edit_bottom.setVisibility(View.GONE);
                //开启软键盘
                KeyboardUtils.showSoftInput(edittext);

                //设置表情布局
               // LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)mLl_emotion_parent.getLayoutParams();
               // layoutParams.setMargins(0,0,0,emotionKeyboard.sp.getInt("soft_input_height", 750));
               // mLl_emotion_parent.setLayoutParams(layoutParams);
            }
        });

         NestedScrollView mNestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        mNestedScrollView.setFillViewport(false);
        listView_message = (RecyclerView) view.findViewById(R.id.listView_message);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        listView_message.setLayoutManager(mLinearLayoutManager);
         listView_message.setNestedScrollingEnabled(false);
        View loadingData = View.inflate(getActivity(), R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        TextView textView = new TextView(getActivity());
        textView.setText("");

        mCommHotAdapter = new MessageAdapter(dataBean, getActivity());
        listView_message.setAdapter(mCommHotAdapter);

        mCommHotAdapter.imageSetOnclick(new MessageAdapter.ImageInterface() {
            @Override
            public void onclick(int position) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(getActivity(), LoginActivity.class);
                    return;
                } else {
                    if (UserUtils.getUserId().equals(dataBean.get(position).re_uid)) {
                        startActivity(new Intent(getActivity(), UserPageActivity.class));
                    } else {
                        Intent intent = new Intent(getActivity(), OtherPageActivity.class);
                        intent.putExtra("re_uid", dataBean.get(position).re_uid);
                        startActivity(new Intent(intent));
                    }
                }
            }

        });

        mCommHotAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivityToPostReplyCommo(position);
            }
        });
        //长安删除
        if(getArguments().getString("uid").equals(UserUtils.getUserId())){
            mCommHotAdapter.setOnItemLongClickLitener(new CommHotAdapter.OnItemLongClickLitener() {
                @Override
                public void onItemClick(View view, final int position) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete_post, null);
                    TextView tv_delete = (TextView) view1.findViewById(R.id.tv_delete);
                    tv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mAlertDialog.dismiss();
                            deleteMessage(dataBean.get(position).id);
                            dataBean.remove(position);
                            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
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
            });
        }

        //隐藏键盘
        listView_message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mLl_emotion_parent.setVisibility(View.GONE);
                mLl_edit_bottom.setVisibility(View.VISIBLE);
                KeyboardUtils.hideSoftInput(getActivity());
                return false;
            }
        });


        listView_message.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //   Log.i("Main", "用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                        //刷新
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //  Log.i("Main", "视图已经停止滑动");
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //  Log.i("Main", "手指没有离开屏幕，视图正在滑动");
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
                                    NUM++;
                                    // Log.d("search", "load more completed");
                                    initCommoData(NUM);
                                    isLoading = false;
                                }
                            }, 200);
                        }
                    }
                }

            }
        });
       /* mCommHotAdapter.setHeaderView(textView);
        mCommHotAdapter.setFooterView(loadingData);*/

        //获取广播传过来的数据
       // updatePostCommoList();

    }


    /**
     * 删除留言
     *
     * @param lid
     */
    private void deleteMessage(final String lid) {
        OkHttpUtils.post()//
                .addParams("lid", lid)
                .url(XingYuInterface.MESSAGE_DELETE)
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }


}
