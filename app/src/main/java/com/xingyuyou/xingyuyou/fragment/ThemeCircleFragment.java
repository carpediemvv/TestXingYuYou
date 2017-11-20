package com.xingyuyou.xingyuyou.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.AddThemeCircle;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.ChangeThemeCircle;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.GetThemeRecom;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.ThemeCircleList;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.RetrofitServiceManager;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.activity.CommunityActivity;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.PostClassListActivity;
import com.xingyuyou.xingyuyou.activity.ThemeCircleAllDetailActivity;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.theme.NodataBean;
import com.xingyuyou.xingyuyou.bean.theme.ThemeCircleItemData;
import com.xingyuyou.xingyuyou.bean.theme.ThemeCircleListBean;
import com.xingyuyou.xingyuyou.bean.theme.ThemeRecom;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ThemeCircleFragment extends BaseFragment {
    private static boolean CLEAR_DATA = false;
    private String TAG = "ThemeCircleFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private RecyclerView mRecyclerView;
    private ArrayList<ThemeCircleListBean.DataBean> mStrings = new ArrayList<>();
    private HeaderAndFooterWrapper<Object> mHeaderAndFooterWrapper;
    private Activity mActivity;
    private ImageView mIv_theme_photo;
    private ImageView mIv_theme_photo2;
    private ImageView mIv_theme_photo3;
    private TextView mTv_theme_name;
    private TextView mTv_theme_name2;
    private TextView mTv_theme_name3;
    private TextView mTv_theme_concern_number;
    private TextView mTv_theme_concern_number2;
    private TextView mTv_theme_concern_number3;
    private Button mBt_add_theme_circle;
    private Button mBt_add_theme_circle2;
    private Button mBt_add_theme_circle3;
    private ItemThemeViewHolder mItemThemeViewHolder;
    private SwipeRefreshLayout mRefreshLayout;

    public ThemeCircleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThemeCircleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThemeCircleFragment newInstance(String param1, String param2) {
        ThemeCircleFragment fragment = new ThemeCircleFragment();
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
        initData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG, "initData2: ");
        if (isVisibleToUser) {
            //initData();
        }
    }

    public void initData() {
        String UserId;
        if( !UserUtils.logined()){
            UserId="105";
        }else {
            UserId=UserUtils.getUserId();
        }
        Observable.merge(RetrofitServiceManager.getInstance()
                .create(GetThemeRecom.class)
                .getThemeRecomData(UserId), RetrofitServiceManager.getInstance()
                .create(ThemeCircleList.class)
                .getThemeCircleList()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object obj) {
                        if (obj instanceof ThemeRecom) {
                            setValues((ThemeRecom) obj);
                        }
                        if (obj instanceof ThemeCircleListBean) {
                            if (CLEAR_DATA) {
                                mStrings.clear();
                            }
                            mStrings.addAll(((ThemeCircleListBean) obj).getData());
                            mHeaderAndFooterWrapper.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    //推荐信息赋值
    private void setValues(final ThemeRecom themeRecom) {
        Glide.with(this).load(themeRecom.getData().get(0).getClass_image()).transform(new GlideCircleTransform(mActivity)).into(mIv_theme_photo);
        mTv_theme_name.setText(themeRecom.getData().get(0).getClass_name());
        mTv_theme_concern_number.setText(themeRecom.getData().get(0).getRecommend_number() + "人关注");
        Glide.with(this).load(themeRecom.getData().get(1).getClass_image()).transform(new GlideCircleTransform(mActivity)).into(mIv_theme_photo2);
        mTv_theme_name2.setText(themeRecom.getData().get(1).getClass_name());
        mTv_theme_concern_number2.setText(themeRecom.getData().get(1).getRecommend_number() + "人关注");
        Glide.with(this).load(themeRecom.getData().get(2).getClass_image()).transform(new GlideCircleTransform(mActivity)).into(mIv_theme_photo3);
        mTv_theme_name3.setText(themeRecom.getData().get(2).getClass_name());
        mTv_theme_concern_number3.setText(themeRecom.getData().get(2).getRecommend_number() + "人关注");
        mIv_theme_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivity(new Intent(mActivity, PostClassListActivity.class)
                        .putExtra("list_id", themeRecom.getData().get(0).getId())
                        .putExtra("getClass_head_image", themeRecom.getData().get(0).getClass_head_image())
                        .putExtra("getClass_virtual_image", themeRecom.getData().get(0).getClass_virtual_image())
                        .putExtra("attribute", "2"));
            }
        });
        mIv_theme_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivity(new Intent(mActivity, PostClassListActivity.class)
                        .putExtra("list_id", themeRecom.getData().get(1).getId())
                        .putExtra("getClass_head_image", themeRecom.getData().get(1).getClass_head_image())
                        .putExtra("getClass_virtual_image", themeRecom.getData().get(1).getClass_virtual_image())
                        .putExtra("attribute", "2"));
            }
        });
        mIv_theme_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivity(new Intent(mActivity, PostClassListActivity.class)
                        .putExtra("list_id", themeRecom.getData().get(2).getId())
                        .putExtra("getClass_head_image", themeRecom.getData().get(2).getClass_head_image())
                        .putExtra("getClass_virtual_image", themeRecom.getData().get(2).getClass_virtual_image())
                        .putExtra("attribute", "2"));
            }
        });
        if (1 == themeRecom.getData().get(0).getRecommend_status()) {
            mBt_add_theme_circle.setBackgroundResource(R.drawable.bg_theme_circle_is_add);
            mBt_add_theme_circle.setText("已加入");
            mBt_add_theme_circle.setTextColor(getResources().getColor(R.color.white));
        } else {
            mBt_add_theme_circle.setBackgroundResource(R.drawable.bg_theme_circle_add);
        }
        if (1 == themeRecom.getData().get(1).getRecommend_status()) {
            mBt_add_theme_circle2.setBackgroundResource(R.drawable.bg_theme_circle_is_add);
            mBt_add_theme_circle2.setText("已加入");
            mBt_add_theme_circle2.setTextColor(getResources().getColor(R.color.white));

        } else {
            mBt_add_theme_circle2.setBackgroundResource(R.drawable.bg_theme_circle_add);
        }
        if (1 == themeRecom.getData().get(2).getRecommend_status()) {
            mBt_add_theme_circle3.setBackgroundResource(R.drawable.bg_theme_circle_is_add);
            mBt_add_theme_circle3.setText("已加入");
            mBt_add_theme_circle3.setTextColor(getResources().getColor(R.color.white));

        } else {
            mBt_add_theme_circle3.setBackgroundResource(R.drawable.bg_theme_circle_add);
        }
        mBt_add_theme_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 == themeRecom.getData().get(0).getRecommend_status()) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(mActivity);
                    normalDialog.setTitle("你真的想好要退出了吗?");
                    normalDialog.setPositiveButton("我确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setAddThemeCircle(themeRecom.getData().get(0).getId(), "1");
                                    themeRecom.getData().get(0).setRecommend_status(0);
                                    mBt_add_theme_circle.setBackgroundResource(R.drawable.bg_theme_circle_add);
                                    mBt_add_theme_circle.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    mBt_add_theme_circle.setText("+加入");
                                    Toast.makeText(mActivity, "已取消加入（*>.<*）~ @", Toast.LENGTH_SHORT).show();
                                }
                            });
                    normalDialog.setNegativeButton("再想想",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    normalDialog.show();
                } else {
                    setAddThemeCircle(themeRecom.getData().get(0).getId(), "0");
                    themeRecom.getData().get(0).setRecommend_status(1);
                    mBt_add_theme_circle.setBackgroundResource(R.drawable.bg_theme_circle_is_add);
                    mBt_add_theme_circle.setTextColor(getResources().getColor(R.color.white));
                    mBt_add_theme_circle.setText("已加入");
                }

            }
        });
        mBt_add_theme_circle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 == themeRecom.getData().get(1).getRecommend_status()) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(mActivity);
                    normalDialog.setTitle("你真的想好要退出了吗?");
                    normalDialog.setPositiveButton("我确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setAddThemeCircle(themeRecom.getData().get(1).getId(), "1");
                                    themeRecom.getData().get(1).setRecommend_status(0);
                                    mBt_add_theme_circle2.setBackgroundResource(R.drawable.bg_theme_circle_add);
                                    mBt_add_theme_circle2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    mBt_add_theme_circle2.setText("+加入");
                                    Toast.makeText(mActivity, "已取消加入（*>.<*）~ @", Toast.LENGTH_SHORT).show();
                                }
                            });
                    normalDialog.setNegativeButton("再想想",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    normalDialog.show();

                } else {
                    setAddThemeCircle(themeRecom.getData().get(1).getId(), "0");
                    themeRecom.getData().get(1).setRecommend_status(1);
                    mBt_add_theme_circle2.setBackgroundResource(R.drawable.bg_theme_circle_is_add);
                    mBt_add_theme_circle2.setTextColor(getResources().getColor(R.color.white));
                    mBt_add_theme_circle2.setText("已加入");
                }
            }
        });
        mBt_add_theme_circle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 == themeRecom.getData().get(2).getRecommend_status()) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(mActivity);
                    normalDialog.setTitle("你真的想好要退出了吗?");
                    normalDialog.setPositiveButton("我确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setAddThemeCircle(themeRecom.getData().get(2).getId(), "1");
                                    themeRecom.getData().get(2).setRecommend_status(0);
                                    mBt_add_theme_circle3.setBackgroundResource(R.drawable.bg_theme_circle_add);
                                    mBt_add_theme_circle3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    mBt_add_theme_circle3.setText("+加入");
                                    Toast.makeText(mActivity, "已取消加入（*>.<*）~ @", Toast.LENGTH_SHORT).show();
                                }
                            });
                    normalDialog.setNegativeButton("再想想",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    normalDialog.show();
                } else {
                    setAddThemeCircle(themeRecom.getData().get(2).getId(), "0");
                    themeRecom.getData().get(2).setRecommend_status(1);
                    mBt_add_theme_circle3.setBackgroundResource(R.drawable.bg_theme_circle_is_add);
                    mBt_add_theme_circle3.setTextColor(getResources().getColor(R.color.white));
                    mBt_add_theme_circle3.setText("已加入");
                }
            }
        });
    }

    private void setAddThemeCircle(String id, String status) {

        if (!UserUtils.logined()) {
            IntentUtils.startActivity(mActivity, LoginActivity.class);
            return;
        }
        RetrofitServiceManager.getInstance()
                .create(AddThemeCircle.class)
                .addThemeCircle(UserUtils.getUserId(), id, status, "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NodataBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull NodataBean nodataBean) {
                        Log.e(TAG, "onNext: " + nodataBean.getErrorinfo() + nodataBean.getStatus());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_theme_circle, container, false);
    }

    @Override
    protected View initView() {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //刷新更新数据
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        initSwipeRefreshLayout();
        ImageView iv_theme_circle_search = (ImageView) view.findViewById(R.id.iv_theme_circle_search);
        iv_theme_circle_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否登陆
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(mActivity, LoginActivity.class);
                    return;
                }
                startActivity(new Intent(mActivity, CommunityActivity.class));
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        MultiItemTypeAdapter<ThemeCircleListBean.DataBean> itemTypeAdapter = new MultiItemTypeAdapter<>(getActivity(), mStrings);
        itemTypeAdapter.addItemViewDelegate(new ThemeCircleDelegate());

        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper<>(itemTypeAdapter);
        View header = View.inflate(getActivity(), R.layout.header_theme_circle, null);
        initHeaderView(header);

        mHeaderAndFooterWrapper.addHeaderView(header);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
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

    private void initHeaderView(View header) {
        mIv_theme_photo = (ImageView) header.findViewById(R.id.iv_theme_photo);
        mIv_theme_photo2 = (ImageView) header.findViewById(R.id.iv_theme_photo2);
        mIv_theme_photo3 = (ImageView) header.findViewById(R.id.iv_theme_photo3);
        mTv_theme_name = (TextView) header.findViewById(R.id.tv_theme_name);
        mTv_theme_name2 = (TextView) header.findViewById(R.id.tv_theme_name2);
        mTv_theme_name3 = (TextView) header.findViewById(R.id.tv_theme_name3);
        mTv_theme_concern_number = (TextView) header.findViewById(R.id.tv_theme_concern_number);
        mTv_theme_concern_number2 = (TextView) header.findViewById(R.id.tv_theme_concern_number2);
        mTv_theme_concern_number3 = (TextView) header.findViewById(R.id.tv_theme_concern_number3);
        mBt_add_theme_circle = (Button) header.findViewById(R.id.bt_add_theme_circle);
        mBt_add_theme_circle2 = (Button) header.findViewById(R.id.bt_add_theme_circle2);
        mBt_add_theme_circle3 = (Button) header.findViewById(R.id.bt_add_theme_circle3);
    }


    private class ThemeCircleDelegate implements ItemViewDelegate<ThemeCircleListBean.DataBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_theme_circle;
        }

        @Override
        public boolean isForViewType(ThemeCircleListBean.DataBean item, int position) {
            return true;
        }

        @Override
        public void convert(final ViewHolder holder, final ThemeCircleListBean.DataBean s, final int position) {
            //换一换
            holder.setOnClickListener(R.id.ll_change_theme_item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeItemData(s.getId(), holder);
                }
            });
            holder.setText(R.id.tv_theme_circle_item_title, s.getTheme_class_name());
            Glide.with(ThemeCircleFragment.this)
                    .load(s.getTheme_class_image())
                    .centerCrop()
                    .into((ImageView) holder.getConvertView().findViewById(R.id.iv_theme_circle_item_icon));
            RecyclerView itemRecyclerView = (RecyclerView) holder.getConvertView().findViewById(R.id.item_recycler_view);
            itemRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            mItemThemeViewHolder = new ItemThemeViewHolder(s.getTheme());
            itemRecyclerView.setTag(mItemThemeViewHolder);
            itemRecyclerView.setAdapter(mItemThemeViewHolder);
            //查看更多
            holder.setOnClickListener(R.id.bt_all_theme_circle, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.startActivity(new Intent(mActivity, ThemeCircleAllDetailActivity.class)
                            .putExtra("type_id", s.getId())
                            .putExtra("theme_name", s.getTheme_class_name()));
                }
            });
        }
    }

    private class ItemThemeViewHolder extends RecyclerView.Adapter {
        private List<ThemeCircleListBean.DataBean.ThemeBean> mThemeBeenList;

        private ItemThemeViewHolder(List<ThemeCircleListBean.DataBean.ThemeBean> themeBeenList) {
            mThemeBeenList = themeBeenList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(getActivity()).inflate(R.layout.item_theme_circle_item, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((ItemViewHolder) holder).tv_item_theme_name.setText(mThemeBeenList.get(position).getClass_name());
            ((ItemViewHolder) holder).tv_item_theme_concern_number.setText(mThemeBeenList.get(position).getRecommend_number() + "人关注");
            Glide.with(ThemeCircleFragment.this).load(mThemeBeenList.get(position).getClass_image())
                    .into(((ItemViewHolder) holder).mItemImage);
            ((ItemViewHolder) holder).mItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.startActivity(new Intent(mActivity, PostClassListActivity.class)
                            .putExtra("list_id", mThemeBeenList.get(holder.getAdapterPosition()).getId())
                            .putExtra("getClass_head_image", mThemeBeenList.get(holder.getAdapterPosition()).getClass_head_image())
                            .putExtra("getClass_virtual_image", mThemeBeenList.get(holder.getAdapterPosition()).getClass_virtual_image())
                            .putExtra("attribute", "2"));

                }
            });
        }

        @Override
        public int getItemCount() {
            return mThemeBeenList.size();
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

    private void changeItemData(String id, final ViewHolder holder) {
        RetrofitServiceManager.getInstance()
                .create(ChangeThemeCircle.class)
                .changeItemThemeCircle(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ThemeCircleItemData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ThemeCircleItemData bean) {
                        Log.e(TAG, "onNext: " + bean.toString());
                        ((ItemThemeViewHolder) holder.getConvertView()
                                .findViewById(R.id.item_recycler_view).getTag())
                                .mThemeBeenList = bean.getData();
                        ((ItemThemeViewHolder) holder.getConvertView()
                                .findViewById(R.id.item_recycler_view).getTag()).notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
