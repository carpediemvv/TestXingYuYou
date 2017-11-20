package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.GodListDetailActivity;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.PostDetailActivityy;
import com.xingyuyou.xingyuyou.bean.community.PostListBeanTest;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/21.
 */
public class CardOtherAdapter extends RecyclerView.Adapter {
    //数据
    private List<PostListBeanTest> mListData;
    private Activity mActivity;
    private int fragmentType;

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    public static final int TYPE_ONE_PIC = 3;  //一张图
    public static final int TYPE_TWO_PIC = 4;  //二张图
    public static final int TYPE_THREE_PIC = 5;  //三张图
    public static final int TYPE_THREE_VIDEO = 6;  //视频
    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;

    public CardOtherAdapter(int i,Activity activity, List<PostListBeanTest> listData) {
        mListData = listData;
        mActivity = activity;
        fragmentType=i;
    }

    public void setDatas(List<PostListBeanTest> listData) {
        mListData = listData;
    }

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }




    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0) {
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }

        //第一步
        if(mListData.get(position - 1).getPost_type().equals("2")){
            return  TYPE_THREE_VIDEO;
        }
        if (mListData.get(position - 1).getThumbnail_image().size()==0){
            if (mListData.get(position - 1).getPosts_image().size() == 1) {
                return TYPE_ONE_PIC;
            }
            if (mListData.get(position - 1).getPosts_image().size() == 2) {
                return TYPE_TWO_PIC;
            }
            if (mListData.get(position - 1).getPosts_image().size() >= 3) {
                return TYPE_THREE_PIC;
            }
        }else {
            if (mListData.get(position - 1).getThumbnail_image().size() == 1) {
                return TYPE_ONE_PIC;
            }
            if (mListData.get(position - 1).getThumbnail_image().size() == 2) {
                return TYPE_TWO_PIC;
            }
            if (mListData.get(position - 1).getThumbnail_image().size() >= 3) {
                return TYPE_THREE_PIC;
            }
        }
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ItemViewHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ItemViewHolder(mFooterView);
        }
        switch (viewType) {
            case 3:
                View layout = LayoutInflater.from(mActivity).inflate(R.layout.item_comm_list, parent, false);
                return new ItemViewHolder(layout);
            case 4:
                View layout1 = LayoutInflater.from(mActivity).inflate(R.layout.item_comm_list1, parent, false);
                return new ItemViewHolder(layout1);
            case 5:
                View layout2 = LayoutInflater.from(mActivity).inflate(R.layout.item_comm_list2, parent, false);
                return new ItemViewHolder(layout2);
            case 6:
                View layout3 = LayoutInflater.from(mActivity).inflate(R.layout.item_comm_list4, parent, false);
                return new ItemViewHolder(layout3);
        }
        return null;
    }

    @Override

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder && (getItemViewType(position) == TYPE_ONE_PIC
                || getItemViewType(position) == TYPE_TWO_PIC
                || getItemViewType(position) == TYPE_THREE_PIC
                || getItemViewType(position) == TYPE_THREE_VIDEO)) {

            if (mListData.get(position - 1).getCollect_status()==1){
                ((ItemViewHolder) holder).mPostCollect.setImageResource(R.drawable.ic_action_like);
            }else {
                ((ItemViewHolder) holder).mPostCollect.setImageResource(R.drawable.ic_action_love_empty);
            }
            if (mListData.get(position - 1).getLaud_status()==1){
                ((ItemViewHolder) holder).mPostLuad.setImageResource(R.drawable.ic_zan_fill);
            }else {
                ((ItemViewHolder) holder).mPostLuad.setImageResource(R.drawable.ic_zan);
            }
            ((ItemViewHolder) holder).mUserName.setText(mListData.get(position - 1).getNickname());
            if (StringUtils.isEmpty(mListData.get(position - 1).getClass_name())){
                ((ItemViewHolder) holder).tv_class_name_tag.setVisibility(View.GONE);
            }else {
                ((ItemViewHolder) holder).tv_class_name_tag.setVisibility(View.VISIBLE);
            }

            if ("2".equals(mListData.get(position - 1).getMedal())){
                ((ItemViewHolder) holder).iv_user_medal.setVisibility(View.VISIBLE);
            }else {
                ((ItemViewHolder) holder).iv_user_medal.setVisibility(View.GONE);
            }
            if (mListData.get(position - 1).getIs_well() == 1) {
                Drawable drawable = mActivity.getResources().getDrawable(R.drawable.ic_action_jinghua);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                ((ItemViewHolder) holder).mPostName.setCompoundDrawables(drawable,null,null,null);
            }else {
                ((ItemViewHolder) holder).mPostName.setCompoundDrawables(null,null,null,null);
            }
            ((ItemViewHolder) holder).tv_class_name.setText(mListData.get(position - 1).getClass_name());
            ((ItemViewHolder) holder).mPostTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mListData.get(position - 1).getDateline() + "000")));
            ((ItemViewHolder) holder).mPostName.setText(mListData.get(position - 1).getSubject());
            ((ItemViewHolder) holder).mPostContent.setText(mListData.get(position - 1).getMessage());
            ((ItemViewHolder) holder).mCollectNum.setText(mListData.get(position - 1).getPosts_collect());
            ((ItemViewHolder) holder).mCommNum.setText(mListData.get(position - 1).getPosts_forums());
            ((ItemViewHolder) holder).mJiaoNangNum.setText(mListData.get(position - 1).getPosts_laud());

            ((ItemViewHolder) holder).ll_root_text.removeAllViews();
            //标签
            if (mListData.get(position - 1).getLabel_name()!=null){
                for (int i = 0; i <mListData.get(position - 1).getLabel_name().size() ; i++) {
                    View text = View.inflate(mActivity, R.layout.textview, null);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(ConvertUtils.dp2px(2), 0, ConvertUtils.dp2px(0),0);
                    TextView textView = (TextView) text.findViewById(R.id.text);
                    textView.setLayoutParams(lp);
                    textView.setText(mListData.get(position - 1).getLabel_name().get(i).getLabel_name());
                    ((ItemViewHolder) holder).ll_root_text.addView(textView);
                }

            }
            Glide.with(mActivity)
                    .load(mListData.get(position - 1).getHead_image())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .transform(new GlideCircleTransform(mActivity))
                    .into(((ItemViewHolder) holder).mUserPhoto);

//家在一张的

            Glide.with(mActivity)
                    .load(mListData.get(position - 1).getThumbnail_image().size()!=0?
                            mListData.get(position - 1).getThumbnail_image().get(0).getThumbnail_image()
                            : mListData.get(position - 1).getPosts_image().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(((ItemViewHolder) holder).mPostCover0);

            if(mListData.get(position - 1).getThumbnail_image().get(0).getThumbnail_image().indexOf("gif") != -1){
                ((ItemViewHolder) holder).gif_icon0.setVisibility(View.VISIBLE);
            }else {
                ((ItemViewHolder) holder).gif_icon0.setVisibility(View.INVISIBLE);
            }

            if (getItemViewType(position) == TYPE_TWO_PIC || getItemViewType(position) == TYPE_THREE_PIC) {
                Glide.with(mActivity)
                        .load(mListData.get(position - 1).getThumbnail_image().size()!=0?
                                mListData.get(position - 1).getThumbnail_image().get(1).getThumbnail_image()
                                : mListData.get(position - 1).getPosts_image().get(1))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(((ItemViewHolder) holder).mPostCover1);

                if(mListData.get(position - 1).getThumbnail_image().get(1).getThumbnail_image().indexOf("gif") != -1){
                    ((ItemViewHolder) holder).gif_icon1.setVisibility(View.VISIBLE);
                }else {
                    ((ItemViewHolder) holder).gif_icon1.setVisibility(View.INVISIBLE);
                }
            }
            if (getItemViewType(position) == TYPE_THREE_PIC) {
                Glide.with(mActivity)
                        .load(mListData.get(position - 1).getThumbnail_image().size()!=0?
                                mListData.get(position - 1).getThumbnail_image().get(2).getThumbnail_image()
                                : mListData.get(position - 1).getPosts_image().get(2))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(((ItemViewHolder) holder).mPostCover2);

                if(mListData.get(position - 1).getThumbnail_image().get(2).getThumbnail_image().indexOf("gif") != -1) {
                    ((ItemViewHolder) holder).gif_icon2.setVisibility(View.VISIBLE);
                }else {
                    ((ItemViewHolder) holder).gif_icon2.setVisibility(View.INVISIBLE);
                }
            }

            ((ItemViewHolder) holder).mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListData.get(position - 1).getUid().equals("206")) {
                        mActivity.startActivity(new Intent(mActivity,GodListDetailActivity.class)
                                .putExtra("activity_id",mListData.get(position - 1).getId()));
                    }else {

                        Intent intent = new Intent(mActivity, PostDetailActivityy.class);
                        intent.putExtra("post_id", mListData.get(position - 1).getId());
                        intent.putExtra("position",(position));
                        intent.putExtra("fragmentType",fragmentType);
                        intent.putExtra("vod_id",mListData.get(position-1).getVod_id());
                        mActivity.startActivity(intent);
                    }
                }
            });
            //长按事件



            ((ItemViewHolder) holder).mRlCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!UserUtils.logined()){
                        IntentUtils.startActivity(mActivity, LoginActivity.class);
                        return;
                    }
                    getCollect(mListData.get(position - 1).getId());
                    if (mListData.get(position - 1).getCollect_status()==1){
                        ((ItemViewHolder) holder).mCollectNum.setText(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_collect())-1)));
                        mListData.get(position - 1).setPosts_collect(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_collect())-1)));
                        mListData.get(position - 1).setCollect_status(0);
                        Toast.makeText(mActivity, "取消收藏", Toast.LENGTH_SHORT).show();
                        ((ItemViewHolder) holder).mPostCollect.setImageResource(R.drawable.ic_action_love_empty);
                    }else {
                        ((ItemViewHolder) holder).mCollectNum.setText(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_collect())+1)));
                        mListData.get(position - 1).setPosts_collect(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_collect())+1)));
                        mListData.get(position - 1).setCollect_status(1);
                        Toast.makeText(mActivity, "收藏成功", Toast.LENGTH_SHORT).show();
                        ((ItemViewHolder) holder).mPostCollect.setImageResource(R.drawable.ic_action_like);

                    }
                }
            });
            ((ItemViewHolder) holder).mRlComm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            ((ItemViewHolder) holder).mRlJiaonang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!UserUtils.logined()){
                        IntentUtils.startActivity(mActivity, LoginActivity.class);
                        return;
                    }
                    getLaud(mListData.get(position - 1).getId());
                    if (mListData.get(position - 1).getLaud_status()==1){
                        ((ItemViewHolder) holder).mJiaoNangNum.setText(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_laud())-1)));
                        mListData.get(position - 1).setPosts_laud(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_laud())-1)));
                        mListData.get(position - 1).setLaud_status(0);
                        Toast.makeText(mActivity, "取消点赞", Toast.LENGTH_SHORT).show();
                        ((ItemViewHolder) holder).mPostLuad.setImageResource(R.drawable.ic_zan);
                    }else {
                        ((ItemViewHolder) holder).mJiaoNangNum.setText(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_laud())+1)));
                        mListData.get(position - 1).setPosts_laud(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_laud())+1)));
                        mListData.get(position - 1).setLaud_status(1);
                        Toast.makeText(mActivity, "点赞成功", Toast.LENGTH_SHORT).show();
                        ((ItemViewHolder) holder).mPostLuad.setImageResource(R.drawable.ic_zan_fill);

                    }
                }
            });
        } else if (getItemViewType(position) == TYPE_HEADER) {
            return;
        } else {
            return;
        }

    }
    public void getCollect( String tid) {
        OkHttpUtils.post()//
                .addParams("tid",tid)
                .addParams("uid",UserUtils.getUserId())
                .url(XingYuInterface.GET_COLLECT)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                    }
                });

    }
    public void getLaud( String tid) {
        OkHttpUtils.post()//
                .addParams("tid",tid)
                .addParams("uid",UserUtils.getUserId())
                .url(XingYuInterface.GET_LAUD)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                    }
                });

    }
    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFooterView == null) {
            return mListData.size();
        } else if (mHeaderView == null && mFooterView != null) {
            return mListData.size() + 1;
        } else if (mHeaderView != null && mFooterView == null) {
            return mListData.size() + 1;
        } else {
            return mListData.size() + 2;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPostCover0;
        private ImageView mPostCover1;
        private ImageView mPostCover2;
        private ImageView gif_icon0;
        private ImageView gif_icon1;
        private ImageView gif_icon2;
        private ImageView mPostCollect;
        private ImageView mPostCommo;
        private ImageView mPostLuad;
        private ImageView mUserPhoto;
        private TextView mPostName;
        private TextView mCollectNum;
        private TextView mCommNum;
        private TextView mJiaoNangNum;
        private TextView mPostContent;
        private TextView mUserName;
        private TextView mPostTime;
        private TextView tv_class_name;
        private TextView tv_class_name_tag;
        private LinearLayout ll_root_text;
        private LinearLayout mLinearLayout;
        private RelativeLayout mRlCollect;
        private RelativeLayout mRlComm;
        private RelativeLayout mRlJiaonang;
        private ImageView iv_user_medal;

        public ItemViewHolder(View itemView) {
            super(itemView);
            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            iv_user_medal = (ImageView) itemView.findViewById(R.id.iv_user_medal);
            mPostCover0 = (ImageView) itemView.findViewById(R.id.iv_post_cover0);
            mPostCover1 = (ImageView) itemView.findViewById(R.id.iv_post_cover1);
            mPostCover2 = (ImageView) itemView.findViewById(R.id.iv_post_cover2);

            gif_icon0= (ImageView) itemView.findViewById(R.id.gif_icon0);
            gif_icon1= (ImageView) itemView.findViewById(R.id.gif_icon1);
            gif_icon2= (ImageView) itemView.findViewById(R.id.gif_icon2);
            mPostCollect = (ImageView) itemView.findViewById(R.id.iv_post_collect);
            mPostCommo = (ImageView) itemView.findViewById(R.id.iv_post_comm);
            mPostLuad = (ImageView) itemView.findViewById(R.id.iv_post_jiaonang);
            mUserPhoto = (ImageView) itemView.findViewById(R.id.civ_user_photo);
            mPostName = (TextView) itemView.findViewById(R.id.tv_post_name);
            mPostContent = (TextView) itemView.findViewById(R.id.tv_post_content);
            mUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            mPostTime = (TextView) itemView.findViewById(R.id.tv_post_time);
            mCollectNum = (TextView) itemView.findViewById(R.id.tv_collect_num);
            mCommNum = (TextView) itemView.findViewById(R.id.tv_comm_num);
            mJiaoNangNum = (TextView) itemView.findViewById(R.id.tv_jiaonang_num);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.LinearLayout);
            mRlCollect = (RelativeLayout) itemView.findViewById(R.id.rl_collect);
            mRlComm = (RelativeLayout) itemView.findViewById(R.id.rl_comm);
            mRlJiaonang = (RelativeLayout) itemView.findViewById(R.id.rl_jiaonang);
            //来自和标签
            tv_class_name = (TextView) itemView.findViewById(R.id.tv_class_name);
            tv_class_name_tag = (TextView) itemView.findViewById(R.id.tv_class_name_tag);
            ll_root_text = (LinearLayout) itemView.findViewById(R.id.ll_root_text);
        }
    }
}
