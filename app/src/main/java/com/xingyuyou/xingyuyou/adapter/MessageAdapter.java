package com.xingyuyou.xingyuyou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.SpanStringUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.bean.other.OtherMessageBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/13.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private ArrayList<OtherMessageBean.DataBean> otherMessageBean;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private ImageInterface ImageInterface;
    private CommHotAdapter.OnItemLongClickLitener mOnItemLongClickLitener;

    public void setOnItemLongClickLitener(CommHotAdapter.OnItemLongClickLitener mOnItemLongClickLitener) {
        this.mOnItemLongClickLitener = mOnItemLongClickLitener;
    }

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public MessageAdapter(ArrayList<OtherMessageBean.DataBean> otherMessageBean, Context mContext) {
        this.otherMessageBean = otherMessageBean;
        this.mContext = mContext;
    }
    /**
     *头像点击事件需要的方法
     */
    public void imageSetOnclick(ImageInterface imageInterface){
        this.ImageInterface=imageInterface;
    }

    /**
     * 头像点击事件对应的接口
     */
    public interface ImageInterface{
        public void onclick(int position);
    }
    @Override
    public int getItemCount() {
 /*       if(otherMessageBean==null){
            return 0;
        }*/
        return otherMessageBean.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //长按点击事件
        if (mOnItemLongClickLitener != null) {
            ((MyViewHolder) holder).rl_root.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemLongClickLitener.onItemClick(((MyViewHolder) holder).rl_root, position);
                    return true;
                }
            });

        }
        //点击事件
        if (mOnItemClickListener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView, position); // 2
                }
            });
        }
        holder.re_fragmentMessage.setVisibility(View.VISIBLE);
        holder.text_time_fragmentMessage.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(otherMessageBean.get(position).dateline + "000")));
        holder.text_name_fragmentMessage.setText(otherMessageBean.get(position).nickname);
        holder.text_content_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_content_fragmentMessage,
                otherMessageBean.get(position).content));
        Glide.with(mContext)
                .load(otherMessageBean.get(position).head_image)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(mContext))
                .into(holder.image_head_fragmentMessage);

        //头像点击事件
        holder.image_head_fragmentMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageInterface.onclick(position);
            }
        });
        //适配数据
        if (otherMessageBean.get(position).relist.size() > 0) {
            //判断
            if (otherMessageBean.get(position).relist.size() == 1) {
                holder.text_selectNum_fragmentMessage.setVisibility(View.INVISIBLE);
            } else {
                holder.text_selectNum_fragmentMessage.setVisibility(View.VISIBLE);
            }
           holder.text_replyName2_fragmentMessage.setVisibility(View.VISIBLE);
            if (otherMessageBean.get(position).relist.size() == 1) {
                holder.text_selectNum_fragmentMessage.setVisibility(View.GONE);
                holder.text_replyName2_fragmentMessage.setVisibility(View.GONE);
                holder.text_replyName2_fragmentMessage.setVisibility(View.GONE);
                String   nickname =otherMessageBean.get(position).relist.get(0).nickname;
                String    replies_content1 =  otherMessageBean.get(position).relist.get(0).content;
                SpannableStringBuilder adpaterText = SpanStringUtils.getAdpaterText(nickname+":", replies_content1, "   "+"", holder.text_replyName_fragmentMessage,mContext);
                holder.text_replyName_fragmentMessage.setText(adpaterText);
/*                holder.text_replyName_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_replyName_fragmentMessage,
                        otherMessageBean.get(position).relist.get(0).nickname + ":"));
                holder.text_reply_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_reply_fragmentMessage,
                        otherMessageBean.get(position).relist.get(0).content));*/

            } else if (otherMessageBean.get(position).relist.size() > 1) {
                holder.text_selectNum_fragmentMessage.setVisibility(View.VISIBLE);
                holder.text_replyName2_fragmentMessage.setVisibility(View.VISIBLE);
                holder.text_replyName2_fragmentMessage.setVisibility(View.VISIBLE);
                String   nickname =otherMessageBean.get(position).relist.get(0).nickname;
                String    replies_content1 =  otherMessageBean.get(position).relist.get(0).content;
                SpannableStringBuilder adpaterText = SpanStringUtils.getAdpaterText(nickname+":", replies_content1, "   "+"", holder.text_replyName_fragmentMessage,mContext);
                holder.text_replyName_fragmentMessage.setText(adpaterText);

                String   nickname1 =otherMessageBean.get(position).relist.get(1).nickname;
                String    replies_content2 =  otherMessageBean.get(position).relist.get(1).content;
                SpannableStringBuilder adpaterText1 = SpanStringUtils.getAdpaterText(nickname1+":", replies_content2, "   "+"", holder.text_replyName_fragmentMessage,mContext);
                holder.text_replyName2_fragmentMessage.setText(adpaterText1);

/*                holder.text_replyName_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_replyName_fragmentMessage,
                        otherMessageBean.get(position).relist.get(0).nickname + ":"));
                holder.text_reply_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_reply_fragmentMessage,
                        otherMessageBean.get(position).relist.get(0).content));
                holder.text_replyName2_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_replyName2_fragmentMessage,
                        otherMessageBean.get(position).relist.get(1).nickname + ":"));
                holder.text_reply2_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_reply2_fragmentMessage,
                        otherMessageBean.get(position).relist.get(1).content));*/
            }
        } else {
            holder.re_fragmentMessage.setVisibility(View.GONE);
        }
    }


    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(mContext, R.layout.item_fragment_message, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_name_fragmentMessage;
        TextView text_content_fragmentMessage;
        TextView text_replyName_fragmentMessage;
      //  TextView text_reply_fragmentMessage;
        TextView text_replyName2_fragmentMessage;
        TextView text_selectNum_fragmentMessage;
        TextView text_time_fragmentMessage;
     //   TextView text_reply2_fragmentMessage;
        ImageView image_head_fragmentMessage;
        RelativeLayout re_fragmentMessage;
        RelativeLayout rl_root;

        public MyViewHolder(View view) {
            super(view);
            text_time_fragmentMessage = (TextView) view.findViewById(R.id.text_time_fragmentMessage);
            re_fragmentMessage = (RelativeLayout) view.findViewById(R.id.re_fragmentMessage);
            rl_root = (RelativeLayout) view.findViewById(R.id.rl_root);
            text_selectNum_fragmentMessage = (TextView) view.findViewById(R.id.text_selectNum_fragmentMessage);
            text_name_fragmentMessage = (TextView) view.findViewById(R.id.text_name_fragmentMessage);
            text_content_fragmentMessage = (TextView) view.findViewById(R.id.text_content_fragmentMessage);
            text_replyName_fragmentMessage = (TextView) view.findViewById(R.id.text_replyName_fragmentMessage);
           // text_reply_fragmentMessage = (TextView) view.findViewById(R.id.text_reply_fragmentMessage);
            text_replyName2_fragmentMessage = (TextView) view.findViewById(R.id.text_replyName2_fragmentMessage);
           // text_reply2_fragmentMessage = (TextView) view.findViewById(R.id.text_reply2_fragmentMessage);
            image_head_fragmentMessage = (ImageView) view.findViewById(R.id.image_head_fragmentMessage);
        }

    }
}