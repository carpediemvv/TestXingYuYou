package com.xingyuyou.xingyuyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.views.ContactSortModel;

import java.util.List;

public class FriendAdapter extends BaseAdapter implements SectionIndexer {
    private List<ContactSortModel.DataBean> list = null;
    private Context mContext;
    private ImageInterface ImageInterface;

    public void imageSetOnclick(ImageInterface imageInterface){
        this.ImageInterface=imageInterface;
    }

    /**
     * 头像点击事件对应的接口
     */
    public interface ImageInterface{
        public void onclick(int position);
    }

    public FriendAdapter(Context mContext, List<ContactSortModel.DataBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<ContactSortModel.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final ContactSortModel.DataBean mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_friend, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_city_name);
            viewHolder.image_imageview= (ImageView) view.findViewById(R.id.image_imageview);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv_catagory);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {

            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(list.get(position).getNickname());
        Glide.with(mContext)
                .load(list.get(position).getHead_image())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(mContext))
                .into(viewHolder.image_imageview);

        viewHolder.image_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageInterface.onclick(position);
            }
        });

        return view;

    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        ImageView image_imageview;
    }

    public int getSectionForPosition(int position) {

        return list.get(position).getSortLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}