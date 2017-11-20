package com.xingyuyou.xingyuyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.OtherPageActivity;
import com.xingyuyou.xingyuyou.activity.UserPageActivity;
import com.xingyuyou.xingyuyou.adapter.CommHotAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.community.PostListBeanTest;
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
public class MyPostListFragment extends BaseFragment {
    private boolean IS_FIRST_INIT_DATA = true;
    private RecyclerView mRecyclerView;
    private int PAGENUMBER = 1;
    private List<PostListBeanTest> mPostList = new ArrayList();
    private List<PostListBeanTest> mPostAdapterList = new ArrayList();
    boolean isLoading = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {
                    //Toast.makeText(mActivity, "已经没有更多数据", Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(mActivity, "已经没有更多数据", Toast.LENGTH_SHORT).show();
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
            if (msg.what == 2) {

            }
        }
    };
    private CommHotAdapter mCommHotAdapter;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private Toolbar mToolbar;
    private AlertDialog mAlertDialog;


    public static MyPostListFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        MyPostListFragment fragment = new MyPostListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.activity_collect_list, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCommHotAdapter = new CommHotAdapter(5,mActivity, mPostAdapterList);
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
                        intent.putExtra("re_uid", mPostAdapterList.get(position).getUid());
                        startActivity(new Intent(intent));
                    }
                }
            }

        });

        mCommHotAdapter.setOnItemLongClickLitener(new CommHotAdapter.OnItemLongClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                View view1 = LayoutInflater.from(mActivity).inflate(R.layout.dialog_delete_post, null);
                TextView tv_delete = (TextView) view1.findViewById(R.id.tv_delete);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog.dismiss();
                        deletePost(mPostAdapterList.get(position - 1).getId());
                        mPostAdapterList.remove(position - 1);
                        Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
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
                //mAlertDialog.setCancelable(false);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //   Log.i("Main", "用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                        Glide.with(MyPostListFragment.this).pauseRequests();
                        //刷新
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //  Log.i("Main", "视图已经停止滑动");
                        Glide.with(MyPostListFragment.this).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //  Log.i("Main", "手指没有离开屏幕，视图正在滑动");
                        Glide.with(MyPostListFragment.this).resumeRequests();
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
        });
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && IS_FIRST_INIT_DATA) {
            initData(PAGENUMBER);
            IS_FIRST_INIT_DATA = false;
        } else {
            //不可见时不执行操作
        }
    }
    /**
     * 删除帖子
     *
     * @param tid
     */
    private void deletePost(final String tid) {
        OkHttpUtils.post()//
                .addParams("tid", tid)
                .addParams("uid", UserUtils.getUserId())
                .url(XingYuInterface.DELETE_MY_POSTS)
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
     * 初始化数据
     */
    public void initData(int PAGENUMBER) {
        OkHttpUtils.post()//
                .addParams("page", String.valueOf(PAGENUMBER))
                .addParams("uid", UserUtils.getUserId())
                .addParams("type", "3")
                .addParams("attribute", "1")
                .addParams("fid","1")
                .addParams("keyword", "1")
                .addParams("bid", "1")
                .url(XingYuInterface.GET_POSTS_LIST)
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

    @Override
    public void onResume() {
        super.onResume();
        DCPage.onEntry("MyPostListFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        DCPage.onExit("MyPostListFragment");
    }
}
