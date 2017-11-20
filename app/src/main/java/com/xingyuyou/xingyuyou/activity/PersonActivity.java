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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.PinyinUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapters.SortAdapter;
import com.xingyuyou.xingyuyou.views.ContactSortModel;
import com.xingyuyou.xingyuyou.views.EditTextWithDel;
import com.xingyuyou.xingyuyou.views.PinyinComparator;
import com.xingyuyou.xingyuyou.views.SideBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

import static java.util.Collections.sort;

public class PersonActivity extends AppCompatActivity {
    private ArrayList<ContactSortModel.DataBean> FansListBean= new ArrayList<>();
    private ArrayList<ContactSortModel.DataBean> SourceDateList= new ArrayList<>();
    ArrayList<String> id_list=new ArrayList();
    ArrayList<String> name_list=new ArrayList();
    ArrayList<String> name_=new ArrayList();
    List<String> listMySelect=new ArrayList<>();
    public static final String KEY_SELECTED = "selected";
    private int PAGENUM = 1;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private EditTextWithDel mEtSearchName;
    private Toolbar mToolbar;
    private ArrayList<ContactSortModel.DataBean> SourceDateListt;
    public static final String KEY_CID = "cid";
    public static final String KEY_NAME = "name";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {
                    //Toast.makeText(PersonActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                //解析Gson
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    FansListBean = gson.fromJson(ja.toString(),
                            new TypeToken<List<ContactSortModel.DataBean>>() {
                            }.getType());
                    SourceDateList.addAll(FansListBean);
                    setAdapter(SourceDateList);
                    //根据输入框输入值的改变来过滤搜索
                    mEtSearchName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                            //   Log.d("www",SourceDateList.get(0).getNickname()+"---"+SourceDateList.get(0).getSortLetters());
                            filterData(s.toString(),SourceDateListt);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    if (SourceDateList.size()<20) {
                        mPbNodata.setVisibility(View.GONE);
                        mTvNodata.setText("已经没有更多数据");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //更新UI
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }
        }
    };
    private ImageView image_choose;
    private TextView sure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        initToolBar();
        initDatas();
        initViews();
    }

    private void initViews() {
        mEtSearchName = (EditTextWithDel) findViewById(R.id.et_search);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sure = (TextView) findViewById(R.id.sure);
        sure.setVisibility(View.VISIBLE);
        sortListView = (ListView) findViewById(R.id.lv_contact);
        View loadingData = View.inflate(PersonActivity.this, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        TextView textView = new TextView(PersonActivity.this);
        textView.setText("");
        sortListView.setDividerHeight(0);
        //sortListView.addFooterView(loadingData);
        initEvents();

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                image_choose = (ImageView)view.findViewById(R.id.image_choose);
                ContactSortModel.DataBean bean= (ContactSortModel.DataBean) adapterView.getItemAtPosition(i);
                if(bean.getTag()==null){
                if(name_list.size()<8){
                        image_choose.setTag("1");
                        strings(bean);
                    ((ContactSortModel.DataBean) adapterView.getItemAtPosition(i)).setTag("1");
                    adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(PersonActivity.this,"亲，最多选8个", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    name_.remove("@"+bean.getNickname());
                    id_list.remove(bean.getId());
                    name_list.remove(" @"+bean.getNickname()+" ");
                    ((ContactSortModel.DataBean) adapterView.getItemAtPosition(i)).setTag(null);
                    image_choose.setTag(null);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(KEY_CID,id_list);
                intent.putExtra(KEY_NAME,name_list);
                intent.putExtra("KEY_",name_);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setAdapter(ArrayList<ContactSortModel.DataBean> SourceDateList) {
        SourceDateListt=filledData(SourceDateList);
        Collections.sort(SourceDateListt, new PinyinComparator());
        adapter = new SortAdapter(PersonActivity.this, SourceDateListt);
        sortListView.setAdapter(adapter);

    }


    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                if(adapter.getCount()!=0||adapter!=null) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        sortListView.setSelection(position);
                    }
                }
            }
        });

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
    public void strings(ContactSortModel.DataBean bean){


        id_list.add(bean.getId());
        name_list.add(" @"+bean.getNickname()+" ");
        name_.add("@"+bean.getNickname());
       // image_choose.setImageResource(R.drawable.ic_choose);
        //   Toast.makeText(this, "存储"+stringList.size(), Toast.LENGTH_SHORT).show();
    }


    private void initDatas() {
        //sideBar.setTextView(dialog);
        OkHttpUtils.post()//
                .addParams("page","1")
                .addParams("uid",UserUtils.getUserId())
                .url(XingYuInterface.FRIEND_LIST)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        handler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr,ArrayList<ContactSortModel.DataBean> date) {
        List<ContactSortModel.DataBean> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = date;
        } else {
            mSortList.clear();
            for (ContactSortModel.DataBean sortModel : date) {
                String name = sortModel.getNickname();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<ContactSortModel.DataBean> filledData(ArrayList<ContactSortModel.DataBean> date) {
        ArrayList<ContactSortModel.DataBean> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();
        if (date != null || date.size() > 0) {
            for (int i = 0; i < date.size(); i++) {
                ContactSortModel.DataBean sortModel = new ContactSortModel.DataBean();
                sortModel.setNickname(date.get(i).nickname);
                sortModel.setHead_image(date.get(i).head_image);
                sortModel.setId(date.get(i).id);
                sortModel.setEmchat_id(date.get(i).emchat_id);
                String pinyin = PinyinUtils.getPingYin(date.get(i).nickname);
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
            sort(indexString);
            sideBar.setIndexText(indexString);
            sideBar.setVisibility(View.VISIBLE);
        }
        return mSortList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
