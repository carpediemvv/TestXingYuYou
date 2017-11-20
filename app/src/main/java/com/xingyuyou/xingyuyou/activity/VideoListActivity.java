package com.xingyuyou.xingyuyou.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.TimeChange;
import com.xingyuyou.xingyuyou.Utils.UIThreadUtils;
import com.xingyuyou.xingyuyou.bean.VideoInfo;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {
    private GridView mListView;
    private TextView noData;
    private Button btnBack;
    private List<VideoInfo> bit = new ArrayList<VideoInfo>();
    private Intent lastIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        initToolBar();
        initView();
        //btnBack = (Button) findViewById(R.id.btn_back);
        lastIntent = getIntent();
        new Search_photo().start();
      //  new initData().start();

    }

    private  void initView(){
        mListView = (GridView) findViewById(R.id.lv_video);
        noData = (TextView) findViewById(R.id.tv_nodata);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("ShowToast")
        public void handleMessage(android.os.Message msg) {
            if (bit.size() == 0) {
                mListView.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
            } else {
                VideoListAdapter adapter = new VideoListAdapter(VideoListActivity.this);
                mListView.setAdapter(adapter);
            }
            mListView.setOnItemClickListener(new ItemClick());

      /*    if (msg.what == 1&&bit!=null) {
                adapter = new VideodetailListviewAdapter(MainActivity.this, bit);
                Toast.makeText(MainActivity.this, "视频总数:"+bit.size(), 1);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        if(bit.size()!=0){
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            String type = "video*//*";
                            Uri uri = Uri.parse("file://"+bit.get(arg2).getUri());
                            intent.setDataAndType(uri, type);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, bit.get(arg2).getUri(), 1000).show();
                            System.out.println("sss: "+bit.get(arg2).getUri());
                        }
                    }
                });
            }*/
        }
    };
    private void initToolBar() {
     Toolbar   mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("选择视频");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


  /*  class initData extends Thread {
        @Override
        public void run() {
            super.run();
            vList=new ArrayList<VideoInfo>();
            String[] mediaColumns = new String[]{MediaStore.MediaColumns.DATA,
                    BaseColumns._ID, MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.MIME_TYPE,
                    MediaStore.Video.VideoColumns.DURATION, MediaStore.MediaColumns.SIZE};
            Cursor cursor = getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns,
                    null, null, null);
            if(cursor.moveToFirst())
            {
                do {
                    VideoInfo info = new VideoInfo();
                    info.setFilePath(cursor.getString(cursor
                            .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
        *//*        info.setMimeType(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)));*//*
//                info.setTitle(cursor.getString(cursor
//                        .getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)));
                    info.setTime(CommTools.LongToHms(cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))));
                    info.setSize(CommTools.LongToPoint(cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE))));
                    int id = cursor.getInt(cursor
                            .getColumnIndexOrThrow(BaseColumns._ID));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inDither = false;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    info.setB(MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), id,
                            MediaStore.Images.Thumbnails.MICRO_KIND, options));

                    vList.add(info);
                } while (cursor.moveToNext());
            }


            if (cursor != null) {
                cursor.close();
                mHandler.sendEmptyMessage(1);
            }


        }



    }*/


    private static Bitmap getVideoThumbnail(int id, ContentResolver mContentResolver) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(mContentResolver, id, MediaStore.Images.Thumbnails.MICRO_KIND, options);
        return bitmap;
    }


    class Search_photo extends Thread {
        @Override
        public void run() {
            // 如果有sd卡（外部存储卡）
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                Uri originalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = VideoListActivity.this.getApplicationContext().getContentResolver();
                Cursor cursor = cr.query(originalUri, null, null, null, null);
                if (cursor == null) {
                    return;
                }
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    //获取当前Video对应的Id，然后根据该ID获取其缩略图的uri
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
             /*       String[] selectionArgs = new String[] { id + "" };
                    String[] thumbColumns = new String[] { MediaStore.Video.Thumbnails.DATA,
                            MediaStore.Video.Thumbnails.VIDEO_ID };
                    String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=?";
                    String uri_thumb = "";
                    Cursor thumbCursor = (VideoListActivity.this.getApplicationContext().getContentResolver()).query(
                            MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs,
                            null);
                    if (thumbCursor != null && thumbCursor.moveToFirst()) {
                        uri_thumb = thumbCursor
                                .getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                    }*/
                    final Bitmap bitmapp = getVideoThumbnail(id, cr);
                    VideoInfo bitmapEntity = new VideoInfo(title, path, size, bitmapp, duration);
                 //   VideoInfo bitmapEntity1 = new VideoInfo(path, null, uri_thumb, null, duration,size);
                    bit.add(bitmapEntity);
                }
                if (cursor != null) {
                    cursor.close();
                    mHandler.sendEmptyMessage(1);
                }
            }
        }
    }


    private class ItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mListView.getItemAtPosition(position);
            String filePath = bit.get(position).getUri();
            String  s= TimeChange.bytes2kb(bit.get(position).getSize())+"";
            String ss= s;
            lastIntent.putExtra("path", filePath);
            lastIntent.putExtra("size", ss);
            setResult(102, lastIntent);
            finish();
/*            Bundle b = new Bundle();
            b.putParcelable("bitmap",vList.get(position).getB());
            lastIntent.putExtras(b);
            setResult(102, lastIntent);
            finish();*/
        }
    }

    class VideoListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public VideoListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return bit.size();
        }

        @Override
        public Object getItem(int p) {
            return bit.get(p);
        }

        @Override
        public long getItemId(int p) {
            return p;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_video_list, null);
                holder.vImage = (ImageView) convertView.findViewById(R.id.video_img);
                holder.vTime = (TextView) convertView.findViewById(R.id.video_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //holder.vImage.setImageBitmap(vList.get(position).getB());
            holder.vTime.setText(TimeChange.setTime(bit.get(position).getDuration()));
            if (UIThreadUtils.isMainThread())
       /*         Glide.with(mContext)
                        .load("file://" +bit.get(position).getUri_thumb())
                        .thumbnail(0.1f)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.vImage);*/
            //加载图片
                holder.vImage.setImageBitmap(bit.get(position).getUri_thumb());
      /*      holder.vImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String bpath = "file://" + vList.get(position).getFilePath();
                    intent.setDataAndType(Uri.parse(bpath), "video*//*");
                    Bundle b = new Bundle();
                    b.putParcelable("bitmap",vList.get(position).getB());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });*/
            return convertView;
        }
        class ViewHolder {
            ImageView vImage;
            TextView vTime;
        }
    }

}
