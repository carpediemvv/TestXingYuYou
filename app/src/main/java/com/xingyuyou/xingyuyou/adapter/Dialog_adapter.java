package com.xingyuyou.xingyuyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xingyuyou.xingyuyou.R;

/**
 * Created by Administrator on 2017/8/7.
 */

public class Dialog_adapter extends BaseAdapter{
   String[] list=new String[]{"广告或垃圾信息","涉及黄色和色情","被骚扰或威胁","其他"};
    Context context;
    private int index = -1;
    public Dialog_adapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.length;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_opt,null);
            viewHolder.text_dialog = (TextView) convertView
                    .findViewById(R.id.text_dialog);
            viewHolder.rb_status= (RadioButton) convertView.findViewById(R.id.rb_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text_dialog.setText(list[position]);
        viewHolder.rb_status
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                         //   Toast.makeText(c, "您选择的作家是：" + authors[position],
                         //           Toast.LENGTH_LONG).show();

                            index = position;
                            notifyDataSetChanged();
                        }
                    }
                });
        if (index == position) {// 选中的条目和当前的条目是否相等
            viewHolder.rb_status.setChecked(true);
        } else {
            viewHolder.rb_status.setChecked(false);
        }
    return convertView;
    }

    class ViewHolder {
        RadioButton rb_status;
        TextView text_dialog;
    }

    public int getIndex(){
        return index;
    }

}
