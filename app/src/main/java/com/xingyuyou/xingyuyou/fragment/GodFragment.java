package com.xingyuyou.xingyuyou.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dataeye.sdk.api.app.channel.DCPage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.GodDeatilActivity;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.OtherPageActivity;
import com.xingyuyou.xingyuyou.activity.UserPageActivity;
import com.xingyuyou.xingyuyou.adapter.GodFragmentAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.community.PostListBeanTest;
import com.xingyuyou.xingyuyou.bean.god.GodBean;
import com.xingyuyou.xingyuyou.weight.infiniteViewPager.GalleryTransformer;
import com.xingyuyou.xingyuyou.weight.infiniteViewPager.ImagePagerAdapter;
import com.xingyuyou.xingyuyou.weight.infiniteViewPager.InfinitePagerAdapter;
import com.xingyuyou.xingyuyou.weight.infiniteViewPager.InfiniteViewPager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.xingyuyou.xingyuyou.R.id.recyclerView;


/**
 * Created by Administrator on 2016/6/28.
 */
public class GodFragment extends BaseFragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<PostListBeanTest> mDatas = new ArrayList<>();
    private GodFragmentAdapter mAdapter;
    private int  PAGENUMBER = 1;
    private List<PostListBeanTest> mGodPostList=new ArrayList<>();
    private List<GodBean> mGodList=new ArrayList<>();
    private List<GodBean> mGodListDatas=new ArrayList<>();
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    String string = jo.getString("status");
                    if (string.equals("1")){
                        JSONArray ja = jo.getJSONArray("data");
                        //Log.e("hot", "解析数据："+  ja.toString());
                        Gson gson = new Gson();
                        mGodPostList = gson.fromJson(ja.toString(),
                                new TypeToken<List<PostListBeanTest>>() {
                                }.getType());
                        mDatas.addAll(mGodPostList);
                        if (mDatas.size()<=20){
                            mPbNodata.setVisibility(View.GONE);
                            mTvNodata.setText("已经没有更多数据");
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 2) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    String string = jo.getString("status");
                    if (string.equals("1")){
                        JSONArray ja = jo.getJSONArray("data");
                        Gson gson = new Gson();
                        mGodList = gson.fromJson(ja.toString(),
                                new TypeToken<List<GodBean>>() {
                                }.getType());
                        mGodListDatas.addAll(mGodList);
                        mImagePagerAdapter = new ImagePagerAdapter(mGodListDatas,mActivity);
                        mInfinitePagerAdapter = new InfinitePagerAdapter(mImagePagerAdapter);
                        mViewPager.setAdapter(mInfinitePagerAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private ImagePagerAdapter mImagePagerAdapter;
    private InfinitePagerAdapter mInfinitePagerAdapter;
    private InfiniteViewPager mViewPager;
    private LinearLayoutManager mLinearLayoutManager;
    boolean isLoading = false;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;

    public static GodFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        GodFragment fragment = new GodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化数据
     */
    public void initData(int PAGENUMBER) {
        OkHttpUtils.post()//
                .addParams("page", String.valueOf(PAGENUMBER))
                .addParams("uid", UserUtils.getUserId())
                .addParams("type", "6")
                .addParams("attribute", "1")
                .addParams("fid","0")
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
                        mHandler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }
    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        initData(1);
        OkHttpUtils.post()//
                .url(XingYuInterface.GET_GODLIST)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(2, response).sendToTarget();
                    }
                });
    }
    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_god, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout);
        initSwipeRefreshLayout();
        mRecyclerView = (RecyclerView)view.findViewById(recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new GodFragmentAdapter(mActivity, mDatas);

        //点击头像跳转
        mAdapter.imageSetOnclick(new GodFragmentAdapter.ImageInterface() {
            @Override
            public void onclick(int position) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(getActivity(), LoginActivity.class);
                    return;
                }else {
                    if (UserUtils.getUserId().equals(mDatas.get(position-1).getUid())){
                        startActivity(new Intent(getActivity(), UserPageActivity.class));
                    }else {
                        Intent intent=new Intent(getActivity(), OtherPageActivity.class);
                        intent.putExtra("re_uid",mDatas.get(position-1).getUid());
                        startActivity(intent);
                    }
                }
            }
        });
        //神轮播图
        View view2 = View.inflate(mActivity, R.layout.part_god_fragment_header, null);
        mViewPager = (InfiniteViewPager) view2.findViewById(R.id.id_viewpager);
        mViewPager.setOffscreenPageLimit(3);

        mViewPager.setPageTransformer(true, new GalleryTransformer());
        mViewPager.setOnItemClickListener(new InfiniteViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(mActivity, GodDeatilActivity.class)
                        .putExtra("gid",mGodListDatas.get(position).getId()));
            }
        });
        mAdapter.setHeaderView(view2);
        View loadingData = View.inflate(mActivity, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        mAdapter.setFooterView(loadingData);
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (lastVisibleItemPosition + 1 == mAdapter.getItemCount() - 5) {
                        //  Log.e("search", "loading executed");
                        if (!isLoading) {
                            isLoading = true;
                            mHandler.postDelayed(new Runnable() {
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateStatus();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        } else {
            //不可见时不执行操作
        }
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                initData();
                mSwipeRefreshLayout.setRefreshing(true);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }
    private void updateStatus() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("GodPostUpdateCollectStatus");
        intentFilter.addAction("GodPostUpdateZanStatus");
        BroadcastReceiver br2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("GodPostUpdateCollectStatus")) {
                    if (intent.getIntExtra("cancelCollect", 0) == 0) {
                        mDatas.get(intent.getIntExtra("position", 0) - 1).setCollect_status(0);
                        mDatas.get(intent.getIntExtra("position", 0) - 1)
                                .setPosts_collect(String.valueOf((Integer.parseInt(
                                        mDatas.get(intent.getIntExtra("position", 0) - 1).getPosts_collect())) - 1));
                    } else {
                        mDatas.get(intent.getIntExtra("position", 0) - 1).setCollect_status(1);
                        mDatas.get(intent.getIntExtra("position", 0) - 1)
                                .setPosts_collect(String.valueOf((Integer.parseInt(
                                        mDatas.get(intent.getIntExtra("position", 0) - 1).getPosts_collect())) +1));
                    }
                }
                if (intent.getAction().equals("GodPostUpdateZanStatus")) {
                    if (intent.getIntExtra("cancelZan", 0) == 0) {
                        mDatas.get(intent.getIntExtra("position", 0) - 1).setLaud_status(0);
                        mDatas.get(intent.getIntExtra("position", 0) - 1)
                                .setPosts_laud(String.valueOf((Integer.parseInt(
                                        mDatas.get(intent.getIntExtra("position", 0) - 1).getPosts_laud())) - 1));
                    } else {
                        mDatas.get(intent.getIntExtra("position", 0) - 1).setLaud_status(1);
                        mDatas.get(intent.getIntExtra("position", 0) - 1)
                                .setPosts_laud(String.valueOf((Integer.parseInt(
                                        mDatas.get(intent.getIntExtra("position", 0) - 1).getPosts_laud())) +1));
                    }
                }
                mAdapter.notifyItemChanged(intent.getIntExtra("position", 0));
            }

        };
        localBroadcastManager.registerReceiver(br2, intentFilter);
    }
    @Override
    public void onResume() {
        super.onResume();
        DCPage.onEntry("GodFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        DCPage.onExit("GodFragment");
    }
}
