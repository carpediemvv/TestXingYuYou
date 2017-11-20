package com.xingyuyou.xingyuyou.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.Post;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.user.GetUserData;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.RetrofitServiceManager;
import com.xingyuyou.xingyuyou.bean.FenQuListBean;
import com.xingyuyou.xingyuyou.bean.community.TagBean;
import com.xingyuyou.xingyuyou.bean.theme.NodataBean;
import com.xingyuyou.xingyuyou.bean.user.UserInfo;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 24002 on 2017/11/15.
 */

public class ChangeFidDialogFragment extends DialogFragment {
    private static final String TAG = "fenqu";
    private RecyclerView mRecyclerView;
    private ChangeFidAdapter mChangeFidAdapter;
    private ArrayList<FenQuListBean.DataBean> mData = new ArrayList();
    private ArrayList<FenQuListBean.DataBean> mRemoveData = new ArrayList();

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View view = inflater.inflate(R.layout.fragment_dialog_change_fid, container);
        initView(view);
        initData();
        return view;
    }
/* .getFenQulList(UserUtils.getUserId(), getArguments().getString("fid"))*/
    private void initData() {
        RetrofitServiceManager.getInstance()
                .create(Post.class)
                .getFenQulList(UserUtils.getUserId(), getArguments().getString("fid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FenQuListBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull FenQuListBean fenQuListBean) {
                        setValues(fenQuListBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setValues(FenQuListBean fenQuListBean) {
        mData.addAll(fenQuListBean.getData());
        mChangeFidAdapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mChangeFidAdapter = new ChangeFidAdapter();
        mRecyclerView.setAdapter(mChangeFidAdapter);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (FenQuListBean.DataBean tag : mRemoveData) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("label_name", tag.getMenu_name());
                    map.put("id", tag.getId());
                    list.add(map);
                }
                JSONArray array = new JSONArray(list);
                postsMove(array.toString());
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    /**
     * 帖子移动
     */
    private void postsMove(String s) {
        RetrofitServiceManager.getInstance()
                .create(Post.class)
                .postsMove(UserUtils.getUserId()
                        , getArguments().getString("tid")
                        , getArguments().getString("fid"),
                        s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NodataBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull NodataBean data) {
                            Toast.makeText(getActivity(), data.getErrorinfo(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }
                });
    }

    private class ChangeFidAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(getActivity()).inflate(R.layout.item_fenqupost_list, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((ItemViewHolder) holder).tv_fenqu_name.setText(mData.get(position).getMenu_name());
            ((ItemViewHolder) holder).cb_fenqu_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        mRemoveData.add(mData.get(position));
                    }else {
                        mRemoveData.remove(mData.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_fenqu_name;
            private CheckBox cb_fenqu_list;

            public ItemViewHolder(View itemView) {
                super(itemView);
                tv_fenqu_name = (TextView) itemView.findViewById(R.id.tv_fenqu_name);
                cb_fenqu_list = (CheckBox) itemView.findViewById(R.id.cb_fenqu_list);
            }
        }
    }
}
