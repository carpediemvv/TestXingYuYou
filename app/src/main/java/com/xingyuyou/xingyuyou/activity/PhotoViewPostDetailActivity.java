package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.gson.Gson;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.bean.community.PostDetailBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import okhttp3.Call;

public class PhotoViewPostDetailActivity extends AppCompatActivity {

    private ViewPager mPager;
    private TextView mTv_image_number;

    private ImageView mIv_save_image;
    private int mPosition = 0;
    private PostDetailBean mPostDetailBean;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        //获取信息
        initData();
        mTv_image_number = (TextView) findViewById(R.id.tv_image_number);

        //保存图片
        mIv_save_image = (ImageView) findViewById(R.id.iv_save_image);
        mIv_save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage();
            }
        });
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mPager.setAdapter(pagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                mTv_image_number.setText((position + 1)
                        + "/" +
                        mPostDetailBean.getThumbnail_image().size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setCurrentItem(getIntent().getIntExtra("position", 0));

    }

    private void initData() {
        JSONObject jo = null;
        try {
            jo = new JSONObject(getIntent().getStringExtra("postDetailBean"));
            JSONObject ja = jo.getJSONObject("data");
            Gson gson = new Gson();
            mPostDetailBean = gson.fromJson(ja.toString(), PostDetailBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void downloadImage() {
        File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        final String s = outDir.getAbsolutePath() + "/JiaoNang";
        Toast.makeText(PhotoViewPostDetailActivity.this, "下载开始", Toast.LENGTH_SHORT).show();
        OkHttpUtils//
                .get()//
                .url(mPostDetailBean.getThumbnail_image()
                        .get(mPosition).getThumbnail_image())//
                .build()//
                .execute(new FileCallBack(s, mPostDetailBean.getThumbnail_image()
                        .get(mPosition).getThumbnail_image().substring(
                                mPostDetailBean.getThumbnail_image().get(mPosition).getThumbnail_image().length() - 15,
                                mPostDetailBean.getThumbnail_image().get(mPosition).getThumbnail_image().length()))//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PhotoViewPostDetailActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        try {
                            MediaStore.Images.Media.insertImage(getContentResolver(), response.getAbsolutePath(), response.getName(), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                       sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(response)));
                    }

                    @Override
                    public void inProgress(final float progress, long total, int id) {
                        super.inProgress(progress, total, id);

                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        Toast.makeText(PhotoViewPostDetailActivity.this, "下载完成，目录:" + s, Toast.LENGTH_SHORT).show();
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(s))));
                    }
                });
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mPostDetailBean.getThumbnail_image().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(PhotoViewPostDetailActivity.this).inflate(R.layout.item_photo_view_pager, null);
            final PhotoView photoView = (PhotoView) view.findViewById(R.id.PhotoView);
            final ProgressBar loading_progressBar = (ProgressBar) view.findViewById(R.id.loading_progressBar);
            final TextView  mTv_image_real = (TextView) view.findViewById(R.id.tv_image_real);
            photoView.enable();
            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ViewTarget<PhotoView, GlideDrawable> viewTarget = new ViewTarget<PhotoView, GlideDrawable>(photoView) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    this.view.setImageDrawable(resource.getCurrent());
                }
            };

            //加载图片的target


            if(mPostDetailBean.getThumbnail_image().get(position).getThumbnail_image().indexOf("gif")!=-1){
                Glide.with(PhotoViewPostDetailActivity.this)
                        .load(mPostDetailBean.getThumbnail_image().get(position).getThumbnail_image())
                        .asGif()
                        .listener(new RequestListener<String, GifDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                                loading_progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                loading_progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(photoView);
                         mTv_image_real.setVisibility(View.GONE);
            }else {
                Glide.with(PhotoViewPostDetailActivity.this)
                        .load(mPostDetailBean.getThumbnail_image().get(position).getThumbnail_image())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                loading_progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                loading_progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(viewTarget);


                mTv_image_real.setVisibility(View.VISIBLE);
                mTv_image_real.setText("查看原图(" +
                        ConvertUtils.byte2FitMemorySize(mPostDetailBean.getPosts_reset_image()
                                .get(position).getPosts_image_size())
                        + ")");
                mTv_image_real.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downloadImage(position,view,photoView);
                    }
                });


            }

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };

    private void downloadImage(final int position, final View view, final PhotoView photoView) {
        OkHttpUtils//
                .get()//
                .url(mPostDetailBean.getPosts_reset_image()
                        .get(position).getPosts_image())//
                .build()//
                .execute(new FileCallBack(FileUtils.imageSavePath, mPostDetailBean.getPosts_reset_image()
                        .get(position).getPosts_image().substring(
                                mPostDetailBean.getPosts_reset_image().get(position).getPosts_image().length() - 15,
                                mPostDetailBean.getPosts_reset_image().get(position).getPosts_image().length()))//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PhotoViewPostDetailActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(File response, int id) {

                    }

                    @Override
                    public void inProgress(final float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView)view).setText(((int)(progress*100))+"%");
                            }
                        });
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        ViewTarget<PhotoView, GlideDrawable> viewTarget = new ViewTarget<PhotoView, GlideDrawable>(photoView) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                this.view.setImageDrawable(resource.getCurrent());
                            }
                        };
                        Glide.with(PhotoViewPostDetailActivity.this)
                                .load(FileUtils.imageSavePath+ mPostDetailBean.getPosts_reset_image()
                                        .get(position).getPosts_image().substring(
                                                mPostDetailBean.getPosts_reset_image().get(position).getPosts_image().length() - 15,
                                                mPostDetailBean.getPosts_reset_image().get(position).getPosts_image().length()))
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(viewTarget);
                        view.setVisibility(View.INVISIBLE);
                    }
                });
    }
}
