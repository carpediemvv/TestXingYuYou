package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.FansListAdapter;
import com.xingyuyou.xingyuyou.bean.user.FansListBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class FansListActivity extends AppCompatActivity {

    private ListView listView_fansList;
    private ArrayList<FansListBean.DataBean> FansListBean= new ArrayList<>();
    private ArrayList<FansListBean.DataBean> dataBean= new ArrayList<>();
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private Toolbar mToolbar;
    boolean isLoading = false;
    private FansListAdapter fansListAdapter;
    private int PAGENUM = 1;
    private String tag;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {

                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("亲没有更多关注");
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    FansListBean = gson.fromJson(ja.toString(),
                            new TypeToken<List<FansListBean.DataBean>>() {
                            }.getType());
                    dataBean.addAll(FansListBean);
                    if (dataBean.size()<20) {
                  //      Toast.makeText(FansListActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                        mPbNodata.setVisibility(View.GONE);
                        mTvNodata.setText("亲没有更多关注");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //更新UI
                if (fansListAdapter != null)
                    fansListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_list);
        iniToolbar();
        initView();
        initData(1);
        setData();

    }

    private void iniToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("关注列表");
        mToolbar.setTitleTextColor(this.getResources().getColor(R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //粉丝列表
    private void initData(int  page) {
        tag = getIntent().getStringExtra("tag");
        final String re_uid = getIntent().getStringExtra("re_uid");
        final String type = getIntent().getStringExtra("type");
        OkHttpUtils.post()
                        .addParams("re_uid",re_uid)
                        .addParams("page",String.valueOf(page))
                        .addParams("type",type)
                        .addParams("uid", UserUtils.getUserId())
                        .url(XingYuInterface.GET_OTHERCONCERN_LIST)
                        .tag(this)//
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
    private void setData() {
        fansListAdapter = new FansListAdapter(FansListActivity.this,dataBean,Integer.parseInt(tag));
        listView_fansList.setAdapter(fansListAdapter);
        listView_fansList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==dataBean.size()){
                    return;
                }
                    if (!UserUtils.logined()) {
                    IntentUtils.startActivity(FansListActivity.this, LoginActivity.class);
                    return;
                }else {
                    if (UserUtils.getUserId().equals(dataBean.get(position).re_uid)){
                        startActivity(new Intent(FansListActivity.this, UserPageActivity.class));
                    }else {
                        Intent intent=new Intent(FansListActivity.this, OtherPageActivity.class);
                        intent.putExtra("re_uid",dataBean.get(position).re_uid);
                        startActivity(intent);
                    }
                }
            }
        });
        //滑动分页
       listView_fansList.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                              PAGENUM++;
                              initData(PAGENUM);
                                isLoading = false;
                            }
                        }, 200);
                    }
                }
            }
        });


    }
    private void initView() {
        listView_fansList = (ListView) findViewById(R.id.listView_FansList);
        View loadingData = View.inflate(FansListActivity.this, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        TextView textView = new TextView(FansListActivity.this);
        textView.setText("");
        listView_fansList.setDividerHeight(0);
        listView_fansList.addFooterView(loadingData);

    }
    @Override
    protected void onResume() {
        super.onResume();
        DCAgent.resume(this);
        DCPage.onEntry("FansListActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("FansListActivity");
    }
}
