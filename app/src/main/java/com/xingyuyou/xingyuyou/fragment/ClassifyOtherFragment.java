package com.xingyuyou.xingyuyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.OtherPageActivity;
import com.xingyuyou.xingyuyou.activity.UserPageActivity;
import com.xingyuyou.xingyuyou.adapter.CommSortAdapterTwo;
import com.xingyuyou.xingyuyou.bean.PostListBEan;
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

import static com.xingyuyou.xingyuyou.App.mContext;

/**
 * Created by Administrator on 2017/10/31.
 */

public class ClassifyOtherFragment extends Fragment {

    private PostListBEan postListBEan;
    private View view;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private List<PostListBeanTest> mPostList = new ArrayList();
    private List<PostTopAndWellBean> mPostTopWellList = new ArrayList();
    private static boolean CLEAR_DATA = false;
    private List mPostAdapterList = new ArrayList();
    boolean isLoading = false;
    private CommSortAdapterTwo mCommHotAdapter;
    private int PAGENUMBER = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {

                if (msg.obj.toString().contains("\"data\":null")) {
                    //Toast.makeText(PostClassListActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
                    return;
                }
                String response = (String) msg.obj;
                Gson gson = new Gson();
               // postListBEan = gson.fromJson(response, PostListBEan.class);
 /*               if (postListBEan.getRelation() == 0) {
                    tv_class_name.setText("+加入");
                } else if (postListBEan.getRelation() == 1) {
                    setBooder();

                }*/
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
               //     String mPosts_num = jo.getString("posts_num");
                //    StringBuilder stringBuilder = new StringBuilder();
            /*        mTvPostNum.setText("人数：" + jo.getString("follow_count"));
                    stringBuilder.append("帖子：");*/
                //    stringBuilder.append(mPosts_num);
                 /*   tvPostDes.setText(stringBuilder);*/


                    JSONArray ja = jo.getJSONArray("data");
                    mPostList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostListBeanTest>>() {
                            }.getType());

                    if(mPostList.size()<20){
                        mPbNodata.setVisibility(View.GONE);
                        mTvNodata.setText("已经没有更多数据");
                    }

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
                    mPostAdapterList.addAll(mPostList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (mCommHotAdapter != null)
                    mCommHotAdapter.notifyDataSetChanged();
            }
        }
    };
    String list_id;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_classifyallfragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData(1);
    }

    /**
     * 初始化数据
     */
    public void initData(int PAGENUMBER) {
        final Bundle bundle = getArguments();
        OkHttpUtils.post()//
                .addParams("page", String.valueOf(PAGENUMBER))
                .addParams("fid", bundle.getString("uid"))
                .addParams("uid", UserUtils.getUserId())
                .addParams("menu_id",bundle.getString("menu_id"))
                .url("http://xingyuyou.com/app.php/UserAdministration/get_forum_theme_relist")
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                  //    Log.d("qqqqq", bundle.getString("uid")+"______"+response.toString());
                        handler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }
    private void initView() {
        //底部布局
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCommHotAdapter = new CommSortAdapterTwo(getActivity(), mPostAdapterList);
        mCommHotAdapter.setFid(getArguments().getString("uid"));
        View headerVieww = View.inflate(getActivity(), R.layout.header_sort_post_listt, null);
        View loadingData = View.inflate(getActivity(), R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        mCommHotAdapter.setHeaderView(headerVieww);
        mCommHotAdapter.setFooterView(loadingData);
        mRecyclerView.setAdapter(mCommHotAdapter);
        //点击头像跳转
        mCommHotAdapter.imageSetOnclick(new CommSortAdapterTwo.ImageInterface() {
            @Override
            public void onclick(int position) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(getActivity(), LoginActivity.class);
                    return;
                } else {
                    if (UserUtils.getUserId().equals(((PostListBeanTest) mPostAdapterList.get(position - 1)).getUid())) {
                        startActivity(new Intent(getActivity(), UserPageActivity.class));
                    } else {
                        Intent intent = new Intent(getActivity(), OtherPageActivity.class);
                        intent.putExtra("re_uid", (((PostListBeanTest) mPostAdapterList.get(position - 1)).getUid()));
                        startActivity(new Intent(intent));
                    }
                }
            }
        });
        //滑动加载更多
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
