package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.PinyinUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapters.SelectCommTagAdapter;
import com.xingyuyou.xingyuyou.bean.ChooseFriendBean;
import com.xingyuyou.xingyuyou.views.EditTextWithDel;
import com.xingyuyou.xingyuyou.views.PinyinComparatorChoose;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class PwersonActivity extends AppCompatActivity {
    private List<ChooseFriendBean.DataBean> sourceDateListt;

    private Toolbar mToolbar;
    private ListView mRecyclerView;
    private SelectCommTagAdapter mCommTagAdapter;
    private EditTextWithDel mEtSearchName;
    private List<ChooseFriendBean.DataBean> mDatas = new ArrayList<>();
    private List<ChooseFriendBean.DataBean> mDatass = new ArrayList<>();
    private List<ChooseFriendBean.DataBean> mLabelClassList=new ArrayList<>();
    private List<String> selectId_list=new ArrayList<>();
    private List<String> className_list=new ArrayList<>();
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    String string = jo.getString("status");
                    if (string.equals("1")){
                        JSONArray ja = jo.getJSONArray("data");
                        Gson gson = new Gson();
                        mLabelClassList = gson.fromJson(ja.toString(),
                                new TypeToken<List<ChooseFriendBean.DataBean>>() {
                                }.getType());
                        mDatas.addAll(mLabelClassList);
                        mDatass=mDatas;
                        setAdapter(mDatas);
                    }


                    //根据输入框输入值的改变来过滤搜索
                    mEtSearchName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                            filterData(s.toString(),sourceDateListt);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (mCommTagAdapter != null)
                    mCommTagAdapter.notifyDataSetChanged();
            }
        }
    };
    private ImageView image_choose;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_comm_tag);
        initToolBar();
        initDatas();
        initViews();
    }

    private void initViews() {
        mRecyclerView = (ListView) findViewById(R.id.recyclerView);
        mEtSearchName = (EditTextWithDel) findViewById(R.id.et_search);
        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                image_choose = (ImageView)view.findViewById(R.id.image_choose);
                ChooseFriendBean.DataBean bean=(ChooseFriendBean.DataBean) adapterView.getItemAtPosition(position);
                if(bean.getTag()==null){
                    if(className_list.size()<3){
                        image_choose.setTag("1");
                        strings(bean);
                        // image_choose.setImageResource(R.drawable.ic_choose);
                        // ((ChooseFriendBean.DataBean) adapterView.getItemAtPosition(position)).setTag("1");
                        //  sourceDateListt.get(position).setTag("1");
                        ((ChooseFriendBean.DataBean) mCommTagAdapter.getItem(position)).setTag("1");
                        mCommTagAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(PwersonActivity.this,"亲，最多选3个", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    selectId_list.remove(bean.getId());
                    className_list.remove(bean.getClass_name());
                    // image_choose.setImageResource(R.drawable.ic_notchoose);
                    image_choose.setTag(null);
                    ((ChooseFriendBean.DataBean) mCommTagAdapter.getItem(position)).setTag(null);
                    mCommTagAdapter.notifyDataSetChanged();
                }
            }
        });


    }
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectId_list.size()==0){
                    Toast.makeText(PwersonActivity.this, "请至少选择一个社区", Toast.LENGTH_SHORT).show();
                }else {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

                    for(int i=0;i<className_list.size();i++){
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id",selectId_list.get(i));
                        map.put("forum_name", className_list.get(i));
                        list.add(map);
                    }
                    JSONArray array = new JSONArray(list);
                    String type = getIntent().getStringExtra("type");
                    String[] strings = new String[className_list.size()];
                    for (int i = 0; i < className_list.size(); i++) {
                        strings[i] = className_list.get(i);
                    }
                    if (type.equals("1")) {
                        Intent intent = new Intent(PwersonActivity.this, PostingActivity.class);
                        //  intent.putExtra("PostCommId", mDatas.get(position).getId());
                        intent.putExtra("GsonNameId",array.toString());
                        intent.putExtra("PostCommClassName",strings);
                        startActivity(intent);
                        finish();
                    } else if (type.equals("2")) {
                        Intent intent = new Intent(PwersonActivity.this, VideoActivity.class);
                        //intent.putExtra("PostCommId", mDatas.get(position).getId());
                        intent.putExtra("GsonNameId",array.toString());
                        intent.putExtra("PostCommClassName",strings);
                        startActivity(intent);
                        finish();
                    } else if (type.equals("3")) {
                        Intent intent = new Intent(PwersonActivity.this, VideoLongActivity.class);
                        //  intent.putExtra("PostCommId", mDatas.get(position).getId());
                        intent.putExtra("GsonNameId",array.toString());
                        intent.putExtra("PostCommClassName",strings);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
    private void setAdapter(List<ChooseFriendBean.DataBean> list) {
        sourceDateListt = filledData(list);
        Collections.sort(sourceDateListt, new PinyinComparatorChoose());
        mCommTagAdapter = new SelectCommTagAdapter(sourceDateListt,PwersonActivity.this);
        mRecyclerView.setAdapter(mCommTagAdapter);

    }


    /*    //如果删除集合的值
        public void delString( s){
            for (int i = 0; i < listMySelect.size(); i++) {
                if(listMySelect.get(i).getForum_name().equals(s.getForum_name())){
                    listMySelect.remove(i);
                }
            }

            //  Toast.makeText(this, "删除"+stringList.size(), Toast.LENGTH_SHORT).show();
        }*/
    //记录选中值
    public void strings(ChooseFriendBean.DataBean bean){
        selectId_list.add(bean.getId());
        className_list.add(bean.getClass_name());
        //   Toast.makeText(this, "存储"+stringList.size(), Toast.LENGTH_SHORT).show();
    }


    private void initDatas() {
        //sideBar.setTextView(dialog);
        OkHttpUtils.post()//
                .url(XingYuInterface.GET_LABEL_CLASSS)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr,List<ChooseFriendBean.DataBean> date) {
        List<ChooseFriendBean.DataBean> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = mDatass;
        } else {
            mSortList.clear();
            for (ChooseFriendBean.DataBean sortModel : date) {
                String name = sortModel.getClass_name();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparatorChoose());
        mCommTagAdapter.updateListView(mSortList);
        mCommTagAdapter.notifyDataSetChanged();
    }


    private List<ChooseFriendBean.DataBean> filledData(List<ChooseFriendBean.DataBean> date) {
        List<ChooseFriendBean.DataBean> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();
        if (date != null || date.size() > 0) {
            for (int i = 0; i < date.size(); i++) {
                ChooseFriendBean.DataBean sortModel = new ChooseFriendBean.DataBean();
                sortModel.setClass_name(date.get(i).class_name);
                sortModel.setClass_image(date.get(i).class_image);
                sortModel.setId(date.get(i).id);
                String pinyin = PinyinUtils.getPingYin(date.get(i).class_name);
                String sortString = pinyin.substring(0, 1).toUpperCase();
                if (!sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters("#");
                    if (!indexString.contains("#")) {
                        indexString.add("#");
                    }
                } else if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString);
                    if (!indexString.contains(sortString)) {
                        indexString.add(sortString);
                    }
                }
                mSortList.add(sortModel);
            }
            Collections.sort(indexString);
        }
        return mSortList;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
