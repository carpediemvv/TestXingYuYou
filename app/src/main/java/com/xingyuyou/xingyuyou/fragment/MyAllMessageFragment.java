package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.GetMyAllMessage;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.RetrofitServiceManager;
import com.xingyuyou.xingyuyou.adapter.MyAllMessageAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.user.MyAllMessageBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MyAllMessageFragment extends BaseFragment {
    private boolean IS_FIRST_INIT_DATA = true;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int PAGENUMBER = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MyAllMessageAdapter mMyAllMessageAdapter;
    private List<MyAllMessageBean.DataBean> mMyAllMessageList = new ArrayList<>();
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private boolean isLoading = false;

    public MyAllMessageFragment() {
        // Required empty public constructor

    }

    public static MyAllMessageFragment newInstance(String param1, String param2) {
        MyAllMessageFragment fragment = new MyAllMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void initData(int PAGENUMBER) {
        RetrofitServiceManager.getInstance()
                .create(GetMyAllMessage.class)
                .getMyAllMessage(UserUtils.getUserId(), String.valueOf(PAGENUMBER))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyAllMessageBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull MyAllMessageBean myAllMessageBean) {
                        isLoading=false;
                        if (myAllMessageBean.getData() == null){
                            mPbNodata.setVisibility(View.GONE);
                            mTvNodata.setText("已经没有更多数据");
                            return;
                        }
                        mMyAllMessageList.addAll(myAllMessageBean.getData());
                        mMyAllMessageAdapter.notifyDataSetChanged();
                        if (myAllMessageBean.getData() != null&&myAllMessageBean.getData().size()<20) {
                            mPbNodata.setVisibility(View.GONE);
                            mTvNodata.setText("已经没有更多数据");
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_all_message, container, false);
    }

    @Override
    protected View initView() {
        return null;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View loadingData = View.inflate(mActivity, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMyAllMessageAdapter = new MyAllMessageAdapter(getActivity(), mMyAllMessageList);
        mMyAllMessageAdapter.setFooterView(loadingData);
        mRecyclerView.setAdapter(mMyAllMessageAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (!recyclerView.canScrollVertically(-1)) {
                } else if (!recyclerView.canScrollVertically(1)) {
                } else if (dy < 0) {
                } else if (dy > 0) {
                    if (lastVisibleItemPosition + 1 == mMyAllMessageAdapter.getItemCount() - 1) {
                        if (!isLoading) {
                            isLoading = true;
                            PAGENUMBER++;
                            initData(PAGENUMBER);
                        }
                    }
                }

            }
        });
    }
}
