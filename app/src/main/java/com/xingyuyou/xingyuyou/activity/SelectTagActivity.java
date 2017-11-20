package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.community.TagBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class SelectTagActivity extends AppCompatActivity {

    private EditText mEtSelectTag;
    private RecyclerView mRlRootView;
    private ListView mListView;
    private Toolbar mToolbar;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {
                    createTag();
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Log.e("weiwei", "initData:22 " + ja.toString());
                    Gson gson = new Gson();
                    mTagList = gson.fromJson(ja.toString(),
                            new TypeToken<List<TagBean>>() {
                            }.getType());
                    mTagListAdapter.clear();
                    mTagListAdapter.addAll(mTagList);
                    if (!StringUtils.isEmpty(mTemTagName)) {
                        for (int i = 0; i < mTagListAdapter.size(); i++) {
                            if (mTagListAdapter.get(i).getLabel_name().equals(mTemTagName)) {
                                if (mTagAdapter != null) {
                                    mTagAdapter.notifyDataSetChanged();
                                }
                                return;
                            }
                        }
                        TagBean tagBean = new TagBean();
                        tagBean.setLabel_name(mTemTagName);
                        tagBean.setId("0");
                        mTagListAdapter.add(tagBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //创建空的标签

                }
                //更新UI
                if (mTagAdapter != null) {
                    mTagAdapter.notifyDataSetChanged();
                }
            }
            if (msg.what == 2) {

            }
        }
    };


    private List<TagBean> mTagList = new ArrayList();
    private List<TagBean> mTagListAdapter = new ArrayList();
    private List<TagBean> mSelectTagList = new ArrayList();
    private List<TagBean> mFilteredArrayList;
    private List<TagBean> mPublishArrayList = new ArrayList();
    private TagAdapter mTagAdapter;
    private SelectTagAdapter mSelectTagAdapter;
    private String mTemTagName;
    private String mInitRespomse;
    private List<TagBean> mMTagListReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);
        initToolbar();
        initView();
        initData();
    }

    private void initData() {
        //记录一下提交过的tags
        if (getIntent().getSerializableExtra("mPostTagsListReturn")!=null){
            ArrayList<TagBean> postTagsList = (ArrayList<TagBean>) getIntent().getSerializableExtra("mPostTagsListReturn");
            mSelectTagList.addAll(postTagsList);
            mSelectTagAdapter.notifyDataSetChanged();
        }
        OkHttpUtils.post()//
                .url(XingYuInterface.POPULAR_TAGS)
                .addParams("page", String.valueOf(1))
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mInitRespomse = response;
                        handler.obtainMessage(1, mInitRespomse).sendToTarget();
                    }
                });
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.select_tag_activity_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ab_send:
                        if (mSelectTagList.size() == 0) {
                            Toast.makeText(SelectTagActivity.this, "请至少选择一个标签", Toast.LENGTH_SHORT).show();
                        } else {
                            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                            for (TagBean tag : mSelectTagList) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("label_name", tag.getLabel_name());
                                map.put("id", tag.getId());
                                list.add(map);
                            }
                            JSONArray array = new JSONArray(list);

                            String type= getIntent().getStringExtra("type");
                            if(type.equals("1")){
                                Intent intent = new Intent(SelectTagActivity.this, PostingActivity.class);
                                intent.putExtra("PostTags", array.toString());
                                intent.putExtra("PostTagsList", (Serializable) mSelectTagList);
                                String[] strings = new String[mSelectTagList.size()];
                                for (int i = 0; i < mSelectTagList.size(); i++) {
                                    strings[i] = mSelectTagList.get(i).getLabel_name();
                                }
                                intent.putExtra("PostTagsString", strings);
                                startActivity(intent);
                                finish();
                            }else if(type.equals("2")){
                                Intent intent = new Intent(SelectTagActivity.this, VideoActivity.class);
                                intent.putExtra("PostTags", array.toString());
                                intent.putExtra("PostTagsList", (Serializable) mSelectTagList);
                                String[] strings = new String[mSelectTagList.size()];
                                for (int i = 0; i < mSelectTagList.size(); i++) {
                                    strings[i] = mSelectTagList.get(i).getLabel_name();
                                }
                                intent.putExtra("PostTagsString", strings);
                                startActivity(intent);
                                finish();
                            }else if(type.equals("3")){
                                Intent intent = new Intent(SelectTagActivity.this, VideoLongActivity.class);
                                intent.putExtra("PostTags", array.toString());
                                intent.putExtra("PostTagsList", (Serializable) mSelectTagList);
                                String[] strings = new String[mSelectTagList.size()];
                                for (int i = 0; i < mSelectTagList.size(); i++) {
                                    strings[i] = mSelectTagList.get(i).getLabel_name();
                                }
                                intent.putExtra("PostTagsString", strings);
                                startActivity(intent);
                                finish();
                            }
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initView() {
        mEtSelectTag = (EditText) findViewById(R.id.et_select_tags);
        initEditText();
        mListView = (ListView) findViewById(R.id.list_view);
        initListView();
        mRlRootView = (RecyclerView) findViewById(R.id.rl_root_tags);
        initRecycleView();
    }

    private void initEditText() {
        mEtSelectTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTemTagName = s.toString();
                if (StringUtils.isEmpty(s.toString())) {
                    mTagListAdapter.clear();
                    handler.obtainMessage(1, mInitRespomse).sendToTarget();
                } else {
                    inQuire(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void inQuire(String s) {
        //从数据库查询数据
        searchTag(s);
    }

    private void searchTag(String tagName) {
        OkHttpUtils.post()//
                .url(XingYuInterface.SEARCH_FORUM_LABEL)
                .addParams("page", String.valueOf(1))
                .addParams("label_keyword", tagName)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        handler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }

    private void createTag() {
        //查询完之后更新数据
        TagBean tagBean = new TagBean();
        tagBean.setLabel_name(mTemTagName);
        tagBean.setId("0");
        mTagListAdapter.clear();
        mTagListAdapter.add(tagBean);
        mTagAdapter.notifyDataSetChanged();
    }

    private void initRecycleView() {
        mRlRootView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mSelectTagAdapter = new SelectTagAdapter();
        mRlRootView.setAdapter(mSelectTagAdapter);
    }

    private void initListView() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSelectTagList.size() >= 5) {
                    Toast.makeText(SelectTagActivity.this, "只能选择5个标签", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (repeatQuery(mTagListAdapter.get(position).getLabel_name())) {
                    Toast.makeText(SelectTagActivity.this, "已经包含此标签", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mTagListAdapter.get(position).getLabel_name().contains(",")
                        || mTagListAdapter.get(position).getLabel_name().contains("，")) {
                    Toast.makeText(SelectTagActivity.this, "标签不能包含逗号", Toast.LENGTH_SHORT).show();
                    return;
                }

                mSelectTagList.add(mTagListAdapter.get(position));
                mEtSelectTag.setText("");
                mTagAdapter.notifyDataSetChanged();
                mSelectTagAdapter.notifyDataSetChanged();
            }

            private boolean repeatQuery(String s) {
                for (TagBean tag : mSelectTagList) {
                    if (tag.getLabel_name().equals(s)) {
                        return true;
                    }
                }
                return false;
            }
        });
        mTagAdapter = new TagAdapter();
        mListView.setAdapter(mTagAdapter);
    }

    private class TagAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTagListAdapter != null ? mTagListAdapter.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(SelectTagActivity.this).inflate(R.layout.item_simple_list, null);
                holder.title = (TextView) convertView.findViewById(R.id.tv_class_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (mTagListAdapter.get(position).getId() == "0") {
                holder.title.setText("创建标签：" + mTagListAdapter.get(position).getLabel_name());
                holder.title.setTextColor(getResources().getColor(R.color.colorAccent));
            } else {
                holder.title.setText(mTagListAdapter.get(position).getLabel_name());
                holder.title.setTextColor(getResources().getColor(R.color.black));
            }
            return convertView;
        }

    }

    public final class ViewHolder {
        public TextView title;
    }

    private class SelectTagAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(SelectTagActivity.this).inflate(R.layout.item_post_tag, parent, false);
            return new SelectTagAdapter.ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof SelectTagAdapter.ItemViewHolder) {
                if (mSelectTagList.size() != 0) {
                    if (position == 0) {
                        Drawable tagIcon = getResources().getDrawable(R.drawable.ic_action_tag_close);
                        Drawable tintIcon = DrawableCompat.wrap(tagIcon);
                        DrawableCompat.setTintList(tintIcon, getResources().getColorStateList(R.color.colorPrimary));
                        ((SelectTagAdapter.ItemViewHolder) holder).mClosePic.setImageDrawable(tintIcon);
                    }
                    if (position == 1) {
                        ((ItemViewHolder) holder).mPostTag.setBackgroundResource(R.drawable.tag_textview_color1_bg);
                        ((ItemViewHolder) holder).mPostTag.setTextColor(getResources().getColor(R.color.orange));
                        Drawable tagIcon = getResources().getDrawable(R.drawable.ic_action_tag_close);
                        Drawable tintIcon = DrawableCompat.wrap(tagIcon);
                        DrawableCompat.setTintList(tintIcon, getResources().getColorStateList(R.color.orange));
                        ((SelectTagAdapter.ItemViewHolder) holder).mClosePic.setImageDrawable(tintIcon);
                    }
                    if (position == 2) {
                        ((ItemViewHolder) holder).mPostTag.setBackgroundResource(R.drawable.tag_textview_color2_bg);
                        ((ItemViewHolder) holder).mPostTag.setTextColor(getResources().getColor(R.color.blue));
                        Drawable tagIcon = getResources().getDrawable(R.drawable.ic_action_tag_close);
                        Drawable tintIcon = DrawableCompat.wrap(tagIcon);
                        DrawableCompat.setTintList(tintIcon, getResources().getColorStateList(R.color.blue));
                        ((SelectTagAdapter.ItemViewHolder) holder).mClosePic.setImageDrawable(tintIcon);
                    }
                    if (position == 3) {
                        ((ItemViewHolder) holder).mPostTag.setBackgroundResource(R.drawable.tag_textview_color3_bg);
                        ((ItemViewHolder) holder).mPostTag.setTextColor(getResources().getColor(R.color.blue_pressed));
                        Drawable tagIcon = getResources().getDrawable(R.drawable.ic_action_tag_close);
                        Drawable tintIcon = DrawableCompat.wrap(tagIcon);
                        DrawableCompat.setTintList(tintIcon, getResources().getColorStateList(R.color.blue_pressed));
                        ((SelectTagAdapter.ItemViewHolder) holder).mClosePic.setImageDrawable(tintIcon);
                    }
                    if (position == 4) {
                        ((ItemViewHolder) holder).mPostTag.setBackgroundResource(R.drawable.tag_textview_color4_bg);
                        ((ItemViewHolder) holder).mPostTag.setTextColor(getResources().getColor(R.color.grey));
                        Drawable tagIcon = getResources().getDrawable(R.drawable.ic_action_tag_close);
                        Drawable tintIcon = DrawableCompat.wrap(tagIcon);
                        DrawableCompat.setTintList(tintIcon, getResources().getColorStateList(R.color.grey));
                        ((SelectTagAdapter.ItemViewHolder) holder).mClosePic.setImageDrawable(tintIcon);
                    }
                    ((ItemViewHolder) holder).mPostTag.setText(mSelectTagList.get(position).getLabel_name());
                    ((SelectTagAdapter.ItemViewHolder) holder).mClosePic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mTagListAdapter.add(mSelectTagList.get(position));
                            mSelectTagList.remove(position);
                            mTagAdapter.notifyDataSetChanged();
                            mSelectTagAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return mSelectTagList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private ImageView mClosePic;
            private TextView mPostTag;

            public ItemViewHolder(View itemView) {
                super(itemView);
                mClosePic = (ImageView) itemView.findViewById(R.id.iv_close);
                mPostTag = (TextView) itemView.findViewById(R.id.tv_post_tag);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }


}
