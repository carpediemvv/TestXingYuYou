package com.xingyuyou.xingyuyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * Created by Administrator on 2017/7/17.
 */

public class OtherListAdapter extends BaseAdapter {

    private ArrayList<OtherMessageBean.DataBean> otherMessageBean;
    private Context mContext;
    private MessageAdapter.OnItemClickListener mOnItemClickListener;


    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(MessageAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public OtherListAdapter(ArrayList<OtherMessageBean.DataBean> otherMessageBean, Context mContext) {
        this.otherMessageBean = otherMessageBean;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return otherMessageBean.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

/*       TextView text_name_fragmentMessage;
        TextView text_content_fragmentMessage;
        TextView text_replyName_fragmentMessage;
        TextView text_reply_fragmentMessage;
        TextView text_replyName2_fragmentMessage;
        TextView text_selectNum_fragmentMessage;
        TextView text_time_fragmentMessage;

        TextView text_reply2_fragmentMessage;
        ImageView image_head_fragmentMessage;
        RelativeLayout re_fragmentMessage;*/

        ViewHolder holder;
        if (convertView == null) {
            View view = View.inflate(mContext, R.layout.item_fragment_message, null);
            holder = new ViewHolder();
            //点击事件
          holder.text_name_fragmentMessage= (TextView) view.findViewById(R.id.text_name_fragmentMessage);
            holder.text_content_fragmentMessage= (TextView) view.findViewById(R.id.text_content_fragmentMessage);
            holder.text_replyName_fragmentMessage= (TextView) view.findViewById(R.id.text_replyName_fragmentMessage);
          //  holder.text_reply_fragmentMessage= (TextView) view.findViewById(R.id.text_reply_fragmentMessage);
            holder.text_replyName2_fragmentMessage= (TextView) view.findViewById(R.id.text_replyName2_fragmentMessage);

            holder.text_selectNum_fragmentMessage= (TextView) view.findViewById(R.id.text_selectNum_fragmentMessage);
            holder.text_time_fragmentMessage= (TextView) view.findViewById(R.id.text_time_fragmentMessage);

           // holder.text_reply2_fragmentMessage= (TextView) view.findViewById(R.id.text_reply2_fragmentMessage);
            holder.image_head_fragmentMessage= (ImageView) view.findViewById(R.id.image_head_fragmentMessage);
            holder.re_fragmentMessage= (RelativeLayout) view.findViewById(R.id.re_fragmentMessage);

        } else {
            holder = (ViewHolder)convertView.getTag();
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
        //适配数据
        if (otherMessageBean.get(position).relist.size() > 0) {
            //判断
            if (otherMessageBean.get(position).relist.size() == 1) {
                holder.text_selectNum_fragmentMessage.setVisibility(View.INVISIBLE);
            } else {
                holder.text_selectNum_fragmentMessage.setVisibility(View.VISIBLE);
            }
            holder.text_reply2_fragmentMessage.setVisibility(View.VISIBLE);
            if (otherMessageBean.get(position).relist.size() == 1) {
                holder.text_selectNum_fragmentMessage.setVisibility(View.GONE);
                holder.text_replyName2_fragmentMessage.setVisibility(View.GONE);
                holder.text_reply2_fragmentMessage.setVisibility(View.GONE);

                holder.text_replyName_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_replyName_fragmentMessage,
                        otherMessageBean.get(position).relist.get(0).nickname + ":"));
                holder.text_reply_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_reply_fragmentMessage,
                        otherMessageBean.get(position).relist.get(0).content));

            } else if (otherMessageBean.get(position).relist.size() > 1) {
                holder.text_selectNum_fragmentMessage.setVisibility(View.VISIBLE);
                holder.text_replyName2_fragmentMessage.setVisibility(View.VISIBLE);
                holder.text_reply2_fragmentMessage.setVisibility(View.VISIBLE);

                holder.text_replyName_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_replyName_fragmentMessage,
                        otherMessageBean.get(position).relist.get(0).nickname + ":"));
                holder.text_reply_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_reply_fragmentMessage,
                        otherMessageBean.get(position).relist.get(0).content));
                holder.text_replyName2_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_replyName2_fragmentMessage,
                        otherMessageBean.get(position).relist.get(1).nickname + ":"));
                holder.text_reply2_fragmentMessage.setText(SpanStringUtils.getEmotionContent(mContext, holder.text_reply2_fragmentMessage,
                        otherMessageBean.get(position).relist.get(1).content));
            }
        } else {
            holder.re_fragmentMessage.setVisibility(View.GONE);
        }

        return convertView;
    }

   class ViewHolder {
       TextView text_name_fragmentMessage;
       TextView text_content_fragmentMessage;
       TextView text_replyName_fragmentMessage;
       TextView text_reply_fragmentMessage;
       TextView text_replyName2_fragmentMessage;
       TextView text_selectNum_fragmentMessage;
       TextView text_time_fragmentMessage;
       TextView text_reply2_fragmentMessage;
       ImageView image_head_fragmentMessage;
       RelativeLayout re_fragmentMessage;
   }
}
