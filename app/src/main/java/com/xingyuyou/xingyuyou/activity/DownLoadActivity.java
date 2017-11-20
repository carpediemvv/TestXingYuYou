package com.xingyuyou.xingyuyou.activity;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppUtils;
import com.xingyuyou.xingyuyou.Utils.ConstUtils;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.download.DownloadInfo;
import com.xingyuyou.xingyuyou.download.DownloadManager;
import com.xingyuyou.xingyuyou.download.DownloadState;
import com.xingyuyou.xingyuyou.download.DownloadViewHolder;
import com.xingyuyou.xingyuyou.weight.HorizontalProgressBarWithTextProgress;


import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

public class DownLoadActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTextView;
    private ListView mListView;
    private DownloadManager downloadManager;
    private DownloadListAdapter downloadListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        initView();
        initData();

    }
    @Override
    protected void onResume() {
        super.onResume();
        DCAgent.resume(this);
        DCPage.onEntry("DownLoadActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("DownLoadActivity");
    }
    private void initData() {

    }
    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("下载管理");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mListView = (ListView) findViewById(R.id.lv_download);
        mListView.setDividerHeight(0);
        mTextView = (TextView) findViewById(R.id.empty_list_view);
        downloadManager = DownloadManager.getInstance();
        downloadListAdapter = new DownloadListAdapter();
        mListView.setAdapter(downloadListAdapter);
    }
    private class DownloadListAdapter extends BaseAdapter {

        private Context mContext;
        private final LayoutInflater mInflater;

        private DownloadListAdapter() {
            mContext = getBaseContext();
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            if (downloadManager == null) return 0;
            return downloadManager.getDownloadListCount();
        }

        @Override
        public Object getItem(int i) {
            return downloadManager.getDownloadInfo(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            DownloadItemViewHolder holder = null;
            DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
            if (view == null) {
                view = mInflater.inflate(R.layout.item_download_app_list, null);
                holder = new DownloadItemViewHolder(view, downloadInfo);
                view.setTag(holder);
                holder.refresh();
            } else {
                holder = (DownloadItemViewHolder) view.getTag();
                holder.update(downloadInfo);
            }

            if (downloadInfo.getState().value() < DownloadState.FINISHED.value()) {
                try {
                    downloadManager.startDownload(
                            downloadInfo.getUrl(),
                            downloadInfo.getGamePicUrl(),
                            downloadInfo.getPackageName(),
                            downloadInfo.getLabel(),
                            downloadInfo.getGameSize(),
                            downloadInfo.getGameIntro(),
                            downloadInfo.getFileSavePath(),
                            downloadInfo.isAutoResume(),
                            downloadInfo.isAutoRename(),
                            holder);
                } catch (DbException ex) {
                    Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
                }
            }

            return view;
        }
    }



    public class DownloadItemViewHolder extends DownloadViewHolder {
        @ViewInject(R.id.app_name)
        TextView label;
        @ViewInject(R.id.app_pic)
        ImageView gamePic;
        @ViewInject(R.id.tv_download_info)
        TextView tv_download_info;
        @ViewInject(R.id.bt_uninstall)
        Button stopBtn;
        @ViewInject(R.id.download_remove_btn)
        Button download_remove_btn;
        @ViewInject(R.id.relativeLayout)
        RelativeLayout relativeLayout;

        public DownloadItemViewHolder(View view, DownloadInfo downloadInfo) {
            super(view, downloadInfo);
            refresh();
        }

        @Event(R.id.bt_uninstall)
        private void toggleEvent(View view) {
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                    downloadManager.stopDownload(downloadInfo);
                    break;
                case ERROR:
                case STOPPED:
                    try {
                        downloadManager.startDownload(
                                downloadInfo.getUrl(),
                                downloadInfo.getGamePicUrl(),
                                downloadInfo.getPackageName(),
                                downloadInfo.getLabel(),
                                downloadInfo.getGameSize(),
                                downloadInfo.getGameIntro(),
                                downloadInfo.getFileSavePath(),
                                downloadInfo.isAutoResume(),
                                downloadInfo.isAutoRename(),
                                this);
                    } catch (DbException ex) {
                        Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                case FINISHED:
                    if (AppUtils.isInstallApp(DownLoadActivity.this,downloadInfo.getPackageName())) {
                        stopBtn.setText("打开");
                        AppUtils.launchApp(DownLoadActivity.this, downloadInfo.getPackageName());
                    } else {
                        stopBtn.setText("安装");
                        AppUtils.installApp(DownLoadActivity.this, downloadInfo.getFileSavePath());
                    }
                    break;
                default:
                    break;
            }
        }

        @Event(R.id.iv_more_action)
        private void dialogEvent(View view) {
            download_remove_btn.setVisibility(View.VISIBLE);
        }
        @Event(R.id.relativeLayout)
        private void dissdialogEvent(View view) {
            download_remove_btn.setVisibility(View.INVISIBLE);
        }
        @Event(R.id.download_remove_btn)
        private void removeEvent(View view) {
            try {
                downloadManager.removeDownload(downloadInfo);
                downloadListAdapter.notifyDataSetChanged();
            } catch (DbException e) {
                Toast.makeText(x.app(), "移除任务失败", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void update(DownloadInfo downloadInfo) {
            super.update(downloadInfo);
            Log.e("wei", "update");
            refresh();
        }

        @Override
        public void onWaiting() {
            Log.e("wei", "onWaiting");
            refresh();
        }

        @Override
        public void onStarted() {
            Log.e("wei", "onStarted");
            refresh();
        }

        @Override
        public void onLoading(long total, long current) {
            Log.e("wei", "onLoading" + "total:" + total + "current" + current);
            refresh();
        }

        @Override
        public void onSuccess(File result) {
            Log.e("wei", "onSuccess");
            AppUtils.installApp(DownLoadActivity.this, downloadInfo.getFileSavePath());
            refresh();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            Log.e("wei", "onError");
            refresh();
        }

        @Override
        public void onCancelled(Callback.CancelledException cex) {
            Log.e("wei", "onCancelled");
            refresh();
        }

        public void refresh() {
            label.setText(downloadInfo.getLabel());
            Glide.with(getApplicationContext()).load(downloadInfo.getGamePicUrl()).into(gamePic);
            stopBtn.setVisibility(View.VISIBLE);
            stopBtn.setText(x.app().getString(R.string.stop));
            tv_download_info.setText(downloadInfo.getProgress()+"%"+"("+
                    ConvertUtils.byte2FitMemorySize(downloadInfo.getFileLength()*downloadInfo.getProgress()/100)
                    +"/"+ConvertUtils.byte2FitMemorySize(downloadInfo.getFileLength())+")");
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                    stopBtn.setText(x.app().getString(R.string.stop));
                    break;
                case ERROR:
                case STOPPED:
                    tv_download_info.setText("等待下载");
                    stopBtn.setText(x.app().getString(R.string.start));
                    break;
                case FINISHED:
                    tv_download_info.setText("下载完成");
                    stopBtn.setText("完成");
                    if (AppUtils.isInstallApp(getBaseContext(),downloadInfo.getPackageName())){
                        stopBtn.setText("打开");
                       // AppUtils.launchApp(getBaseContext(), downloadInfo.getPackageName());
                    }else {
                        stopBtn.setText("安装");
                        //AppUtils.installApp(getBaseContext(), downloadInfo.getFileSavePath());
                    }
                    break;
                default:
                    stopBtn.setText(x.app().getString(R.string.start));
                    break;
            }
        }
    }
}
