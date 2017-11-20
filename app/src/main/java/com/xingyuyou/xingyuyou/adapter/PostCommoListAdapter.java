package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.SpanStringUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.bean.community.PostCommoBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

import static com.xingyuyou.xingyuyou.App.mContext;
import static com.xingyuyou.xingyuyou.R.id.item_commo_detail;
import static com.xingyuyou.xingyuyou.R.id.re_more_commo_item_bottom;
import static com.xingyuyou.xingyuyou.R.id.tv_commo2_more;

/**
 * Created by 24002 on 2017/5/23.
 */
public class PostCommoListAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<PostCommoBean> mCommoBeanList;
    private String Uid = "";
    private ImageInterface ImageInterface;
    public PostCommoListAdapter(Activity activity, List<PostCommoBean> commoBeanList) {
        this.mActivity = activity;
        this.mCommoBeanList = commoBeanList;
    }


    public void imageSetOnclick(ImageInterface imageInterface) {
        this.ImageInterface = imageInterface;
    }

    /**
     * 头像点击事件对应的接口
     */
    public interface ImageInterface {
        public void onclick(int position);
    }


    /**
     * ItemClick的回调接口
     */
    public interface OnImageItemClickLitener {
        void onItemClick(View view, int position_i, int position_j);
    }

    private OnImageItemClickLitener mOnImageItemClickLitener;

    public void setOnItemClickLitener(OnImageItemClickLitener mOnImageItemClickLitener) {
        this.mOnImageItemClickLitener = mOnImageItemClickLitener;
    }
    /**
     * MyItemClick的回调接口
     */
    public interface OnMyItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnMyItemClickLitener mOnMyItemClickLitener;

    public void setOnMyClickLitener(OnMyItemClickLitener mOnMyItemClickLitener) {
        this.mOnMyItemClickLitener = mOnMyItemClickLitener;
    }

    /**
     * ItemClick的回调接口
     */
    public interface OnItemLongClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemLongClickLitener mOnItemLongClickLitener;

    public void setOnItemLongClickLitener(OnItemLongClickLitener mOnItemLongClickLitener) {
        this.mOnItemLongClickLitener = mOnItemLongClickLitener;
    }

    /**
     * 头像点击事件需要的方法
     */

    public void setUid(String uid) {
        this.Uid = uid;
    }

    @Override
    public int getCount() {
        return mCommoBeanList.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mActivity, R.layout.item_commo_post_list, null);
            holder.re_more_commo_item_bottom = (RelativeLayout) view.findViewById(re_more_commo_item_bottom);
            holder.re_more_commo_item_top = (RelativeLayout) view.findViewById(R.id.re_more_commo_item_top);
            holder.iv_user_photo = (ImageView) view.findViewById(R.id.iv_user_photo);
            holder.iv_zan = (ImageView) view.findViewById(R.id.iv_love);
            holder.tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
            holder.iv_user_medal = (ImageView) view.findViewById(R.id.iv_user_medal);
            holder.iv_banzhu = (ImageView) view.findViewById(R.id.iv_banzhu);
            holder.tv_louzhu = (TextView) view.findViewById(R.id.tv_louzhu);
            holder.tv_zan_num = (TextView) view.findViewById(R.id.tv_love_num);
            holder.tv_reply_content = (TextView) view.findViewById(R.id.tv_commo_content);
            holder.tv_commo_time = (TextView) view.findViewById(R.id.tv_post_time);
            holder.tv_floor_num = (TextView) view.findViewById(R.id.tv_floor_num);
            //条目点击事件
            holder.item_commo_detail = (LinearLayout) view.findViewById(item_commo_detail);
            holder.tv_commo2_name = (TextView) view.findViewById(R.id.tv_commo2_name);
//            holder.tv_commo2_content = (TextView) view.findViewById(R.id.tv_commo2_content);
//            holder.tv_commo2_time = (TextView) view.findViewById(R.id.tv_commo2_time);

            holder.tv_commo3_name = (TextView) view.findViewById(R.id.tv_commo3_name);
//            holder.tv_commo3_content = (TextView) view.findViewById(R.id.tv_commo3_content);
//            holder.tv_commo3_time = (TextView) view.findViewById(R.id.tv_commo3_time);
            holder.ll_root_image_item = (LinearLayout) view.findViewById(R.id.ll_root_image_item);
            //更多评论
            holder.ll_more_commo_item = (LinearLayout) view.findViewById(R.id.ll_more_commo_item);
            holder.tv_commo2_more = (TextView) view.findViewById(tv_commo2_more);
            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (Uid.equals(mCommoBeanList.get(i).getUid())) {
            holder.tv_louzhu.setVisibility(View.VISIBLE);
        } else {
            holder.tv_louzhu.setVisibility(View.GONE);
        }



        //   设置请求下来的颜色   开始
        StringBuffer aa = new StringBuffer();
        String replies_content = mCommoBeanList.get(i).getReplies_content().trim();
        holder.tv_reply_content.setText(SpanStringUtils.getEmotionContent(mActivity, holder.tv_reply_content, replies_content));
        holder.tv_floor_num.setText(mCommoBeanList.get(i).getFloor_num() == null ? "正在抢楼" : mCommoBeanList.get(i).getFloor_num() + "楼");
        holder.tv_user_name.setText(mCommoBeanList.get(i).getNickname());
        holder.tv_zan_num.setText(mCommoBeanList.get(i).getForum_laud());
        //holder.tv_reply_content.setText(SpanStringUtils.getEmotionContent(mActivity, holder.tv_reply_content, mCommoBeanList.get(i).getReplies_content()));
        holder.tv_commo_time.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoBeanList.get(i).getDateline() + "000")));
        Glide.with(mActivity)
                .load(mCommoBeanList.get(i).getHead_image())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(mActivity))
                .into(holder.iv_user_photo);

        //点击头像
        holder.iv_user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageInterface.onclick(i);
            }
        });

        //点击条目
        holder.item_commo_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //条目点击事件
                mOnMyItemClickLitener.onItemClick(view, i);
            }
        });
        holder.ll_root_image_item.removeAllViews();
        if (!(mCommoBeanList.get(i).getThumbnail_image() == null) && mCommoBeanList.get(i).getThumbnail_image().size() >= 1 && !mCommoBeanList.get(i).getThumbnail_image().get(0).toString().equals("")) {
            for (int j = 0; j < mCommoBeanList.get(i).getThumbnail_image().size(); j++) {
                final int finalJ = j;
                //  final ImageView imageView = new ImageView(mActivity);
                final SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(mActivity);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(ConvertUtils.dp2px(0), ConvertUtils.dp2px(5), ConvertUtils.dp2px(0), ConvertUtils.dp2px(5));
                imageView.setLayoutParams(lp);
                //imageView.setAdjustViewBounds(true);

                //判断是否是gif图

                if (!mCommoBeanList.get(i).getThumbnail_image().get(j).equals(imageView.getTag())) {

                    if (mCommoBeanList.get(i).getThumbnail_image().get(j).getThumbnail_image().indexOf("gif") != -1) {
                        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.activity_gifimage, null);
                        final ImageView photoView = (ImageView) view1.findViewById(R.id.gif_imageview);
                        Glide.with(mContext)
                                .load(mCommoBeanList.get(i).getThumbnail_image().get(j).getThumbnail_image())
                                .asBitmap()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(photoView);

                        imageView.setTag(mCommoBeanList.get(i).getThumbnail_image().get(j));
                        holder.ll_root_image_item.addView(view1);


                        //如果设置了回调，则设置点击事件
                        if (mOnImageItemClickLitener != null) {
                            view1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mOnImageItemClickLitener.onItemClick(photoView, i, finalJ);
                                }
                            });

                        }
                    } else {
                        Glide.with(mActivity)
                                .load(mCommoBeanList.get(i).getThumbnail_image().get(j).getThumbnail_image())
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        // imageView.setImageBitmap(resource);
                                        imageView.setImage(ImageSource.bitmap(resource));
                                    }
                                });
                        imageView.setTag(mCommoBeanList.get(i).getThumbnail_image().get(j));
                        holder.ll_root_image_item.addView(imageView);

                        //如果设置了回调，则设置点击事件
                        if (mOnImageItemClickLitener != null) {
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mOnImageItemClickLitener.onItemClick(imageView, i, finalJ);
                                }
                            });
                        }
                    }


                }
            }
        }
        if ("2".equals(mCommoBeanList.get(i).getMedal())) {
            holder.iv_user_medal.setVisibility(View.VISIBLE);
        } else {
            holder.iv_user_medal.setVisibility(View.GONE);
        }
        if ("2".equals(mCommoBeanList.get(i).getRights_is())) {
            holder.iv_banzhu.setVisibility(View.VISIBLE);
        } else {
            holder.iv_banzhu.setVisibility(View.GONE);
        }

        //点赞
        if (mCommoBeanList.get(i).getLaud_status() == 1) {
            holder.iv_zan.setImageResource(R.drawable.ic_zan_fill);
        } else {
            holder.iv_zan.setImageResource(R.drawable.ic_zan);
        }
        holder.iv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(mActivity, LoginActivity.class);
                    return;
                }
                getLuad(mCommoBeanList.get(i).getId(), mCommoBeanList.get(i).getUid());
                if (mCommoBeanList.get(i).getLaud_status() == 1) {
                    holder.tv_zan_num.setText(String.valueOf((Integer.parseInt(mCommoBeanList.get(i).getForum_laud()) - 1)));
                    mCommoBeanList.get(i).setForum_laud(String.valueOf((Integer.parseInt(mCommoBeanList.get(i).getForum_laud()) - 1)));
                    mCommoBeanList.get(i).setLaud_status(0);
                    Toast.makeText(mActivity, "取消点赞", Toast.LENGTH_SHORT).show();
                    holder.iv_zan.setImageResource(R.drawable.ic_zan);
                } else {
                    holder.tv_zan_num.setText(String.valueOf((Integer.parseInt(mCommoBeanList.get(i).getForum_laud()) + 1)));
                    mCommoBeanList.get(i).setForum_laud(String.valueOf((Integer.parseInt(mCommoBeanList.get(i).getForum_laud()) + 1)));
                    mCommoBeanList.get(i).setLaud_status(1);
                    Toast.makeText(mActivity, "点赞", Toast.LENGTH_SHORT).show();
                    holder.iv_zan.setImageResource(R.drawable.ic_zan_fill);
                }
            }
        });
        //更多评论

        if (mCommoBeanList.get(i).getChild() != null && mCommoBeanList.get(i).getChild().size() > 0) {
            int s = mCommoBeanList.get(i).getChild().size() - 2;
            if (mCommoBeanList.get(i).getChild().size() == 1 || mCommoBeanList.get(i).getChild().size() == 2) {
                holder.tv_commo2_more.setVisibility(View.GONE);
                holder.tv_commo2_more.setText("更多" + String.valueOf(s) + "条评论");
            } else {
                holder.tv_commo2_more.setVisibility(View.VISIBLE);
                holder.tv_commo2_more.setText("更多" + String.valueOf(s) + "条评论");
            }
            holder.ll_more_commo_item.setVisibility(View.VISIBLE);

            if (mCommoBeanList.get(i).getChild().size() == 1) {
                //名字 getNickname 内容 getReplies_content() 时间 getDateline()
                String nickname = mCommoBeanList.get(i).getChild().get(0).getNickname();
                String replies_content1 = mCommoBeanList.get(i).getChild().get(0).getReplies_content();
                String dateline =TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoBeanList.get(i).getChild().get(0).getDateline() + "000"));
                SpannableStringBuilder adpaterText = SpanStringUtils.getAdpaterText(nickname+":", replies_content1, "   "+dateline, holder.tv_commo2_name, mActivity);
                holder.tv_commo2_name.setText(adpaterText);
//                holder.tv_commo2_content.setText(SpanStringUtils.getEmotionContent(mActivity, holder.tv_commo2_content
//                        , mCommoBeanList.get(i).getChild().get(0).getReplies_content()));
              /* holder.tv_commo2_content
                        .setText(SpanStringUtils.getEmotionContent(mActivity, holder.tv_commo2_content, mCommoBeanList.get(i).getChild().get(0).getReplies_content()));*/
//                holder.tv_commo2_time
//                        .setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoBeanList.get(i).getChild().get(0).getDateline() + "000")));
                holder.re_more_commo_item_bottom.setVisibility(View.GONE);
            } else if (mCommoBeanList.get(i).getChild().size() > 1) {
                holder.re_more_commo_item_bottom.setVisibility(View.VISIBLE);

                String nickname = mCommoBeanList.get(i).getChild().get(1).getNickname();
                String replies_content1 = mCommoBeanList.get(i).getChild().get(1).getReplies_content();
                String dateline =TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoBeanList.get(i).getChild().get(1).getDateline() + "000"));
                SpannableStringBuilder adpaterText = SpanStringUtils.getAdpaterText(nickname+":", replies_content1, "   "+dateline, holder.tv_commo2_name, mActivity);
                holder.tv_commo2_name.setText(adpaterText);
//                holder.tv_commo2_name
//                        .setText(mCommoBeanList.get(i).getChild().get(1).getNickname() + ":");
//                holder.tv_commo2_content
//                        .setText(SpanStringUtils.getEmotionContent(mActivity, holder.tv_commo2_content, mCommoBeanList.get(i).getChild().get(1).getReplies_content()));
//                holder.tv_commo2_time
//                        .setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoBeanList.get(i).getChild().get(1).getDateline() + "000")));
                nickname = mCommoBeanList.get(i).getChild().get(0).getNickname();
                replies_content1 = mCommoBeanList.get(i).getChild().get(0).getReplies_content();
                dateline =TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoBeanList.get(i).getChild().get(0).getDateline() + "000"));
                adpaterText = SpanStringUtils.getAdpaterText(nickname+":", replies_content1,"   "+dateline, holder.tv_commo3_name, mActivity);
                holder.tv_commo3_name.setText(adpaterText);

                //第二条评论
//                holder.tv_commo3_name
//                        .setText(mCommoBeanList.get(i).getChild().get(0).getNickname() + ":");
//                holder.tv_commo3_content
//                        .setText(SpanStringUtils.getEmotionContent(mActivity, holder.tv_commo2_content,
//                                mCommoBeanList.get(i).getChild().get(0).getReplies_content()));
//                holder.tv_commo3_time
//                        .setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoBeanList.get(i).getChild().get(0).getDateline() + "000")));
            }

        } else {
            holder.ll_more_commo_item.setVisibility(View.GONE);
        }
        return view;
    }

    /*存放控件 的ViewHolder*/
    public final class ViewHolder {
        public TextView tv_user_name;
        private TextView tv_commo2_name;
        //private TextView tv_commo2_content;
//      private TextView tv_commo2_time;
        private ImageView iv_banzhu;
        private ImageView iv_user_medal;
        private TextView tv_commo3_name;
        //private TextView tv_commo3_content;
//        private TextView tv_commo3_time;
        private TextView tv_louzhu;
        private TextView tv_zan_num;
        private TextView tv_reply_content;
        private TextView tv_commo_time;
        private ImageView iv_user_photo;
        private ImageView iv_zan;
        private TextView tv_floor_num;
        private TextView tv_commo2_more;
        private LinearLayout ll_root_image_item;
        private LinearLayout ll_more_commo_item;

        private RelativeLayout re_more_commo_item_bottom;
        private RelativeLayout re_more_commo_item_top;
        private LinearLayout item_commo_detail;
    }

/*
*
* 点赞请求接口*/

    private void getLuad(String replies_id, String re_uid) {
        OkHttpUtils.post()//
                .addParams("replies_id", replies_id)
                .addParams("uid", UserUtils.getUserId())
                .addParams("re_uid", re_uid)
                .url(XingYuInterface.REPLIES_LAUD)
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

}
