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
import android.widget.TextView;
import android.widget.Toast;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.Post;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.RetrofitServiceManager;
import com.xingyuyou.xingyuyou.bean.FenQuListBean;
import com.xingyuyou.xingyuyou.bean.theme.NodataBean;

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

public class NormalDialogFragment extends DialogFragment {

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
        View view = inflater.inflate(R.layout.fragment_dialog_normal, container);
        initView(view);

        return view;
    }

    private void postOperation() {
        if (getArguments().getString("type_houtai").equals("1")) {
            RetrofitServiceManager.getInstance()
                    .create(Post.class)
                    .postsTopWell(UserUtils.getUserId()
                            , getArguments().getString("tid")
                            , getArguments().getString("fid"),
                            getArguments().getString("type"))
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
        } else {
            RetrofitServiceManager.getInstance()
                    .create(Post.class)
                    .postsDelete(UserUtils.getUserId()
                            , getArguments().getString("tid")
                            , getArguments().getString("fid"),
                            getArguments().getString("type"))
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

    }


    private void initView(View view) {
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(getArguments().getString("type_values"));

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOperation();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
