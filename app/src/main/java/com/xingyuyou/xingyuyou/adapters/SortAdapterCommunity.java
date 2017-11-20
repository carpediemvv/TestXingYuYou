package com.xingyuyou.xingyuyou.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.bean.CommunityBean;

import java.util.HashMap;
import java.util.List;

public class SortAdapterCommunity extends BaseAdapter implements SectionIndexer {
    private List<CommunityBean.DataBean> list = null;
    private Context mContext;
    private ImageInterface ImageInterface;
    //定义hashMap 用来存放之前创建的每一项item
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    private onItemDeleteListener mOnItemDeleteListener;

    /**
     * 头像点击事件对应的接口
     */
    public interface ImageInterface{
        public void onclick(int position);
    }

    public SortAdapterCommunity(Context mContext, List<CommunityBean.DataBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<CommunityBean.DataBean> list) {
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

    public View getView(final int position, View convertView, ViewGroup arg2) {
       // ViewHolder viewHolder = null;
        final CommunityBean.DataBean mContent = list.get(position);
      //  if (lmap.get(position) == null) {
       //     viewHolder = new ViewHolder();
         View   view = LayoutInflater.from(mContext).inflate(R.layout.item_community, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_city_name);
        ImageView image_imageview= (ImageView) view.findViewById(R.id.image_imageview);
        TextView tvLetter = (TextView) view.findViewById(R.id.tv_catagory);
        Button image_choose= (Button) view.findViewById(R.id.image_choose);
        View view_line=view.findViewById(R.id.view_line);
        TextView tv_city_name_content= (TextView) view.findViewById(R.id.tv_city_name_content);
      //      view.setTag(viewHolder);
      //      lmap.put(position,view);
       // } else {
       /*     view = lmap.get(position);
            viewHolder = (ViewHolder) view.getTag();*/
       // }
        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            tvLetter.setVisibility(View.VISIBLE);
          tvLetter.setText(mContent.getSortLetters());
            view_line.setVisibility(View.VISIBLE);
        } else {
            view_line.setVisibility(View.GONE);
            tvLetter.setVisibility(View.GONE);
        }
        tv_city_name_content.setText(list.get(position).getRecommend_number()+"人已关注");
       tvTitle.setText(list.get(position).class_name);
        Glide.with(mContext)
                .load(list.get(position).class_image)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(image_imageview);


        if(list.get(position).getRecommend_status()==0){
           noJoin(image_choose);
        }else {
            join(image_choose);
        }

        image_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemDeleteListener.onDeleteClick(position);
            }
        });
        return view;

    }
    public void noJoin(Button button) {
        button.setTextColor(android.graphics.Color.parseColor("#ff717c"));
        Resources resources = mContext.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.joincommunity);
        button.setBackgroundDrawable(drawable);
        button.setText("+加入");
    }

    public void join(Button button) {
        button.setTextColor(android.graphics.Color.parseColor("#ffffff"));
        Resources resources = mContext.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.jion_style);
        button.setBackgroundDrawable(drawable);
        button.setText("已加入");
    }

/*

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        ImageView image_imageview;
        Button image_choose;
        View view_line;
    }
*/

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
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }
    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

}