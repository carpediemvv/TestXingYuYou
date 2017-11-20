package com.xingyuyou.xingyuyou.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dataeye.sdk.api.app.DCAgent;
import com.dataeye.sdk.api.app.channel.DCPage;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.Utils.ImageUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideLoader;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.IdListBean;
import com.xingyuyou.xingyuyou.bean.community.TagBean;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xingyuyou.xingyuyou.activity.PersonActivity.KEY_CID;
import static com.xingyuyou.xingyuyou.activity.PersonActivity.KEY_NAME;

public class PostingActivity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {
    private static final int REQUEST_IMAGE = 2;
    private static final int TYPE_FOOTER = 21;
    private static final int REQUEST_CODE = 3;
    public static  final int REQUEST_CODE_IMAGE = 1000;
    private Toolbar mToolbar;
    private CustomDialog mDialog;
    private RelativeLayout mRlTagMore;
    private EditText mStTitle;
    private EditText mEtContent;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mImageList = new ArrayList();
    private ImageAdapter mImageAdapter;
    private String mPostTags;
    private RelativeLayout mRlCommMore;
    private String mPostCommId;
    private Map map = new HashMap<String, String>();
    private static final int CODE_PERSON = 1;
    private PopupWindow mPopWindow;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                mPopWindow.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);
            }
        }
    };
    private String[] PostCommClassName;
    private String mPostTagsReturn;
    private List<TagBean> mPostTagsList;
    private String[] mPostTagsString;
    private ImageView post_aite;
    private List<IdListBean> idListBeen =new ArrayList<>();
    /**
     * 存储@的cid、name对
     */
    private Map<String, String> cidNameMap = new HashMap<String, String>();

    private String nameStr;
    private ArrayList<String> idStr=new ArrayList<>();
    List<Map<String, Object>> list_map = new ArrayList<Map<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //捕获那个不知道原因的异常  上面实现的UncaughtExceptionHandler就是为这个
        Thread.setDefaultUncaughtExceptionHandler(this);
        setContentView(R.layout.activity_posting);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRlTagMore = (RelativeLayout) findViewById(R.id.rl_tag_more);
        mRlCommMore = (RelativeLayout) findViewById(R.id.rl_comm_more);
        mStTitle = (EditText) findViewById(R.id.et_title);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        post_aite = (ImageView) findViewById(R.id.post_aite);
        initRecyclerView();
        initToolbar();
        initTagMore();
        initCommMore();
        popupWin();
        mEtContent.setFilters(new InputFilter[] { new MyInputFilter() });
        post_aite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostingActivity.this, PersonActivity.class);
                startActivityForResult(intent, CODE_PERSON);
            }
        });
    }

    private void popupWin() {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_post_announcement_layout, null);
        mPopWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setBackgroundDrawable(getResources().getDrawable(R.color.custom_dark_gray));
        mPopWindow.setOutsideTouchable(true);
        ImageView iv_post_announcement = (ImageView) popupView.findViewById(R.id.iv_post_announcement);
        iv_post_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopWindow.dismiss();
            }
        });
        mHandler.sendEmptyMessageDelayed(2, 200);
    }

    private void initCommMore() {
        mRlCommMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PostingActivity.this, SelectCommTagActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mImageAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mImageAdapter);
    }

    private void initTagMore() {
        mRlTagMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostingActivity.this, SelectTagActivity.class);
                intent.putExtra("mPostTagsListReturn", (Serializable) mPostTagsList);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
    }

    private void initToolbar() {
        mToolbar.setTitle("发布帖子");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.post_activity_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ab_send:
                        mDialog = new CustomDialog(PostingActivity.this);
                        mDialog.ProgressDialog(PostingActivity.this, "正在上传，请稍后");
                        mDialog.showDialog();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dealEditData();
                            }
                        }, 200);

                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (!StringUtils.isEmpty(intent.getStringExtra("PostTags"))) {
            mPostTagsReturn = intent.getStringExtra("PostTags");
            mPostTagsList = (ArrayList<TagBean>) intent.getSerializableExtra("PostTagsList");
            map.put("PostTags", mPostTagsReturn);
            mPostTagsString = intent.getStringArrayExtra("PostTagsString");
            TextView tv_select_tag = (TextView) findViewById(R.id.tv_select_tag);
            TextView tv_tag_one = (TextView) findViewById(R.id.tv_tag_one);
            tv_tag_one.setText("");
            tv_tag_one.setVisibility(View.INVISIBLE);
            TextView tv_tag_two = (TextView) findViewById(R.id.tv_tag_two);
            tv_tag_two.setText("");
            tv_tag_two.setVisibility(View.INVISIBLE);
            TextView tv_tag_three = (TextView) findViewById(R.id.tv_tag_three);
            tv_tag_three.setText("");
            tv_tag_three.setVisibility(View.INVISIBLE);
            TextView tv_tag_four = (TextView) findViewById(R.id.tv_tag_four);
            tv_tag_four.setText("");
            tv_tag_four.setVisibility(View.INVISIBLE);
            TextView tv_tag_five = (TextView) findViewById(R.id.tv_tag_five);
            tv_tag_five.setText("");
            tv_tag_five.setVisibility(View.INVISIBLE);
            for (int i = 0; i < mPostTagsString.length; i++) {
                if (i == 0) {
                    tv_select_tag.setVisibility(View.GONE);
                    tv_tag_one.setText(mPostTagsString[0]);
                    tv_tag_one.setVisibility(View.VISIBLE);
                }
                if (i == 1) {
                    tv_tag_two.setText(mPostTagsString[1]);
                    tv_tag_two.setVisibility(View.VISIBLE);
                }
                if (i == 2) {
                    tv_tag_three.setText(mPostTagsString[2]);
                    tv_tag_three.setVisibility(View.VISIBLE);
                }
                if (i == 3) {
                    tv_tag_four.setText(mPostTagsString[3]);
                    tv_tag_four.setVisibility(View.VISIBLE);
                }
                if (i == 4) {
                    tv_tag_five.setText(mPostTagsString[4]);
                    tv_tag_five.setVisibility(View.VISIBLE);
                }
            }
        }

        if (!StringUtils.isEmpty(intent.getStringExtra("GsonNameId"))) {
            map.put("GsonNameId", intent.getStringExtra("GsonNameId"));
            PostCommClassName=intent.getStringArrayExtra("PostCommClassName");
            TextView tv_select_commu = (TextView) findViewById(R.id.tv_select_commu);
            TextView tv_tag_commu = (TextView) findViewById(R.id.tv_tag_commu);
            tv_tag_commu.setText("");
            tv_tag_commu.setVisibility(View.INVISIBLE);
            TextView tv_tag_commu2 = (TextView) findViewById(R.id.tv_tag_commu2);
            tv_tag_commu2.setText("");
            tv_tag_commu2.setVisibility(View.INVISIBLE);
            TextView tv_tag_commu3 = (TextView) findViewById(R.id.tv_tag_commu3);
            tv_tag_commu3.setText("");
            tv_tag_commu3.setVisibility(View.INVISIBLE);

            for(int i = 0; i < PostCommClassName.length; i++){
                if (i == 0) {
                    tv_select_commu.setVisibility(View.GONE);
                    tv_tag_commu.setVisibility(View.VISIBLE);
                    tv_tag_commu.setText(PostCommClassName[0]);
                }
                if (i == 1) {
                    tv_tag_commu2.setVisibility(View.VISIBLE);
                    tv_tag_commu2.setText(PostCommClassName[1]);
                }
                if (i == 2) {
                    tv_tag_commu3.setVisibility(View.VISIBLE);
                    tv_tag_commu3.setText(PostCommClassName[2]);
                }


            }

        }
    }
    /**
     * 负责处理编辑数据提交等事宜
     */
    protected void dealEditData() {
        if (StringUtils.isEmpty(mStTitle.getText().toString().trim())) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            mDialog.dismissDialog();
            return;
        }
       /* if (StringUtils.isEmpty(mEtContent.getText().toString().trim())) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }*/

        if (StringUtils.isEmpty((String) map.get("PostTags"))) {
            Toast.makeText(this, "请选择标签", Toast.LENGTH_SHORT).show();
            mDialog.dismissDialog();
            return;
        }
        if (StringUtils.isEmpty((String) map.get("GsonNameId"))) {
            Toast.makeText(this, "请选发帖社区", Toast.LENGTH_SHORT).show();
            mDialog.dismissDialog();
            return;
        }
        if (mImageList.size() == 0) {
            Toast.makeText(this, "请至少选择一张图片", Toast.LENGTH_SHORT).show();
            mDialog.dismissDialog();
            return;
        }
        // params.put("fid", "0");
        //params.put("uid", "258");
        String s1 = StringUtils.isEmpty(mEtContent.getText().toString().trim()) == true ? "" : mEtContent.getText().toString().trim();
        if(idListBeen.size()!=0){
            String replies_content = s1.trim();
            if(replies_content.trim()!=null||replies_content.length()!=0){
                String[] split = replies_content.split(" ");
                for (int j = 0; j <split.length; j++) {
                    if(split[j].trim().length()!=0){
                        String substring = split[j].substring(0,1);
//                            存在
                        if(substring.equals("@")||substring.equals("＠")){
                            Map<String, Object> map = new HashMap<String, Object>();
                            String s2 = split[j].toString();
                            String s = cidNameMap.get(s2);
                            map.put("re_uid",s);
                            list_map.add(map);
                        }
                    }
                }
            }
        }
        JSONArray array = new JSONArray(list_map);
        Map<String, String> params = new HashMap<String, String>();
        params.put("fid", (String) map.get("GsonNameId"));
        params.put("uid", UserUtils.getUserId());
        params.put("subject", mStTitle.getText().toString().trim());
        params.put("message", StringUtils.isEmpty(mEtContent.getText().toString().trim()) == true ? "" : mEtContent.getText().toString().trim());
        params.put("tags", (String) map.get("PostTags"));
        params.put("post_type","1");
        params.put("relation", array.toString());
        PostFormBuilder post = OkHttpUtils.post();
        for (int i = 0; i < mImageList.size(); i++) {
            File file = new File(mImageList.get(i));
            if (file.exists()) {
                //File file1 = new File(getExternalCacheDir() + "/tempCompress" + i + ".jpg");
                //NativeUtil.compressBitmap(mImageList.get(i), file1.getAbsolutePath());
                //以上是压缩代码
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                String type = options.outMimeType;
                if (type.equals("image/webp")) {
                    //FileUtils.saveFile(FileUtils.imageSavePath+"/tempCompress" ,i + ".jpg",ImageUtils.getBitmap(file));
                    ImageUtils.save(ImageUtils.getBitmap(file), FileUtils.imageSavePath + "/tempCompress" + i + ".jpg", Bitmap.CompressFormat.JPEG);
                    String s = "posts_image";
                    post.addFile(s + i, file.getName(), new File(FileUtils.imageSavePath + "/tempCompress" + i + ".jpg"));
                } else {
                    String s = "posts_image";
                    post.addFile(s + i, file.getName(), file);
                }
            }
        }

        post.url(XingYuInterface.POST_POSTS_TEST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        mDialog.dismissDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mDialog.dismissDialog();
                        Intent intent = new Intent("updateFragment");
                        LocalBroadcastManager.getInstance(PostingActivity.this)
                                .sendBroadcast(intent);
                        Toast.makeText(PostingActivity.this, "发帖成功", Toast.LENGTH_SHORT).show();
                        PostingActivity.this.finish();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        mDialog.setProgressDialog(((int) (progress * 100)));
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==REQUEST_CODE_IMAGE) {
            if (resultCode == RESULT_OK) {
                List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                mImageList.clear();
                mImageList.addAll(pathList);
                mImageAdapter.notifyDataSetChanged();
            }
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CODE_PERSON:
                //获取来的id和name集合
                if(data.getStringArrayListExtra(KEY_CID)!=null) {
                    ArrayList<String> tmpCidStr = data.getStringArrayListExtra(KEY_CID);
                    ArrayList<String> tmpNameStr = data.getStringArrayListExtra(KEY_NAME);
                    ArrayList<String> tmpName = data.getStringArrayListExtra("KEY_");

                    for (int i = 0; i < tmpCidStr.size(); i++) {
                        IdListBean idListBean =new IdListBean();
                        idListBean.setId(tmpCidStr.get(i));
                        idListBean.setName(tmpNameStr.get(i));
                        idListBeen.add(idListBean);
                    }
                    Collections.reverse(tmpNameStr);

                    if (tmpCidStr != null && tmpCidStr.size() > 0) {
                        for (int i = 0; i < tmpCidStr.size(); i++) {
                            if (tmpName.size() > i) {
                                cidNameMap.put(tmpName.get(i),tmpCidStr.get(i) );
                            }
                        }
                    }
                    for(int i=0;i<tmpNameStr.size();i++){
                        nameStr= tmpNameStr.get(i)+nameStr;
                    }
                    for(int i=0;i<tmpCidStr.size();i++) {
                        idStr.add(tmpCidStr.get(i));
                    }
                    //  lastNameStr = tmpNameStr;
                    // 获取光标当前位置
                    int curIndex = mEtContent.getSelectionStart();
                    // 把要@的人插入光标所在位置
                    for(String s:tmpNameStr){
                        mEtContent.getText().insert(curIndex, s);
                    }
                    // 通过输入@符号进入好友列表并返回@的人，要删除之前输入的@
                    if (curIndex >= 1) {
                        mEtContent.getText().replace(curIndex - 1, curIndex, "");
                    }
                    setAtImageSpan(nameStr);
                    break;
                }
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e("weiwei", "uncaughtException: " + throwable.toString());
    }

    private class ImageAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(PostingActivity.this).inflate(R.layout.item_post_image, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == TYPE_FOOTER) {
                ((ItemViewHolder) holder).mClosePic.setVisibility(View.GONE);
                ((ItemViewHolder) holder).mPostImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mImageList.size() >= 5) {
                            Toast.makeText(PostingActivity.this, "只能发布五张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        toCheckPermission(Manifest.permission.CAMERA);
                    }
                });
            }
            if (holder instanceof ItemViewHolder) {
                if (mImageList.size() != 0) {
                    if (getItemViewType(position) != TYPE_FOOTER) {
                        Glide.with(PostingActivity.this)
                                .load(mImageList.get(position))
                                .into(((ItemViewHolder) holder).mPostImage);
                        ((ItemViewHolder) holder).mClosePic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mImageList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                //最后一个,应该加载Footer
                return TYPE_FOOTER;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mImageList.size() == 0 ? 1 : mImageList.size() + 1;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private ImageView mClosePic;
            private ImageView mPostImage;

            public ItemViewHolder(View itemView) {
                super(itemView);
               /* if (itemView == mFooterView) {
                    return;
                }*/
                mClosePic = (ImageView) itemView.findViewById(R.id.iv_close);
                mPostImage = (ImageView) itemView.findViewById(R.id.iv_post_image);
            }
        }
    }

    /**
     * Manifest.permission.WRITE_EXTERNAL_STORAGE
     */
    private void toCheckPermission(final String permission) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                //进入到这里代表没有权限.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    //已经禁止提示了
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("申请权限")
                            .setMessage("就是胶囊需要这个权限，浏览您的设备图片、拍照等...")
                            .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(PostingActivity.this, new String[]{permission}, REQUEST_CODE);

                                }
                            })
                            .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(PostingActivity.this, "您已禁止该权限，如需请在设置中重新开启。", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE);
                }
            } else {
//                MultiImageSelector.create()
//                        .showCamera(true)
//                        .count(5-mImageList.size())
//                        .multi()
//                        .start(PostingActivity.this, REQUEST_IMAGE);

                ImageConfig imageConfig
                        = new ImageConfig.Builder(
                        // GlideLoader 可用自己用的缓存库
                        new GlideLoader())
                        // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                        .steepToolBarColor(getResources().getColor(R.color.black))
                        // 标题的背景颜色 （默认黑色）
                        .titleBgColor(getResources().getColor(R.color.black))
                        // 提交按钮字体的颜色  （默认白色）
                        .titleSubmitTextColor(getResources().getColor(R.color.white))
                        // 标题颜色 （默认白色）
                        .titleTextColor(getResources().getColor(R.color.white))
                        // 开启多选   （默认为多选）  (单选 为 singleSelect)
                        //                         .singleSelect()
                        .crop()
                        // 多选时的最大数量   （默认 9 张）
                        .mutiSelectMaxSize(5)
                        // 已选择的图片路径
                        .pathList(mImageList)
                        // 拍照后存放的图片路径（默认 /temp/picture）
                        .filePath("/ImageSelector/Pictures")
                        // 开启拍照功能 （默认开启）
                        .showCamera()
                        .requestCode(REQUEST_CODE_IMAGE)
                        .build();

                ImageSelector.open(PostingActivity.this, imageConfig);//开启图片选择器
            }
        }else {
            //进入到这里代表没有权限.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //已经禁止提示了
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("申请权限")
                        .setMessage("就是胶囊需要这个权限，下载游戏，发表图片等等...")
                        .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(PostingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

                            }
                        })
                        .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(PostingActivity.this, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }


    }

    private void setAtImageSpan(String nameStr) {
        String content = String.valueOf(mEtContent.getText());
        if (content.endsWith("@") || content.endsWith("＠")) {
            content = content.substring(0, content.length() - 1);
        }
        String tmp = content;

        SpannableString ss = new SpannableString(tmp);

        if (nameStr != null) {
            String[] names = nameStr.split(" ");
            if (names != null && names.length > 0) {
                for (String name : names) {
                    if (name != null && name.trim().length() > 0) {
                        final Bitmap bmp = getNameBitmap(name);
                        // 这里会出现删除过的用户，需要做判断，过滤掉
                        if (tmp.indexOf(name) >= 0
                                && (tmp.indexOf(name) + name.length()) <= tmp
                                .length()) {
                            // 把取到的要@的人名，用DynamicDrawableSpan代替
                            ss.setSpan(
                                    new DynamicDrawableSpan(
                                            DynamicDrawableSpan.ALIGN_BASELINE) {
                                        @Override
                                        public Drawable getDrawable() {
                                            // TODO Auto-generated method stub
                                            BitmapDrawable drawable = new BitmapDrawable(
                                                    getResources(), bmp);
                                            drawable.setBounds(0, 0,
                                                    bmp.getWidth(),
                                                    bmp.getHeight());
                                            return drawable;
                                        }
                                    }, tmp.indexOf(name),
                                    tmp.indexOf(name) + name.length(),
                                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }
        }
        mEtContent.setTextKeepState(ss);
    }

    /**
     * 把返回的人名，转换成bitmap
     *
     * @param name
     * @return
     */
    private Bitmap getNameBitmap(String name) {

		/* 把@相关的字符串转换成bitmap 然后使用DynamicDrawableSpan加入输入框中 */
        name = "" + name;
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.other_pink));
        paint.setAntiAlias(true);
        paint.setTextSize(38);
        Rect rect = new Rect();
        paint.getTextBounds(name, 0, name.length(), rect);
        // 获取字符串在屏幕上的长度
        int width = (int) (paint.measureText(name));

        final Bitmap bmp = Bitmap.createBitmap(width, rect.height(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawText(name, rect.left, rect.height() - rect.bottom, paint);

        return bmp;
    }
    @Override
    protected void onResume() {
        super.onResume();
        DCAgent.resume(this);
        DCPage.onEntry("PostingActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DCAgent.pause(this);
        DCPage.onExit("PostingActivity");
    }
    //监听edittext
    private class MyInputFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // TODO Auto-generated method stub
            if (source.toString().equalsIgnoreCase("@")
                    || source.toString().equalsIgnoreCase("＠")) {
                goAt();
            }

            return source;
        }
    }
    private void goAt() {
        StringBuffer tmp = new StringBuffer();
        // 把选中人的id已空格分隔，拼接成字符串
        for (Map.Entry<String, String> entry : cidNameMap.entrySet()) {
            tmp.append(entry.getKey() + " ");
        }

        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra(PersonActivity.KEY_SELECTED, tmp.toString());
        startActivityForResult(intent, CODE_PERSON);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意授权
                } else {
                    //用户拒绝授权
                    Toast.makeText(this, "您已拒绝调用相机权限，会导致无法拍摄图片", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
