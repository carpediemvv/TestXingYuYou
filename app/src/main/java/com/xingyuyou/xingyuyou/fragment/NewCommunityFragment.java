package com.xingyuyou.xingyuyou.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.GetCommunityList;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.RetrofitServiceManager;
import com.xingyuyou.xingyuyou.activity.CommunityActivity;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.PostClassListActivity;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.theme.CommunityListBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class NewCommunityFragment extends BaseFragment {
    private String TAG = "NewCommunityFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean IS_FIRST_INIT_DATA = true;
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private ArrayList<CommunityListBean.DataBean> mStrings = new ArrayList<>();
    private Activity mActivity;
    private ItemCommuViewHolder mItemCommuViewHolder;
    private MultiItemTypeAdapter<CommunityListBean.DataBean> mItemTypeAdapter;
    private ArrayList<CommunityListBean.DataBean.ForumClassBean> mHotData;
    private SwipeRefreshLayout mRefreshLayout;
    private static boolean CLEAR_DATA = false;
    public NewCommunityFragment() {
        // Required empty public constructor
    }


    public static NewCommunityFragment newInstance(String param1, String param2) {
        NewCommunityFragment fragment = new NewCommunityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mActivity = getActivity();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && IS_FIRST_INIT_DATA) {
            initData();
            IS_FIRST_INIT_DATA = false;
        }
    }

    public void initData() {
        RetrofitServiceManager.getInstance()
                .create(GetCommunityList.class)
                .getCommunityList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommunityListBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull CommunityListBean userInfo) {

                        if (CLEAR_DATA) {
                            mStrings.clear();
                        }
                        setValues(userInfo);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mItemTypeAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void setValues(CommunityListBean beanInfo) {
        mHotData = new ArrayList<CommunityListBean.DataBean.ForumClassBean>();
        for (int i = 0; i < beanInfo.getData().size(); i++) {
            for (int j = 0; j < beanInfo.getData().get(i).getForum_class().size(); j++) {
                if (beanInfo.getData().get(i).getForum_class().get(j).getHot_forum_class().equals("1")) {
                    mHotData.add(beanInfo.getData().get(i).getForum_class().get(j));
                }
            }
        }
        mStrings.addAll(beanInfo.getData());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_community, container, false);
    }

    @Override
    protected View initView() {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        initSwipeRefreshLayout();
        ImageView iv_commu_search =(ImageView) view.findViewById(R.id.iv_commu_search);
        iv_commu_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否登陆
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(mActivity, LoginActivity.class);
                    return;
                }
                startActivity(new Intent(mActivity,CommunityActivity.class));
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mItemTypeAdapter = new MultiItemTypeAdapter<>(getActivity(), mStrings);
        mItemTypeAdapter.addItemViewDelegate(new CommunityDelegate());

        mRecyclerView.setAdapter(mItemTypeAdapter);
    }

    private void initSwipeRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CLEAR_DATA = true;
                initData();
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }
    private class CommunityDelegate implements ItemViewDelegate<CommunityListBean.DataBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_new_commu;
        }

        @Override
        public boolean isForViewType(CommunityListBean.DataBean item, int position) {
            return true;
        }

        @Override
        public void convert(final ViewHolder holder, final CommunityListBean.DataBean s, final int position) {
            if (s.getForum_class_id().equals("1")) {
                holder.setText(R.id.tv_theme_circle_item_title, "次元");
                holder.setImageResource(R.id.iv_theme_circle_item_icon,R.drawable.ic_action_ciyuan);
            }
            if (s.getForum_class_id().equals("2")) {
                holder.setText(R.id.tv_theme_circle_item_title, "娱乐");
                holder.setImageResource(R.id.iv_theme_circle_item_icon,R.drawable.ic_action_yule);
            }
            if (s.getForum_class_id().equals("3")) {
                holder.setText(R.id.tv_theme_circle_item_title, "生活");
                holder.setImageResource(R.id.iv_theme_circle_item_icon,R.drawable.ic_action_shenghuo);
            }

            holder.setOnClickListener(R.id.iv_commu_hot, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.startActivity(new Intent(mActivity, PostClassListActivity.class)
                            .putExtra("list_id",mHotData.get(position).getId())
                            .putExtra("getClass_head_image",mHotData.get(holder.getAdapterPosition()).getClass_head_image())
                            .putExtra("getClass_virtual_image",mHotData.get(holder.getAdapterPosition()).getClass_virtual_image())
                            .putExtra("attribute","1"));
                }
            });
            for (int i = 0; i < s.getForum_class().size(); i++) {
                if (s.getForum_class().get(i).getHot_forum_class().equals("1")) {
                    Glide.with(NewCommunityFragment.this)
                            .load(s.getForum_class().get(i).getClass_image())
                            .centerCrop()
                            .into((ImageView) holder.getConvertView().findViewById(R.id.iv_commu_hot));
                    s.getForum_class().remove(i);
                }
            }

            RecyclerView itemRecyclerView = (RecyclerView) holder.getConvertView().findViewById(R.id.item_recycler_view);
            itemRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            mItemCommuViewHolder = new ItemCommuViewHolder(s.getForum_class());
            itemRecyclerView.setTag(mItemCommuViewHolder);
            itemRecyclerView.setAdapter(mItemCommuViewHolder);
        }
    }

    private class ItemCommuViewHolder extends RecyclerView.Adapter {
        private List<CommunityListBean.DataBean.ForumClassBean> mBeenList;

        private ItemCommuViewHolder(List<CommunityListBean.DataBean.ForumClassBean> themeBeenList) {
            mBeenList = themeBeenList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(getActivity()).inflate(R.layout.item_new_commu_item, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((ItemViewHolder) holder).tv_item_theme_name.setText(mBeenList.get(position).getClass_name());
            ((ItemViewHolder) holder).tv_item_theme_concern_number.setText(mBeenList.get(position).getRecommend_number() + "人关注");
            Glide.with(NewCommunityFragment.this).load(mBeenList.get(position).getClass_image())
                    .into(((ItemViewHolder) holder).mItemImage);
            ((ItemViewHolder) holder).mItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.startActivity(new Intent(mActivity, PostClassListActivity.class)
                            .putExtra("getClass_head_image",mBeenList.get(holder.getAdapterPosition()).getClass_head_image())
                            .putExtra("getClass_virtual_image",mBeenList.get(holder.getAdapterPosition()).getClass_virtual_image())
                            .putExtra("attribute","1")
                            .putExtra("list_id",mBeenList.get(holder.getAdapterPosition()).getId()));
                }
            });

        }

        @Override
        public int getItemCount() {
            return mBeenList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            private ImageView mItemImage;
            private TextView tv_item_theme_name;
            private TextView tv_item_theme_concern_number;

            public ItemViewHolder(View itemView) {
                super(itemView);
                mItemImage = (ImageView) itemView.findViewById(R.id.iv_theme_circle_item_icon);
                tv_item_theme_name = (TextView) itemView.findViewById(R.id.tv_item_theme_name);
                tv_item_theme_concern_number = (TextView) itemView.findViewById(R.id.tv_item_theme_concern_number);
            }
        }

    }
}
