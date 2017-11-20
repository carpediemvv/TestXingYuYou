package com.xingyuyou.xingyuyou.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.bean.ChooseFriendBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class SelectCommTagAdapter extends BaseAdapter {
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    List<ChooseFriendBean.DataBean> mDatas;
    Context context;

    public SelectCommTagAdapter(List<ChooseFriendBean.DataBean> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
    }

    public void updateListView(List<ChooseFriendBean.DataBean> list) {
        mDatas = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(final int position, View view, ViewGroup arg2) {

        ViewHolder viewHolder = null;
        if (lmap.get(position) == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_comm_tag_list, null);
            viewHolder.mGameName = (TextView) view.findViewById(R.id.tv_class_name);
            viewHolder.mGamePic= (ImageView) view.findViewById(R.id.iv_class_image);
            viewHolder.mRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_root_comm_tags);
            viewHolder.image_choose= (ImageView) view.findViewById(R.id.image_choose);
            view.setTag(viewHolder);
            lmap.put(position,view);
        } else {
            view = lmap.get(position);
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mGameName.setText(mDatas.get(position).getClass_name());
        Glide.with(context)
                .load(mDatas.get(position).getClass_image())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                //   .transform(new GlideCircleTransform(SelectCommTagActivity.this))
                .into(viewHolder.mGamePic);

        if(mDatas.get(position).getTag()!=null){
            viewHolder.image_choose.setImageResource(R.drawable.ic_choose);
        }else {
            viewHolder.image_choose.setImageResource(R.drawable.ic_notchoose);
        }


        return view;

    }




    class ViewHolder {
        private ImageView mGamePic;
        private TextView mGameName;
        private ImageView image_choose;
        private RelativeLayout mRelativeLayout;
    }
}
