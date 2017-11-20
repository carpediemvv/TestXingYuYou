package com.jcodecraeer.xrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yang on 16-3-4.
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public static XRecyclerView.OnItemClickListener onItemClickListener;

    public MyViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(itemView, getPosition());
        }
    }
}
