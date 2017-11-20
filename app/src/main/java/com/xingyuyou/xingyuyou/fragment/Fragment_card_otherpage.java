package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.CardOtherAdapter;
import com.xingyuyou.xingyuyou.bean.community.PostListBeanTest;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.xingyuyou.xingyuyou.App.mContext;

/**
 * Created by Administrator on 2017/7/5.
 */

public class Fragment_card_otherpage extends Fragment {

    private RecyclerView mRecyclerView;
    private int PAGENUMBER = 1;
    private List<PostListBeanTest> mPostList = new ArrayList();
    private List<PostListBeanTest> mPostAdapterList = new ArrayList();
    boolean isLoading = false;
    private CardOtherAdapter mCommHotAdapter;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private LinearLayoutManager mLinearLayoutManager;
    private View view;
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
                    //   Log.e("post", "解析数据："+  ja.toString());
                    Gson gson = new Gson();
                    mPostList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostListBeanTest>>() {
                            }.getType());
                    mPostAdapterList.addAll(mPostList);

                    if (mPostList.size() < 20) {
                       // Toast.makeText(getActivity(), "已经没有更多数据", Toast.LENGTH_SHORT).show();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_collect_listother, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData(1);
    }
        //初始化数据

    private void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCommHotAdapter = new CardOtherAdapter(5,getActivity(), mPostAdapterList);
        View loadingData = View.inflate(getActivity(), R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        TextView textView = new TextView(getActivity());
        textView.setText("");
        mCommHotAdapter.setHeaderView(textView);
        mCommHotAdapter.setFooterView(loadingData);
        mRecyclerView.setAdapter(mCommHotAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //   Log.i("Main", "用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                        Glide.with(mContext).pauseRequests();
                        //刷新
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //  Log.i("Main", "视图已经停止滑动");
                        Glide.with(mContext).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //  Log.i("Main", "手指没有离开屏幕，视图正在滑动");
                        Glide.with(mContext).resumeRequests();
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
    }

    /**
     * 初始化数据
     */
    public void initData(int PAGENUMBER) {
        Bundle bundle = getArguments();
        OkHttpUtils.post()//
                .addParams("page", String.valueOf(PAGENUMBER))
                .addParams("uid", bundle.getString("uid"))
                .addParams("re_uid", UserUtils.getUserId())
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
                        Log.d("wwwww",response.toString());
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}

