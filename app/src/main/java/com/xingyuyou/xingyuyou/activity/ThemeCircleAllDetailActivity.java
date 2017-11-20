package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.ThemeCircleList;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.RetrofitServiceManager;
import com.xingyuyou.xingyuyou.bean.theme.ThemeCircleDetailBean;
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

public class ThemeCircleAllDetailActivity extends AppCompatActivity {
    private String TAG = "ThemeCircleFragment";
    private RecyclerView mRecyclerView;
    private ArrayList<ThemeCircleDetailBean.DataBean> mStrings = new ArrayList<>();
    private MultiItemTypeAdapter<ThemeCircleDetailBean.DataBean> mItemTypeAdapter;
    private ItemThemeViewHolder mItemThemeViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_circle_all_detail);
        initToolBar();
        initView();
        initData();
    }
    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getIntent().getStringExtra("theme_name"));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initData() {
        RetrofitServiceManager.getInstance()
                .create(ThemeCircleList.class)
                .getThemeCircleDetailList(getIntent().getStringExtra("type_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ThemeCircleDetailBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ThemeCircleDetailBean userInfo) {
                        setValues(userInfo);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mItemTypeAdapter = new MultiItemTypeAdapter<>(this, mStrings);
        mItemTypeAdapter.addItemViewDelegate(new ThemeCircleAllDetailDelegate());

        mRecyclerView.setAdapter(mItemTypeAdapter);
    }

    public void setValues(ThemeCircleDetailBean values) {
        mStrings.addAll(values.getData());
        Log.e(TAG, "setValues: "+values.toString());
        mItemTypeAdapter.notifyDataSetChanged();
    }


    private class ThemeCircleAllDetailDelegate implements ItemViewDelegate<ThemeCircleDetailBean.DataBean> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_theme_circle_detail_list;
        }

        @Override
        public boolean isForViewType(ThemeCircleDetailBean.DataBean item, int position) {
            return true;
        }

        @Override
        public void convert(ViewHolder holder, ThemeCircleDetailBean.DataBean s, int position) {
            Log.e(TAG, "convert: "+s.toString());
            holder.setText(R.id.tv_theme_circle_item_title, s.getTheme_type_name());
            Glide.with(ThemeCircleAllDetailActivity.this)
                    .load(s.getTheme_type_image())
                    .centerCrop()
                    .into((ImageView) holder.getConvertView().findViewById(R.id.iv_theme_circle_item_icon));
            RecyclerView itemRecyclerView = (RecyclerView) holder.getConvertView().findViewById(R.id.item_recycler_view);
            itemRecyclerView.setLayoutManager(new GridLayoutManager(ThemeCircleAllDetailActivity.this, 3));
            mItemThemeViewHolder = new ItemThemeViewHolder(s.getTheme());
            itemRecyclerView.setTag(mItemThemeViewHolder);
            itemRecyclerView.setAdapter(mItemThemeViewHolder);
        }
    }

    private class ItemThemeViewHolder extends RecyclerView.Adapter{
        private final List<ThemeCircleDetailBean.DataBean.ThemeBean> mThemeList;
        public ItemThemeViewHolder(List<ThemeCircleDetailBean.DataBean.ThemeBean> theme) {
            mThemeList = theme;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(ThemeCircleAllDetailActivity.this).inflate(R.layout.item_theme_circle_item, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            ((ItemViewHolder) holder).tv_item_theme_name.setText(mThemeList.get(position).getClass_name());
            ((ItemViewHolder) holder).tv_item_theme_concern_number.setText(mThemeList.get(position).getRecommend_number() + "人关注");
            Glide.with(ThemeCircleAllDetailActivity.this).load(mThemeList.get(position).getClass_image())
                    .into(((ItemViewHolder) holder).mItemImage);
            ((ItemViewHolder) holder).mItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //帖子详情界面
                    ThemeCircleAllDetailActivity.this.startActivity(new Intent(ThemeCircleAllDetailActivity.this, PostClassListActivity.class)
                            .putExtra("list_id",mThemeList.get(holder.getAdapterPosition()).getId())
                            .putExtra("getClass_head_image",mThemeList.get(holder.getAdapterPosition()).getClass_head_image())
                            .putExtra("getClass_virtual_image",mThemeList.get(holder.getAdapterPosition()).getClass_virtual_image())
                            .putExtra("attribute","2"));

                }
            });
        }

        @Override
        public int getItemCount() {
            return mThemeList.size();
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
