package com.xingyuyou.xingyuyou.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.OtherPageActivity;
import com.xingyuyou.xingyuyou.activity.UserPageActivity;
import com.xingyuyou.xingyuyou.bean.user.FansListBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;

import static com.xingyuyou.xingyuyou.App.mContext;

/**
 * Created by Administrator on 2017/7/18.
 */

public class FansListAdapter extends BaseAdapter {
    ArrayList<FansListBean.DataBean> dataBeen;
    Context context;
    private ViewHolder viewHolder;
    int log=1;
    int  tag;

    public FansListAdapter(Context context, ArrayList<FansListBean.DataBean> dataBeen, int tag) {
        this.context = context;
        this.dataBeen = dataBeen;
        this.tag = tag;
    }

    @Override
    public int getCount() {
        return dataBeen.size();
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
        viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_fanslist, null);
            viewHolder.imageView_head_fans= (ImageView) convertView.findViewById(R.id.imageView_head_fans);
            viewHolder.imageView_sex_fans= (ImageView) convertView.findViewById(R.id.imageView_sex_fans);
            viewHolder.textView_name_fans= (TextView) convertView.findViewById(R.id.textView_name_fans);
            viewHolder.button_concern_fans= (Button) convertView.findViewById(R.id.button_concern_fans);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context)
                .load(dataBeen.get(position).head_image)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(context))
                .into(viewHolder.imageView_head_fans);
        //性别
        if(dataBeen.get(position).sex!=null){
            if (dataBeen.get(position).sex.equals("0")) {
                viewHolder.imageView_sex_fans.setVisibility(View.GONE);
            } else if(dataBeen.get(position).sex.equals("1")){
                viewHolder.imageView_sex_fans.setImageResource(R.mipmap.ic_action_girl);
            }else {
                viewHolder.imageView_sex_fans.setImageResource(R.mipmap.ic_action_boy);
            }
        }

        viewHolder.textView_name_fans.setText(dataBeen.get(position).nickname);

        if (dataBeen.get(position).relation.equals("1")) {
            viewHolder.button_concern_fans.setText("已关注");
            setBooder();
        } else if (dataBeen.get(position).relation.equals("2")) {
            viewHolder.button_concern_fans.setText("已互粉");
            setBooder();
        } else if (dataBeen.get(position).relation.equals("0")) {
            viewHolder.button_concern_fans.setText("关注ta");
            setBooderNo();
        }
        final ViewHolder finalViewHolder = viewHolder;

        viewHolder.button_concern_fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag==2) {
                    if (dataBeen.get(position).relation.equals("0")) {
                            finalViewHolder.button_concern_fans.setText("已关注");
                            finalViewHolder.button_concern_fans.setTextColor(android.graphics.Color.parseColor("#7A7A7A"));
                            Resources resources = mContext.getResources();
                            Drawable drawable = resources.getDrawable(R.drawable.textview_bordert);
                            finalViewHolder.button_concern_fans.setBackgroundDrawable(drawable);
                            initConcern(position,"0");
                    } else if (dataBeen.get(position).relation.equals("2") || dataBeen.get(position).relation.equals("1")) {
                        if (UserUtils.getUserId().equals(dataBeen.get(position).re_uid)) {
                            context.startActivity(new Intent(context, UserPageActivity.class));
                        } else {
                            Intent intent = new Intent(context, OtherPageActivity.class);
                            intent.putExtra("re_uid", dataBeen.get(position).re_uid);
                            context.startActivity(intent);
                        }
                    }
                }else if(tag==1) {
                    //自己的界面
                    if (dataBeen.get(position).relation.equals("0")) {
                        finalViewHolder.button_concern_fans.setText("已关注");
                        finalViewHolder.button_concern_fans.setTextColor(android.graphics.Color.parseColor("#7A7A7A"));
                        Resources resources = mContext.getResources();
                        Drawable drawable = resources.getDrawable(R.drawable.textview_bordert);
                        finalViewHolder.button_concern_fans.setBackgroundDrawable(drawable);
                        initConcern(position, "0");
                    } else if (dataBeen.get(position).relation.equals("2") || dataBeen.get(position).relation.equals("1")) {
                        dialogShow(position);
                    }
                }


            }
        });


        return convertView;
    }

    class ViewHolder {
        ImageView imageView_head_fans;
        ImageView imageView_sex_fans;
        TextView textView_name_fans;
        Button button_concern_fans;
    }

    public void setBooder(){
        viewHolder.button_concern_fans.setTextColor(android.graphics.Color.parseColor("#7A7A7A"));
        Resources resources = mContext.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.textview_bordert);
        viewHolder.button_concern_fans.setBackgroundDrawable(drawable);


    }
    public void setBooderNo() {
        viewHolder.button_concern_fans.setTextColor(android.graphics.Color.parseColor("#ff717c"));
        Resources resources = mContext.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.other_buttonstyle);
        viewHolder.button_concern_fans.setBackgroundDrawable(drawable);
    }

    public void initConcern(final int position, String relation){
        OkHttpUtils.post()
                .addParams("uid",UserUtils.getUserId())
                .addParams("re_uid",dataBeen.get(position).re_uid)
                .addParams("relation",relation)
                .addParams("type","1")
                .url(XingYuInterface.GET_OTHERCONCERN)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }

    public void dialogShow(final int position){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle("确定不再关注ta了吗?");
        normalDialog.setPositiveButton("我确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dataBeen.get(position).relation.equals("1")){
                            initConcern(position,"1");

                        }else if(dataBeen.get(position).relation.equals("2")){
                            initConcern(position,"2");
                        }
                        dataBeen.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "已取消关注（*>.<*）~ @", Toast.LENGTH_SHORT).show();
                    }
                });
        normalDialog.setNegativeButton("再想想",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        normalDialog.show();
    }
}
