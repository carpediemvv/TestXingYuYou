package com.xingyuyou.xingyuyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.OtherPageActivity;
import com.xingyuyou.xingyuyou.activity.UserPageActivity;
import com.xingyuyou.xingyuyou.adapter.CommHotAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.community.PostListBeanTest;
import com.xingyuyou.xingyuyou.bean.community.PostTopAndWellBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by Administrator on 2016/6/28.
 */
public class CollectPostListFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private int PAGENUMBER = 1;
    private boolean IS_FIRST_INIT_DATA = true;
    private List<PostListBeanTest> mPostList = new ArrayList();
    private List<PostTopAndWellBean> mPostTopWellList = new ArrayList();
    private List<PostListBeanTest> mPostAdapterList = new ArrayList();
    boolean isLoading = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {
                   // Toast.makeText(mActivity, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    //   Log.e("post", "解析数据："+  ja.toString());
                    Gson gson = new Gson();
                    mPostList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostListBeanTest>>() {
                            }.getType());
                    mPostAdapterList.addAll(mPostList);

                    if (mPostList.size() < 20) {
                       // Toast.makeText(mActivity, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                        mPbNodata.setVisibility(View.GONE);
                        mTvNodata.setText("已经没有更多数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (mCommHotAdapter != null)
                    mCommHotAdapter.notifyDataSetChanged();
            }
        }
    };
    private CommHotAdapter mCommHotAdapter;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private Toolbar mToolbar;


    public static CollectPostListFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        CollectPostListFragment fragment = new CollectPostListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.activity_collect_list, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCommHotAdapter = new CommHotAdapter(4, mActivity, mPostAdapterList);
        View loadingData = View.inflate(mActivity, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        TextView textView = new TextView(mActivity);
        textView.setText("");
        mCommHotAdapter.setHeaderView(textView);
        mCommHotAdapter.setFooterView(loadingData);
        mRecyclerView.setAdapter(mCommHotAdapter);


        //点击头像跳转
        mCommHotAdapter.imageSetOnclick(new CommHotAdapter.ImageInterface() {
            @Override
            public void onclick(int position) {

                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(getActivity(), LoginActivity.class);
                    return;
                } else {
                    if (UserUtils.getUserId().equals(mPostAdapterList.get(position-1).getUid())) {
                        startActivity(new Intent(getActivity(), UserPageActivity.class));
                    } else {
                        Intent intent = new Intent(getActivity(), OtherPageActivity.class);
                        intent.putExtra("re_uid", mPostAdapterList.get(position-1).getUid());
                        startActivity(new Intent(intent));
                    }
                }
            }

        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //   Log.i("Main", "用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                        Glide.with(CollectPostListFragment.this).pauseRequests();
                        //刷新
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //  Log.i("Main", "视图已经停止滑动");
                        Glide.with(CollectPostListFragment.this).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //  Log.i("Main", "手指没有离开屏幕，视图正在滑动");
                        Glide.with(CollectPostListFragment.this).resumeRequests();
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
                                    initData(PAGENUMBER);
                                    isLoading = false;
                                }
                            }, 200);
                        }
                    }
                }

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(PAGENUMBER);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && IS_FIRST_INIT_DATA) {
            IS_FIRST_INIT_DATA = false;
        } else {
            //不可见时不执行操作
        }
    }

    /**
     * 初始化数据
     */
    public void initData(int PAGENUMBER) {
        Log.e("weiwei", "onResponse: " );
        SPUtils user_data = new SPUtils("user_data");
        OkHttpUtils.post()//
                .addParams("page", String.valueOf(PAGENUMBER))
                .addParams("uid", user_data.getString("id"))
                .addParams("type", "4")
                .addParams("fid", "1")
                .addParams("keyword", "1")
                .addParams("bid", "1")
                .addParams("attribute", "1")
                .url(XingYuInterface.GET_POSTS_LIST)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                        Log.e("weiwei", "onResponse: 怎么不反回"+e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("weiwei", "onResponse: "+response );
                        handler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        DCPage.onEntry("AllGameFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        DCPage.onExit("AllGameFragment");
    }
}
