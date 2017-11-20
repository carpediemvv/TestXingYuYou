package com.xingyuyou.xingyuyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingyuyou.xingyuyou.R;

/**
 * Created by Administrator on 2017/8/25.
 */

public class ChooseAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;
    private int[] images;
    private String[] text;
    public ChooseAdapter(Context context, int[] images, String[] text){
        this.images = images;
        this.text = text;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = layoutInflater.inflate(R.layout.item_grideview_layout,null);
        ImageView iv = (ImageView) v.findViewById(R.id.iv_gridView_item);
        TextView tv = (TextView) v.findViewById(R.id.tv_gridView_item);
        iv.setTag("0");
        iv.setImageResource(images[position]);
        tv.setText(text[position]);

        return v;
    }
}