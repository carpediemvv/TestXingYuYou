package com.xingyuyou.xingyuyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.bean.other.OtherMessageBean;

/**
 * Created by Administrator on 2017/7/15.
 */

public class MessageOtherAdapter extends BaseAdapter {
    private Context context;
    private OtherMessageBean.DataBean otherbean;

    public MessageOtherAdapter(Context context, OtherMessageBean.DataBean otherbean) {
        this.context = context;
        this.otherbean = otherbean;
    }

    @Override
    public int getCount() {
        return otherbean.relist.size();
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_commo_to_commo_list, null);
            holder.head_name = (TextView) convertView.findViewById(R.id.tv_user2_name);
/*            holder.conten_text = (TextView) convertView.findViewById(R.id.tv_commo2_content);
            holder.time_text = (TextView) convertView.findViewById(R.id.tv_commo2_time);*/
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
  /*      holder.head_name.setText(otherbean.relist.get(i).nickname);
        holder.conten_text.setText(otherbean.relist.get(i).content);*/
        return convertView;
    }

    class ViewHolder {
        TextView head_name;
        TextView conten_text;
        TextView time_text;
    }
}




