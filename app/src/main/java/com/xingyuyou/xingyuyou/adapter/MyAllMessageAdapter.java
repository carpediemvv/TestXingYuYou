package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.SpanStringUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.activity.OtherPageActivity;
import com.xingyuyou.xingyuyou.activity.PostDetailActivityy;
import com.xingyuyou.xingyuyou.bean.Voied_bean;
import com.xingyuyou.xingyuyou.bean.user.MyAllMessageBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by 24002 on 2017/7/26.
 */

public class MyAllMessageAdapter extends RecyclerView.Adapter {
    private List<MyAllMessageBean.DataBean> mListData;
    private Activity mActivity;
    public static final int TYPE_FOOTER = 0;  //说明是带有Footer的
    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;
    public static final int TYPE_FOUR = 4;
    public static final int TYPE_FIVES = 5;
    public static final int TYPE_SIX = 6;
    private View mFooterView;
    public MyAllMessageAdapter(Activity activity, List<MyAllMessageBean.DataBean> listData) {
        mListData = listData;
        mActivity = activity;
    }
    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return Integer.parseInt(mListData.get(position).getType());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ItemViewHolder(mFooterView);
        }
        switch (viewType) {
            case TYPE_TWO:
            case TYPE_THREE:
                View layout = LayoutInflater.from(mActivity).inflate(R.layout.item_my_all_message_list_two, parent, false);
                return new ItemViewHolder(layout);
            case TYPE_ONE:
            case TYPE_FOUR:
            case TYPE_FIVES:
            case TYPE_SIX:
                View layout4 = LayoutInflater.from(mActivity).inflate(R.layout.item_my_all_message_list_one, parent, false);
                return new ItemViewHolder(layout4);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder && (getItemViewType(position) == TYPE_TWO
                || getItemViewType(position) == TYPE_THREE
                || getItemViewType(position) == TYPE_FOUR
                || getItemViewType(position) == TYPE_FIVES
                || getItemViewType(position) == TYPE_SIX
                || getItemViewType(position) == TYPE_ONE)) {
            ((ItemViewHolder) holder).tv_title.setText(mListData.get(position).getTitle());
            ((ItemViewHolder) holder).tv_time.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mListData.get(position).getDateline() + "000")));
            if (getItemViewType(position) == TYPE_TWO) {
                ((ItemViewHolder) holder).iv_user_photo.setImageResource(R.drawable.ic_action_pic_guanzhu);
                ((ItemViewHolder) holder).rl_item_onclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(mActivity, OtherPageActivity.class);
                        intent.putExtra("re_uid",mListData.get(position).getUid());
                        mActivity.startActivity(intent);
                    }
                });
            }
            if (getItemViewType(position) == TYPE_THREE) {
                ((ItemViewHolder) holder).iv_user_photo.setImageResource(R.drawable.ic_action_pic_liuyan);
                ((ItemViewHolder) holder).rl_item_onclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent("toLiuYan");
                        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                    }
                });

            }
            if (getItemViewType(position ) == TYPE_ONE || getItemViewType(position) == TYPE_FOUR) {
                ((ItemViewHolder) holder).iv_user_photo.setImageResource(R.drawable.ic_action_pic_zan);
                ((ItemViewHolder) holder).tv_content.setText(SpanStringUtils.getEmotionContent(mActivity, ((ItemViewHolder) holder).tv_content, mListData.get(position ).getContent()));
                ((ItemViewHolder) holder).rl_item_onclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                /*        Intent intent = new Intent(mActivity, PostDetailActivityy.class);
                        intent.putExtra("post_id", mListData.get(position).getPid());
                        mActivity.startActivity(intent);*/
                        okSend(position);
                    }
                });
            }
            if (getItemViewType(position ) == TYPE_FIVES) {
                ((ItemViewHolder) holder).iv_user_photo.setImageResource(R.drawable.ic_action_pic_jing);
                ((ItemViewHolder) holder).tv_content.setText(SpanStringUtils.getEmotionContent(mActivity, ((ItemViewHolder) holder).tv_content, mListData.get(position ).getContent()));
                ((ItemViewHolder) holder).rl_item_onclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                 /*       Intent intent = new Intent(mActivity, PostDetailActivityy.class);
                        intent.putExtra("post_id", mListData.get(position).getPid());
                        mActivity.startActivity(intent);*/
                        okSend(position);
                    }
                });
            }
            if (getItemViewType(position ) == TYPE_SIX) {
                ((ItemViewHolder) holder).iv_user_photo.setImageResource(R.drawable.ic_action_pic_at);
                ((ItemViewHolder) holder).tv_content.setText(SpanStringUtils.getEmotionContent(mActivity, ((ItemViewHolder) holder).tv_content, mListData.get(position ).getContent()));
                ((ItemViewHolder) holder).rl_item_onclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
               /*         Intent intent = new Intent(mActivity, PostDetailActivityy.class);
                        intent.putExtra("post_id", mListData.get(position).getPid());
                        mActivity.startActivity(intent);*/
                        okSend(position);
                    }
                });
            }
        }
    }

    public void okSend(final int possin){
        OkHttpUtils.post()
                .addParams("tid",mListData.get(possin).getPid())
                .url("http://xingyuyou.com/app.php/Posts/posts_vod_check")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson=new Gson();
                        Voied_bean voied_bean= gson.fromJson(response, Voied_bean.class);
                        if(voied_bean.data==null){
                            Toast.makeText(mActivity,"该帖已删除",Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(mActivity, PostDetailActivityy.class);
                            intent.putExtra("post_id", mListData.get(possin).getPid());
                            intent.putExtra("vod_id", voied_bean.data.vod_id);
                            mActivity.startActivity(intent);
                        }

                    }
                });
    }
    @Override
    public int getItemCount() {
        if ( mFooterView == null) {
            return mListData.size();
        }  else {
            return mListData.size() + 1;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_user_photo;
        private TextView tv_title;
        private TextView tv_time;
        private TextView tv_content;
        private RelativeLayout rl_item_onclick;

        public ItemViewHolder(View itemView) {
            super(itemView);
            if (itemView == mFooterView) {
                return;
            }
            iv_user_photo = (ImageView) itemView.findViewById(R.id.iv_user_photo);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            rl_item_onclick = (RelativeLayout) itemView.findViewById(R.id.rl_item_onclick);
        }
    }

}
